package com.example.isabe.bakingapp.adapters;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.isabe.bakingapp.R;
import com.example.isabe.bakingapp.objects.BakingStep;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isabe on 5/12/2018.
 */

public class RecipeStepDetailsAdapter extends ArrayAdapter<BakingStep> {

    private int currentPosition;
    private Context mContext;
    public String mVideoUrl;

    @BindView(R.id.step_video)
    PlayerView mStepVideo;

    @BindView(R.id.step_image)
    ImageView mStepImage;

    @BindView(R.id.step_instructions)
    TextView mDetailedInstructions;

    public RecipeStepDetailsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
        currentPosition = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View detailView = convertView;
        if (detailView == null) {
            detailView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fragment_step_details, parent, false
            );
        }
        BakingStep bakingStep = getItem(position);
        String imagePath = bakingStep.getThumbnailStepUrl();
        String videoPath = bakingStep.getVideoUrl();

        if (imagePath != null && !imagePath.isEmpty()) {
            Picasso.with(mContext)
                    .load(imagePath)
                    .into(mStepImage);
        }
        mVideoUrl = videoPath;
        return detailView;
    }


}
