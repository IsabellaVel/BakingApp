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

import com.example.isabe.bakingapp.objects.BakingStep;
import com.google.android.exoplayer2.C;
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
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
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
    private ExoPlayer mExoPlayer;
    private PlayerView mExoPlayerView;
    private List<BakingStep> bakingStepList = new ArrayList<>();

    @BindView(R.id.step_video)
    PlayerView mStepVideo;

    @BindView(R.id.step_image)
    ImageView mStepImage;

    @BindView(R.id.step_instructions)
    TextView mDetailedInstructions;

    @BindView(R.id.button_next)
    Button mNextButton;

    @BindView(R.id.button_previous)
    Button mPreviousButton;

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

    Unbinder unbinder;

    public static StepsPlayFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_STEP_ID, index);
        StepsPlayFragment exoPlayFragment = new StepsPlayFragment();
        exoPlayFragment.setArguments(args);
        return exoPlayFragment;
    }

    public StepsPlayFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            BakingStep stepsList = getArguments().getParcelable(RecipeDetailFragment.STEP_SELECTION);
            stepDescription = stepsList.getBriefStepDescription();
            stepLongDesc = stepsList.getLongStepDescription();
            stepVideoUrl = stepsList.getVideoUrl();
            stepImageUrl = stepsList.getThumbnailStepUrl();
        }
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDetailedInstructions.setText(stepDescription);

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

            public void onPause() {
                mExoPlayer.setPlayWhenReady(false);
            }

            @OnClick(R.id.button_previous)
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
            @OnClick(R.id.button_next)
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
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mStepVideo.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);

            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            String userAgent = Util.getUserAgent(getContext(), "Video Step");

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent,
                    bandwidthMeter);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);
        }

    }

    public void releasePlayer() {
        if (mExoPlayer != null) {
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
}
