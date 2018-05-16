package com.example.isabe.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isabe.bakingapp.R;
import com.example.isabe.bakingapp.RecipeStepActivity;
import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.objects.RecipeContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isabe on 5/5/2018.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {
    private List<BakingStep> stepList = new ArrayList<>();
    public RecyclerTouchListener.ItemClickListener mRecyclerTouchListener;
    private int adapterPosition;
    private View mView;
    private static int DEFAULT_STEP_POSITION = 0;
    private Context mContext;
    private RecipeContent recipeItem;
    private BakingStep stepItem;

    @BindView(R.id.list_step_name)
    TextView mStepName;

    /**
     * public interface OnStepClickListener {
     * void onClick(int stepId);
     * }
     **/
    public RecipeStepsAdapter(Context context, List<BakingStep> steps, RecyclerTouchListener listener) {
        mContext = context;
        stepList = steps;
        mRecyclerTouchListener = (RecyclerTouchListener.ItemClickListener) listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.list_step_name)
        TextView mStepName;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View view) {
            if (mRecyclerTouchListener != null) {
                adapterPosition = this.getLayoutPosition();
                mRecyclerTouchListener.onClick(view, adapterPosition);

                Intent intent = new Intent(view.getContext(), RecipeStepActivity.class);
                intent.putExtra("step_id", adapterPosition);
                view.getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public RecipeStepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_step_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BakingStep bakingStep = stepList.get(position);
        adapterPosition = bakingStep.getId();
        holder.mStepName.setText(bakingStep.getBriefStepDescription());
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public BakingStep getItem(int position) {
        return stepList.get(position);

    }


    public void clear() {
        stepList.clear();
    }

    public void addAll(List<BakingStep> list) {
        stepList.addAll(list);
    }

}

