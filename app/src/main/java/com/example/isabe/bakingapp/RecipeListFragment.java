package com.example.isabe.bakingapp;

import android.support.v4.app.LoaderManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isabe.bakingapp.adapters.RecipeListAdapter;
import com.example.isabe.bakingapp.loaders.RecipeLoader;
import com.example.isabe.bakingapp.objects.RecipeContent;
import com.example.isabe.bakingapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by isabe on 5/6/2018.
 */

public class RecipeListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<RecipeContent>> {

    private static final String RECIPE_JSON_URI =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final int LOADER_ID = 11;
    private static final String LOG_TAG = RecipeListFragment.class.getSimpleName();
    private RecipeListAdapter mRecipeAdapter;

    @BindView(R.id.recipes_list_rv)
    RecyclerView mRecipeListRv;

    private RecyclerView.LayoutManager layoutManager;

    private List<RecipeContent> mRecipeList;
    private RecipeListAdapter.RecipeOnClickListener mRecipeOnClickListener;
    @BindInt(R.integer.grid_column_number)
    int gridColNo;

    Unbinder unbinder;

    public RecipeListFragment() {
    }

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        mRecipeAdapter = new RecipeListAdapter(getActivity(), mRecipeList, mRecipeOnClickListener);

        mRecipeAdapter.setHasStableIds(true);


        LoaderManager loaderManager = getLoaderManager();
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return view;
    }

    @Override
    public android.support.v4.content.Loader<List<RecipeContent>> onCreateLoader(int i, Bundle bundle) {
        Uri recipeUri = Uri.parse(RECIPE_JSON_URI).buildUpon().build();
        URL recipeURL = NetworkUtils.createUrl((recipeUri).toString());
        return new RecipeLoader(getActivity(), (recipeURL).toString());
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<RecipeContent>> loader, List<RecipeContent> data) {
        //setup the recyclerView
        layoutManager = new RecyclerView.LayoutManager() {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return null;
            }
        };

        mRecipeAdapter = new RecipeListAdapter(getActivity(), mRecipeList, mRecipeOnClickListener);
        mRecipeListRv.setLayoutManager(layoutManager);
        mRecipeListRv.setHasFixedSize(true);
        mRecipeListRv.setAdapter(mRecipeAdapter);

        //setup the adapter
        mRecipeAdapter.clear();
        if (mRecipeList != null && !mRecipeList.isEmpty()) {
            mRecipeAdapter.addAll(mRecipeList);
            Log.e(LOG_TAG, "Successful LoadFinished for List of recipes.");
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<RecipeContent>> loader) {
        mRecipeAdapter.clear();

    }
}
