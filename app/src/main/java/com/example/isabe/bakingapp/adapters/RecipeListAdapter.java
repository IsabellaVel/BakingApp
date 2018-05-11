package com.example.isabe.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isabe.bakingapp.R;
import com.example.isabe.bakingapp.RecipeDetailActivity;
import com.example.isabe.bakingapp.RecipeListActivity;
import com.example.isabe.bakingapp.objects.RecipeContent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isabe on 5/6/2018.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    public List<RecipeContent> recipesList = new ArrayList<>();
    private final RecipeOnClickListener recipesOnClickListener;
    @BindView(R.id.recipeNameListEntry)
    public TextView mTvRecipeName;

    @BindView(R.id.recipeImageView)
    public ImageView mRecipeImage;

    public Context mContext;

    public interface RecipeOnClickListener {
        void onClick(int itemId);
    }

    public RecipeListAdapter(Context context, List<RecipeContent> recipeContents, RecipeOnClickListener listener) {
        mContext = context;
        recipesList = recipeContents;
        recipesOnClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        @BindView(R.id.recipeNameListEntry)
        public TextView mTvRecipeName;
        @BindView(R.id.recipeImageView)
        public ImageView mRecipeImage;

        public int currentId;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //     int adapterId = getAdapterPosition();
            //   recipesOnClickListener.onClick(adapterId);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe_list_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecipeContent recipe = recipesList.get(position);
        final int currentId = Integer.parseInt(recipe.getId());

        final String imagePath = recipe.getRecipeImage();
        if (imagePath.isEmpty()) {
            holder.mRecipeImage.setImageResource(R.drawable.recipe_icon);
        } else {
            Picasso.with(mContext)
                    .load(imagePath)
                    .placeholder(R.drawable.recipe_icon)
                    .into(holder.mRecipeImage);
        }
        holder.mTvRecipeName.setText(recipe.getRecipeName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDetailActivity = new Intent(view.getContext(), RecipeDetailActivity.class);
                intentDetailActivity.putExtra("id", currentId);
                mContext.startActivity(intentDetailActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public void clear() {
        recipesList.clear();
    }

    public void addAll(List<RecipeContent> list) {
        recipesList.addAll(list);
    }

}
