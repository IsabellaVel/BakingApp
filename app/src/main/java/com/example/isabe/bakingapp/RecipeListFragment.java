package com.example.isabe.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isabe.bakingapp.adapters.RecipeListAdapter;
import com.example.isabe.bakingapp.adapters.RecyclerTouchListener;
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
    public static final String RECIPE_SELECTION = "thisRecipe";
    private static final int LOADER_ID = 11;
    private static final String LOG_TAG = RecipeListFragment.class.getSimpleName();
    // --Commented out by Inspection (6/5/2018 11:17 PM):private static final int REQUEST_CODE = 1;
    private RecipeListAdapter mRecipeAdapter;

    @BindView(R.id.recipes_list_rv)
    RecyclerView mRecipeListRv;

    private List<RecipeContent> mRecipeList;
    private RecyclerTouchListener.ItemClickListener mRecyclerTouchListener;
    @BindInt(R.integer.grid_column_number)
    int gridColNo;

    private Unbinder unbinder;

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

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
        return view;
    }

    @Override
    public android.support.v4.content.Loader<List<RecipeContent>> onCreateLoader(int i, Bundle bundle) {
        Uri recipeUri = Uri.parse(RECIPE_JSON_URI).buildUpon().build();
        URL recipeURL = NetworkUtils.createUrl((recipeUri).toString());
        return new RecipeLoader(getActivity(), (recipeURL).toString());
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<RecipeContent>> loader, List<RecipeContent> dataRecipe) {
        //setup the recyclerView
        mRecipeListRv.setLayoutManager(new GridLayoutManager(getContext(), gridColNo));

        mRecipeAdapter = new RecipeListAdapter(getActivity(), dataRecipe,
                 mRecyclerTouchListener);
        mRecipeAdapter.setHasStableIds(true);
        mRecipeListRv.setHasFixedSize(true);
        mRecipeListRv.setAdapter(mRecipeAdapter);

        mRecipeListRv.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecipeListRv,
                new RecyclerTouchListener.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //TODO
                        RecipeContent thisRecipe = mRecipeAdapter.getItem(position);
                        assert thisRecipe != null;
                        int thisRecipeId = thisRecipe.getId();
                        String thisRecipeName = thisRecipe.getRecipeName();
                        List thisRecipeIngredients = thisRecipe.getIngredients();
                        List thisRecipeBakingSteps = thisRecipe.getBakingSteps();
                        String thisRecipeImage = thisRecipe.getRecipeImage();

                        Intent showDetailsRecipe = new Intent(getActivity().getBaseContext(), RecipeDetailActivity.class);
                        showDetailsRecipe.putExtra(RECIPE_SELECTION, thisRecipe);
                        getActivity().startActivity(showDetailsRecipe);
                    }
                }));

        //setup the adapter
        //  mRecipeAdapter.clear();
        if (dataRecipe != null && !dataRecipe.isEmpty()) {
            mRecipeAdapter.addAll(dataRecipe);
            Log.e(LOG_TAG, getString(R.string.load_finished_recipes));
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<RecipeContent>> loader) {
        mRecipeAdapter.clear();

    }

}
