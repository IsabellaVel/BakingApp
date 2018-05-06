package com.example.isabe.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isabe.bakingapp.R;
import com.example.isabe.bakingapp.RecipeDetailActivity;
import com.example.isabe.bakingapp.RecipeDetailFragment;
import com.example.isabe.bakingapp.RecipeListActivity;
import com.example.isabe.bakingapp.objects.RecipeContent;

import java.util.List;

/**
 * Created by isabe on 5/6/2018.
 */


public class RecipeListAdapterDefaultGenerated
        extends RecyclerView.Adapter<RecipeListAdapterDefaultGenerated.ViewHolder> {

    private final RecipeListActivity mParentActivity;
    private final List<RecipeContent.RecipeItem> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecipeContent.RecipeItem item = (RecipeContent.RecipeItem) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, item.id);
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, item.id);

                context.startActivity(intent);
            }
        }
    };

    public RecipeListAdapterDefaultGenerated(RecipeListActivity parent,
                                             List<RecipeContent.RecipeItem> items,
                                             boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mContentView.setText(mValues.get(position).recipeName);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mContentView = (TextView) view.findViewById(R.id.recipeName);
        }
    }
}
