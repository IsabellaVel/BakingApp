package com.example.isabe.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.isabe.bakingapp.R;
import com.example.isabe.bakingapp.objects.BakingStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isabe on 5/5/2018.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {
    private List<BakingStep> stepList;
    public final OnStepClickListener onStepClickListener;
    private int currentPosition;


    public RecipeStepsAdapter(Context context, List<BakingStep> steps, OnStepClickListener listener) {
        stepList = steps;
        onStepClickListener = listener;
    }

    @Override
    public RecipeStepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe_item_screen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        currentPosition = position;
        final BakingStep bakingStep = stepList.get(position);
        holder.mTvDescription.setText(bakingStep.getBriefStepDescription());
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_item_layout)
        LinearLayout stepLayout;

        @BindView(R.id.step_description)
        TextView stepDescr;

        @BindView(R.id.step_video_launcher_icon)
        ImageView stepVideoIcon;

        public TextView mTvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTvDescription.getText();
        }


        @Override
        public void onClick(View view) {
            onStepClickListener.stepClicked(currentPosition);
            notifyDataSetChanged();
        }
    }

    public interface OnStepClickListener {
        void stepClicked(int stepId);
    }
}

