package com.example.isabe.bakingapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isabe.bakingapp.adapters.RecipeStepsAdapter;
import com.example.isabe.bakingapp.loaders.StepsLoader;
import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<BakingStep>> {
    private static final int LOADER_ID = 1;
    private static final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();
    private Unbinder unbinder;

    private RecipeStepsAdapter recipeStepsAdapter;
    private List<BakingStep> mBakingSteps = new ArrayList<>();
    public static final String RECIPE_JSON_URI =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private RecipeStepsAdapter.OnStepClickListener onStepClickListener;
    @BindView(R.id.ingreds_details)
    TextView tvRecipeIngreds;

    @BindView(R.id.recipe_steps_list_rv)
    RecyclerView mRecipeStepsRecyclerView;

    private boolean twoPane;
    private int recipeNo;

    public static RecipeDetailFragment newInstance(int recipeNo) {
        Bundle arguments = new Bundle();
        arguments.putInt(RecipeDetailActivity.RECIPE_ID_EXTRA_FIELD, recipeNo);
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(arguments);
        return recipeDetailFragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeNo = getArguments() != null ? getArguments().getInt(RecipeDetailActivity.RECIPE_ID_EXTRA_FIELD) : 1;

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle("Baking Details");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        recipeStepsAdapter = new RecipeStepsAdapter(getActivity(), mBakingSteps, onStepClickListener);

        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public android.support.v4.content.Loader<List<BakingStep>> onCreateLoader(int id, Bundle args) {
        Uri stepsUri = Uri.parse(RECIPE_JSON_URI).buildUpon().build();
        URL stepsURL = NetworkUtils.createUrl((stepsUri).toString());
        return new StepsLoader(getActivity(), (stepsURL).toString());
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<List<BakingStep>> loader, List<BakingStep> dataSteps) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecipeStepsRecyclerView.setLayoutManager(layoutManager);

        recipeStepsAdapter = new RecipeStepsAdapter(getActivity(), dataSteps, onStepClickListener);
        recipeStepsAdapter.setHasStableIds(true);
        mRecipeStepsRecyclerView.setHasFixedSize(true);
        mRecipeStepsRecyclerView.setAdapter(recipeStepsAdapter);

        mRecipeStepsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        if (dataSteps != null && !dataSteps.isEmpty()) {
            recipeStepsAdapter.addAll(dataSteps);
            Log.e(LOG_TAG, "Successful LoadFinished for List of Steps per Recipe.");

        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<List<BakingStep>> loader) {
        recipeStepsAdapter.clear();
    }

}
