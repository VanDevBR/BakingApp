package br.com.vanilson.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        mStepTitleTv = rootView.findViewById(R.id.step_title);
        mStepDescTv = rootView.findViewById(R.id.step_desc);
        mPlayerView = rootView.findViewById(R.id.playerView);

        Bundle bundle = getArguments();
        if (bundle == null) {
            System.out.println("NO ARGUMENT FOUND!!!");
            return rootView;
        }

        mStep = bundle.getParcelable(STEP_DETAIL_KEY);
        if(mStepTitleTv != null && mStepDescTv != null) {
            mStepTitleTv.setText(mStep.getShortDescription());
            mStepDescTv.setText(mStep.getDescription());
        }

        try {
            initializePlayer(getContext(), Uri.parse(mStep.getVideoURL()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return rootView;


    }

    private void initializePlayer(Context context, Uri mediaUri) {
        if (mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            if(!mediaUri.toString().isEmpty()){
                String userAgent = Util.getUserAgent(context, "BakingApp");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        context, userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
            }

        }
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }
}
