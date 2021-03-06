package com.example.isabe.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isabe.bakingapp.objects.BakingStep;
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

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.isabe.bakingapp.RecipeDetailFragment.STEP_INDEX;
import static com.example.isabe.bakingapp.RecipeDetailFragment.STEP_SELECTION;

/**
 * Created by isabe on 5/9/2018.
 */

public class StepsPlayFragment extends Fragment implements Player.EventListener {
    private static final String LOG_TAG = StepsPlayFragment.class.getSimpleName();
    private static final java.lang.String VIDEO_POSITION = "position_video";
    private static final String CURRENT_WINDOW = "window_index";
    private static final String PLAYBACK_STATE = "playback_state";
    private ExoPlayer mExoPlayer;
    private List<BakingStep> bakingStepList = new ArrayList<>();
    private BakingStep stepItem;

    @BindView(R.id.step_video)
    PlayerView mStepVideo;

    @BindView(R.id.step_image)
    ImageView mStepImage;

    @BindView(R.id.cupcake_drawable)
    ImageView mCupcakeImage;

    @BindView(R.id.step_instructions)
    TextView mDetailedInstructions;

    @BindView(R.id.button_next)
    Button mNextButton;

    @BindView(R.id.button_previous)
    Button mPreviousButton;

    private static final String EXTRA_STEP_ID = "EXTRA_ID";
    private String stepDescription;
    private String stepVideoUrl;
    private String stepImageUrl;
    private String stepLongDesc;
    private int stepId;
    private int savedStep;

    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;
    // --Commented out by Inspection (6/5/2018 11:17 PM):private boolean mTwoPane;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private final Timeline.Window currentWindow = new Timeline.Window();
    private int currentWindowIndex = 0;
    // --Commented out by Inspection (6/5/2018 11:17 PM):private RecipeStepsAdapter mRecipeAdapter;
    private long playbackPosition = 0;
    @BindBool(R.bool.isTablet)
    boolean tabletSize;
    private boolean playWhenReady;
    private int mCurrentIndex = 0;

    private Unbinder unbinder;

    public StepsPlayFragment() {

    }

    public static StepsPlayFragment newInstance(int index) {
        StepsPlayFragment exoPlayFragment = new StepsPlayFragment();
        Bundle args = new Bundle();
        args.putInt(STEP_INDEX, index);
        exoPlayFragment.setArguments(args);
        return exoPlayFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            stepItem = bundle.getParcelable(STEP_SELECTION);
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
            stepId = savedInstanceState.getInt(EXTRA_STEP_ID);
            playbackPosition = savedInstanceState.getLong(VIDEO_POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAYBACK_STATE);
            currentWindowIndex = savedInstanceState.getInt(CURRENT_WINDOW);
            Log.i(LOG_TAG, getString(R.string.saved_step_id) + stepId);
            Log.i(LOG_TAG, getString(R.string.saved_playback_pos) + playbackPosition);


            stepItem = bakingStepList.get(stepId);
            stepLongDesc = stepItem.getLongStepDescription();
            stepVideoUrl = stepItem.getVideoUrl();

            if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {
                mStepVideo.setVisibility(View.VISIBLE);
                initializeMediaSession();
                initializePlayer(Uri.parse(stepVideoUrl));
            } else {
                mStepVideo.setVisibility(View.GONE);
            }

            stepImageUrl = stepItem.getThumbnailStepUrl();
            mDetailedInstructions.setText(stepLongDesc);
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            mCupcakeImage.setVisibility(View.GONE);
            initializeMediaSession();
            initializePlayer(Uri.parse(stepVideoUrl));

            int configOrientation = getResources().getConfiguration().orientation;

            if (configOrientation == Configuration.ORIENTATION_LANDSCAPE && !tabletSize) {
                expandVideoFullScreen(mStepVideo);
                mDetailedInstructions.setVisibility(View.GONE);
                hideSystemUI();

            }
        } else {
            mStepVideo.setVisibility(View.GONE);
            mCupcakeImage.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        mStepVideo.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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

// --Commented out by Inspection START (6/5/2018 11:17 PM):
//            public void skipPrevious() {
//                Timeline currentTimeline = mExoPlayer.getCurrentTimeline();
//                if (currentTimeline.isEmpty()) {
//                    return;
//                }
//                int currentWindowIndex = mExoPlayer.getCurrentWindowIndex();
//                currentTimeline.getWindow(currentWindowIndex, currentWindow);
//                if (currentWindowIndex > 0 && (mExoPlayer.getContentPosition() <= MAX_POSITION_FOR_SEEK_TO_PREVIOUS
//                        || (currentWindow.isDynamic && !currentWindow.isSeekable))) {
//                    mExoPlayer.seekTo(currentWindowIndex - 1, C.TIME_UNSET);
//                } else {
//                    mExoPlayer.seekTo(0, C.TRACK_TYPE_DEFAULT);
//                }
//            }
// --Commented out by Inspection STOP (6/5/2018 11:17 PM)

// --Commented out by Inspection START (6/5/2018 11:17 PM):
//            public void skipNext() {
//                Timeline currentTimeline = mExoPlayer.getCurrentTimeline();
//                if (currentTimeline.isEmpty()) {
//                    return;
//                }
//                int currentWindowIndex = mExoPlayer.getCurrentWindowIndex();
//                if (currentWindowIndex < currentTimeline.getWindowCount() - 1) {
//                    mExoPlayer.seekTo(currentWindowIndex + 1, C.TIME_UNSET);
//                } else if (currentTimeline.getWindow(currentWindowIndex, currentWindow,
//                        false).isDynamic) {
//                    mExoPlayer.seekTo(currentWindowIndex, C.TIME_UNSET);
//                }
//            }
// --Commented out by Inspection STOP (6/5/2018 11:17 PM)
        });
        mMediaSession.setActive(true);

        MediaControllerCompat mediaControllerCompat = new MediaControllerCompat(getContext(), mMediaSession);
        MediaControllerCompat.setMediaController(getActivity(), mediaControllerCompat);

    }

