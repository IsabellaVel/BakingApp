package com.example.isabe.bakingapp;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isabe.bakingapp.adapters.RecipeStepsAdapter;
import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.objects.RecipeContent;

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
public class RecipeDetailFragment extends Fragment {
    private Unbinder unbinder;
    private RecipeStepsAdapter recipeStepsAdapter;
    private List<BakingStep> mBakingSteps;
    private RecipeStepsAdapter.OnStepClickListener onStepClickListener;
    @BindView(R.id.ingreds_details)
    TextView tvRecipeIngreds;

    @BindView(R.id.recipe_steps_list_rv)
    RecyclerView mRecipeStepsRecyclerView;

    private boolean twoPane;


    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy recipeName this fragment is presenting.
     */
    private RecipeContent.RecipeItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy recipeName specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load recipeName from a recipeName provider.
            mItem = RecipeContent.RECIPE_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.recipeName);
            }
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
        mRecipeStepsRecyclerView.setHasFixedSize(true);
        mRecipeStepsRecyclerView.setAdapter(recipeStepsAdapter);

        mRecipeStepsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));



        return rootView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }
}
