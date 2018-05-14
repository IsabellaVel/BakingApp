package com.example.isabe.bakingapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isabe.bakingapp.adapters.RecipeListAdapter;
import com.example.isabe.bakingapp.adapters.RecipeStepsAdapter;
import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.objects.RecipeContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.isabe.bakingapp.RecipeListFragment.RECIPE_SELECTION;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    private static final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();
    private Unbinder unbinder;
    public static final String STEP_SELECTION = "STEP_ID";

    private RecipeStepsAdapter recipeStepsAdapter;
    private RecipeListAdapter mRecipeAdapter;
    private List<BakingStep> mBakingSteps = new ArrayList<>();

    private RecipeStepsAdapter.OnStepClickListener onStepClickListener;
    @BindView(R.id.ingreds_details)
    TextView tvRecipeIngreds;

    @BindView(R.id.recipe_steps_list_rv)
    RecyclerView mRecipeStepsRecyclerView;

    private boolean twoPane;
    private int recipeNo;

    public static RecipeDetailFragment newInstance(int recipeNo) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(RecipeListFragment.RECIPE_SELECTION, recipeNo);
        recipeDetailFragment.setArguments(bundle);
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

        Bundle args = getArguments();
        if (args != null) {
            RecipeContent recipeContent = args.getParcelable(RECIPE_SELECTION);
            recipeNo = recipeContent.getId();
            mBakingSteps = recipeContent.getBakingSteps();
        }


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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecipeStepsRecyclerView.setLayoutManager(layoutManager);

        recipeStepsAdapter.setHasStableIds(true);
        mRecipeStepsRecyclerView.setHasFixedSize(true);
        mRecipeStepsRecyclerView.setAdapter(recipeStepsAdapter);

        mRecipeStepsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        setUpRecyclerListener(mRecipeStepsRecyclerView);

        if (mBakingSteps != null && !mBakingSteps.isEmpty()) {
            recipeStepsAdapter.addAll(mBakingSteps);
            Log.e(LOG_TAG, "Successful LoadFinished for List of Steps per Recipe.");

        }

        return rootView;
    }

    public void setUpRecyclerListener(RecyclerView recyclerListener) {
        mRecipeStepsRecyclerView = recyclerListener;

        mRecipeStepsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecipeStepsRecyclerView,
                new RecyclerTouchListener.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //TODO
                        BakingStep thisRecipeStep = recipeStepsAdapter.getItem(position);
                        assert thisRecipeStep != null;
                        int thisRecipeId = thisRecipeStep.getId();
                        String thisStepShortDesc = thisRecipeStep.getBriefStepDescription();
                        String thisStepLongDesc = thisRecipeStep.getLongStepDescription();
                        String thisStepVideoUrl = thisRecipeStep.getVideoUrl();
                        String thisStepImageUrl = thisRecipeStep.thumbnailStepUrl;

                        Fragment exoPlayerFragment = new RecipeStepsExoPlayFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(STEP_SELECTION, thisRecipeStep);
                        exoPlayerFragment.setArguments(bundle);
                    }
                }));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}



