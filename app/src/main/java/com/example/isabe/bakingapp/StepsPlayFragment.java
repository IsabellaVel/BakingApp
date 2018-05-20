package com.example.isabe.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isabe.bakingapp.adapters.RecipeStepsAdapter;
import com.example.isabe.bakingapp.objects.BakingStep;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by isabe on 5/9/2018.
 */

public class StepsPlayFragment extends Fragment implements Player.EventListener {
    private static final String LOG_TAG = StepsPlayFragment.class.getSimpleName();
    private static final java.lang.String VIDEO_POSITION = "position_video";
    private ExoPlayer mExoPlayer;
    private PlayerView mExoPlayerView;
    private List<BakingStep> bakingStepList = new ArrayList<>();
    private BakingStep stepItem;

    @BindView(R.id.step_video)
    PlayerView mStepVideo;

    @BindView(R.id.step_image)
    ImageView mStepImage;

    @BindView(R.id.step_instructions)
    TextView mDetailedInstructions;

    @BindView(R.id.button_next)
    public Button mNextButton;

    @BindView(R.id.button_previous)
    public Button mPreviousButton;

    private static final String EXTRA_STEP_ID = "EXTRA_ID";
    private static final String EXTRA_VIDEO_ID = "VIDEO_ID";
    private static final String EXTRA_IMAGE_ID = "IMAGE_ID";
    private String stepDescription;
    private String stepVideoUrl;
    private String stepImageUrl;
    private String stepLongDesc;
    private int stepId;

    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;
    private boolean mTwoPane;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Timeline.Window currentWindow = new Timeline.Window();
    private int currentWindowIndex = 0;
    private RecipeStepsAdapter mRecipeAdapter;
    private long playbackPosition = 0;

    Unbinder unbinder;

    public StepsPlayFragment() {

    }

