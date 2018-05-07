package com.example.isabe.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isabe.bakingapp.R;
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
    private RecipeOnClickListener recipesOnClickListener;
    @BindView(R.id.recipeNameListEntry)
    public TextView mTvRecipeName;

    @BindView(R.id.recipeImageView)
    public ImageView mRecipeImage;

    public int currentId;
    public Context mContext;

    public RecipeListAdapter(Context context, List<RecipeContent> recipeContents, RecipeOnClickListener listener) {
        mContext = context;
        recipesList = recipeContents;
        recipesOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe_list_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        currentId = position;
        final RecipeContent recipe = recipesList.get(position);
        String imagePath = recipe.getRecipeImage();

        holder.mTvRecipeName.setText(recipe.getRecipeName());

        Picasso.with(mContext)
                .load(imagePath)
                .placeholder(R.drawable.recipe_icon)
                .into(holder.mRecipeImage);
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTvRecipeName;
        public ImageView mRecipeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recipesOnClickListener.recipeClicked(currentId);
        }
    }

    public void clear() {
        recipesList.clear();
    }

    public void addAll(List<RecipeContent> list) {
        recipesList.addAll(recipesList);
    }

    public interface RecipeOnClickListener {
        void recipeClicked(int itemId);
    }
}
