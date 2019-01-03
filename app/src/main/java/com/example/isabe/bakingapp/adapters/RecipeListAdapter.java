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

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder>
        implements RecyclerView.OnItemTouchListener {

    private List<RecipeContent> recipesList = new ArrayList<>();
    private final RecyclerTouchListener.ItemClickListener mRecyclerTouchListener;
    @BindView(R.id.recipeNameListEntry)
    public TextView mTvRecipeName;
    @BindView(R.id.recipeImageView)
    public ImageView mRecipeImage;


    private final Context mContext;
    private final GestureDetector gestureDetector;


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mRecyclerTouchListener != null && gestureDetector.onTouchEvent(e)) {
            mRecyclerTouchListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public RecipeListAdapter(Context context, List<RecipeContent> recipeContents,
                             RecyclerTouchListener.ItemClickListener listener) {
        mContext = context;
        recipesList = recipeContents;
        mRecyclerTouchListener = listener;
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

        // --Commented out by Inspection (6/5/2018 11:17 PM):public int currentId;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mRecyclerTouchListener != null) {
                mRecyclerTouchListener.onClick(view, this.getLayoutPosition());

                int itemId = this.getLayoutPosition();
                Intent intent = new Intent(view.getContext(), RecipeDetailFragment.class);
                intent.putExtra("recipe_id", itemId);
            }
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
            holder.mRecipeImage.setImageResource(R.drawable.recipe_icon_density);
        } else {
            Picasso.with(mContext)
                    .load(imagePath)
                    .placeholder(R.drawable.recipe_icon_density)
                    .into(holder.mRecipeImage);
        }
        holder.mTvRecipeName.setText(recipe.getRecipeName());

    }

    @Override
    public int getItemCount() {
        if (recipesList == null) {
            return 0;
        } else {
            return recipesList.size();
        }
    }

    public void clear() {
        recipesList.clear();
    }

    public RecipeContent getItem(int position) {
        return recipesList.get(position);

    }

    public void addAll(List<RecipeContent> list) {
        if (list == null)
            recipesList.addAll(list);
    }

}