    public static StepsPlayFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_STEP_ID, index);
        StepsPlayFragment exoPlayFragment = new StepsPlayFragment();
        exoPlayFragment.setArguments(args);
        return exoPlayFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            stepItem = getArguments().getParcelable(RecipeDetailFragment.STEP_SELECTION);
            stepId = stepItem.getId();
            stepDescription = stepItem.getBriefStepDescription();
            stepLongDesc = stepItem.getLongStepDescription();
            stepVideoUrl = stepItem.getVideoUrl();
            stepImageUrl = stepItem.getThumbnailStepUrl();
        }
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        bakingStepList = RecipeDetailFragment.getListOfSteps();
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(VIDEO_POSITION, mExoPlayer.getCurrentPosition());
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDetailedInstructions.setText(stepLongDesc);
        mDetailedInstructions.setTextSize(20);

        if (stepImageUrl == null || stepImageUrl.isEmpty()) {
            mStepImage.setVisibility(View.GONE);
        }
        if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {
            mStepVideo.setVisibility(View.VISIBLE);
            initializeMediaSession();
            initializePlayer(Uri.parse(stepVideoUrl));

            int configOrientation = getResources().getConfiguration().orientation;

            if (configOrientation == Configuration.ORIENTATION_LANDSCAPE && !mTwoPane) {
                expandVideoFullScreen(mStepVideo);
                mDetailedInstructions.setVisibility(View.GONE);

            }
        } else {
            mStepVideo.setVisibility(View.GONE);
        }
    }


    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), LOG_TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT);
        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                mExoPlayer.setPlayWhenReady(true);
            }

            public void skipPrevious() {
                Timeline currentTimeline = mExoPlayer.getCurrentTimeline();
                if (currentTimeline.isEmpty()) {
                    return;
                }
                int currentWindowIndex = mExoPlayer.getCurrentWindowIndex();
                currentTimeline.getWindow(currentWindowIndex, currentWindow);
                if (currentWindowIndex > 0 && (mExoPlayer.getContentPosition() <= MAX_POSITION_FOR_SEEK_TO_PREVIOUS
                        || (currentWindow.isDynamic && !currentWindow.isSeekable))) {
                    mExoPlayer.seekTo(currentWindowIndex - 1, C.TIME_UNSET);
                } else {
                    mExoPlayer.seekTo(0, C.TRACK_TYPE_DEFAULT);
                }
            }

            public void skipNext() {
                Timeline currentTimeline = mExoPlayer.getCurrentTimeline();
                if (currentTimeline.isEmpty()) {
                    return;
                }
                int currentWindowIndex = mExoPlayer.getCurrentWindowIndex();
                if (currentWindowIndex < currentTimeline.getWindowCount() - 1) {
                    mExoPlayer.seekTo(currentWindowIndex + 1, C.TIME_UNSET);
                } else if (currentTimeline.getWindow(currentWindowIndex, currentWindow,
                        false).isDynamic) {
                    mExoPlayer.seekTo(currentWindowIndex, C.TIME_UNSET);
                }
            }
        });
        mMediaSession.setActive(true);

        MediaControllerCompat mediaControllerCompat = new MediaControllerCompat(getContext(), mMediaSession);
        MediaControllerCompat.setMediaController(getActivity(), mediaControllerCompat);

    }

    @OnClick(R.id.button_next)
    public void toNext() {
        try {
            stepId = stepItem.getId();
            stepId = stepId + 1;

            stepItem = bakingStepList.get(stepId);
            stepLongDesc = stepItem.getLongStepDescription();
            stepVideoUrl = stepItem.getVideoUrl();
            releasePlayer();

            if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {
                mStepVideo.setVisibility(View.VISIBLE);
                initializeMediaSession();
                initializePlayer(Uri.parse(stepVideoUrl));
            } else {
                mStepVideo.setVisibility(View.GONE);
            }

            stepImageUrl = stepItem.getThumbnailStepUrl();
            mDetailedInstructions.setText(stepLongDesc);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            stepId = stepId - 1;
        }
    }

    @OnClick(R.id.button_previous)
    public void toPrevious() {
        if (stepId > 0) {
            stepId = stepItem.getId();
            stepId = stepId - 1;

            stepItem = bakingStepList.get(stepId);
            stepLongDesc = stepItem.getLongStepDescription();
            stepVideoUrl = stepItem.getVideoUrl();
            releasePlayer();

            if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {
                mStepVideo.setVisibility(View.VISIBLE);
                initializeMediaSession();
                initializePlayer(Uri.parse(stepVideoUrl));
            } else {
                mStepVideo.setVisibility(View.GONE);
            }
            stepImageUrl = stepItem.getThumbnailStepUrl();

            mDetailedInstructions.setText(stepLongDesc);
        } else if (stepId == 0) {
            mPreviousButton.setEnabled(false);
        }

    }

    private void expandVideoFullScreen(PlayerView playerView) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams)
                mStepVideo.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        mStepVideo.setLayoutParams(params);
    }

    public void testExpand(PlayerView playerView) {
        playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        playerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private void initializePlayer(Uri videoUri) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            mStepVideo.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(currentWindowIndex, playbackPosition);

            Uri uri = Uri.parse(stepVideoUrl);

            MediaSource mediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mediaSource, true, false);
        }

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("Video Step"))
                .createMediaSource(uri);
    }

    public void releasePlayer() {
        if (mExoPlayer != null) {
            playbackPosition = mExoPlayer.getCurrentPosition();
            currentWindowIndex = mExoPlayer.getCurrentWindowIndex();
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.release();
            mExoPlayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
            Log.i(LOG_TAG, "ExoPlayer is playing.");
        } else if (playbackState == Player.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
            Log.i(LOG_TAG, "ExoPlayer is paused.");
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {
                initializeMediaSession();
                initializePlayer(Uri.parse(stepVideoUrl));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {
                initializeMediaSession();
                initializePlayer(Uri.parse(stepVideoUrl));
            }
        }
    }


    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
}
