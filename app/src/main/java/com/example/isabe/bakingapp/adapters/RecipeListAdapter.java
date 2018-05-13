package com.example.isabe.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isabe.bakingapp.R;
import com.example.isabe.bakingapp.RecipeDetailActivity;
import com.example.isabe.bakingapp.RecipeDetailFragment;
import com.example.isabe.bakingapp.objects.RecipeContent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by isabe on 5/6/2018.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener {

    public List<RecipeContent> recipesList = new ArrayList<>();
    private final RecipeOnClickListener recipesOnClickListener;
    @BindView(R.id.recipeNameListEntry)
    public TextView mTvRecipeName;

    @BindView(R.id.recipeImageView)
    public ImageView mRecipeImage;

    public Context mContext;
    private GestureDetector gestureDetector;


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && recipesOnClickListener != null && gestureDetector.onTouchEvent(e)) {
            recipesOnClickListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface RecipeOnClickListener {
        void onClick(View view, int itemId);
    }

    public RecipeListAdapter(Context context, List<RecipeContent> recipeContents, RecipeOnClickListener listener) {
        mContext = context;
        recipesList = recipeContents;
        recipesOnClickListener = listener;
        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
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
            if (recipesOnClickListener != null) {
                recipesOnClickListener.onClick(view, getAdapterPosition());
                int adapterId = getAdapterPosition();
                Intent intent = new Intent(view.getContext(), RecipeDetailFragment.class);
                intent.putExtra("recipe_id", adapterId);
            }
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
        final int currentId = recipe.getId();

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

    public RecipeContent getItem(int position) {
        return recipesList.get(position);

    }

    public void addAll(List<RecipeContent> list) {
        recipesList.addAll(list);
    }

}
