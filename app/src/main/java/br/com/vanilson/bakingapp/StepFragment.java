package br.com.vanilson.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import br.com.vanilson.bakingapp.model.StepModel;

import static br.com.vanilson.bakingapp.StepActivity.STEP_DETAIL_KEY;

public class StepFragment extends Fragment {

    public StepFragment() {
    }

    private StepModel mStep;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private TextView mStepTitleTv;
    private TextView mStepDescTv;
    private Uri mVideoUri;
    private Long mPosition = 0l;
    private Boolean mPlayWhenReady = true;

    private static final String PLAYER_POSITION_KEY = "position_key";
    private static final String PLAYER_PLAY_WHEN_READY_KEY = "play_when_ready_key";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int screenLayout = R.layout.fragment_step;
        int orientation = getResources().getConfiguration().orientation;
        if (!getResources().getBoolean(R.bool.isTablet) && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenLayout = R.layout.fragment_step_land;
        }


        final View rootView = inflater.inflate(screenLayout, container, false);
        mStepTitleTv = rootView.findViewById(R.id.step_title);
        mStepDescTv = rootView.findViewById(R.id.step_desc);
        mPlayerView = rootView.findViewById(R.id.playerView);

        Bundle bundle = getArguments();
        if (bundle == null) {
            System.out.println("NO ARGUMENT FOUND!!!");
            return rootView;
        }

        mStep = bundle.getParcelable(STEP_DETAIL_KEY);
        if (mStepTitleTv != null && mStepDescTv != null) {
            mStepTitleTv.setText(mStep.getShortDescription());
            mStepDescTv.setText(mStep.getDescription());
        }

        mVideoUri = Uri.parse(mStep.getVideoURL());
        if (savedInstanceState != null && savedInstanceState.containsKey(PLAYER_POSITION_KEY)) {
            mPosition = savedInstanceState.getLong(PLAYER_POSITION_KEY, 0l);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY_KEY);
        }

        return rootView;

    }

    private void initializePlayer() {
        Context context = getContext();
        if (mExoPlayer == null) {

            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            if (!mVideoUri.toString().isEmpty()) {

                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                        getContext(),
                        Util.getUserAgent(getContext(), getString(R.string.app_name)),
                        bandwidthMeter);
                MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mVideoUri);
                mExoPlayer.prepare(mediaSource);
                if(mPosition != 0l){
                    mExoPlayer.seekTo(mPosition);
                }
                mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            }

        }
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        releasePlayer();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            initializePlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            initializePlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mExoPlayer != null) {
            outState.putLong(PLAYER_POSITION_KEY, mExoPlayer.getCurrentPosition());
            outState.putBoolean(PLAYER_PLAY_WHEN_READY_KEY, mPlayWhenReady);
        }
    }
}