// --Commented out by Inspection START (6/5/2018 11:17 PM):
//    public void getObject() {
//        if (getArguments() != null) {
//            stepItem = getArguments().getParcelable(STEP_SELECTION);
//        }
//    }
// --Commented out by Inspection STOP (6/5/2018 11:17 PM)


    @OnClick(R.id.button_next)
    public void toNext() {
        try {
            //getObject();
            if (stepItem != null) {
                //stepId = stepItem.getId();
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
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            stepId = stepId - 1;
        }
        Log.i(LOG_TAG, "Step " + stepId);
    }

    @OnClick(R.id.button_previous)
    public void toPrevious() {
        if (stepId > 0) {
            // getObject();
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
        ViewGroup.LayoutParams params = mStepVideo.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mStepVideo.setLayoutParams(params);
    }

    private void initializePlayer(Uri videoUri) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            mStepVideo.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(true);

            Uri uri = Uri.parse(stepVideoUrl);

            MediaSource mediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mediaSource, true, false);
            if (playbackPosition != 0)
                mExoPlayer.seekTo(currentWindowIndex, playbackPosition);
        }

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(getString(R.string.video_step)))
                .createMediaSource(uri);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            playbackPosition = mExoPlayer.getCurrentPosition();
            Log.i(LOG_TAG, getString(R.string.release_playback_pos) + playbackPosition);
            currentWindowIndex = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        //check here if this saves the current step id
        outState.putInt(EXTRA_STEP_ID, savedStep);

        if (mExoPlayer != null) {
            //  outState.putInt(EXTRA_STEP_ID, savedStep);
            outState.putLong(VIDEO_POSITION, mExoPlayer.getCurrentPosition());
            outState.putBoolean(PLAYBACK_STATE, mExoPlayer.getPlayWhenReady());
            outState.putInt(CURRENT_WINDOW, currentWindowIndex);
            Log.i(LOG_TAG, getString(R.string.onSave_playback_pos) + playbackPosition);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
            Log.i(LOG_TAG, getString(R.string.exo_playing));
        } else if (playbackState == Player.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
            Log.i(LOG_TAG, getString(R.string.exo_paused));
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {
                //initializePlayer(Uri.parse(stepVideoUrl));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        savedStep = stepId;
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
                //initializePlayer(Uri.parse(stepVideoUrl));
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

// --Commented out by Inspection START (6/5/2018 11:17 PM):
//    public int getShownIndex() {
//        int id;
//        if (getArguments() != null) {
//            return id = getArguments().getInt(STEP_INDEX);
//        } else {
//            return 0;
//        }
//    }
// --Commented out by Inspection STOP (6/5/2018 11:17 PM)
}
