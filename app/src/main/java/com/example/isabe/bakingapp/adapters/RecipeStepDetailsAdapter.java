package com.example.isabe.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isabe.bakingapp.R;
import com.example.isabe.bakingapp.objects.BakingStep;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by isabe on 5/12/2018.
 */

class RecipeStepDetailsAdapter extends ArrayAdapter<BakingStep> {

    private final int currentPosition;
    private final Context mContext;
    private String mVideoUrl;

    @BindView(R.id.step_video)
    PlayerView mStepVideo;
// --Commented out by Inspection STOP (6/5/2018 11:17 PM)

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
