package com.example.isabe.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.objects.Ingredient;
import com.example.isabe.bakingapp.objects.RecipeContent;

import java.util.List;

import butterknife.BindView;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {
    // --Commented out by Inspection (6/5/2018 11:17 PM):private static final int DEFAULT_VALUE = 1;
    private static final String RECIPE_ID_EXTRA_FIELD = "recipe_id";
    // --Commented out by Inspection (6/5/2018 11:17 PM):int clickedId;
    private boolean mTwoPane;
    @BindView(R.id.ingreds_details)
    TextView mTvIngreds;
Context mContext;

    private RecipeContent mRecipeDetails;

    private void sendData(Context context, Parcelable object) {
        context = getBaseContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeListFragment.RECIPE_SELECTION, mRecipeDetails);
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_details_container, recipeDetailFragment)
                .commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            mRecipeDetails = savedInstanceState.getParcelable(RecipeListFragment.RECIPE_SELECTION);
        }else {

        mRecipeDetails = getIntent().getParcelableExtra(RecipeListFragment.RECIPE_SELECTION);
        int recipeId = mRecipeDetails.getId();
        String recipeName = mRecipeDetails.getRecipeName();
        List<Ingredient> thisRecipeIngreds = mRecipeDetails.getIngredients();
        List<BakingStep> thisRecipeSteps = mRecipeDetails.getBakingSteps();
        String thisRecipeImage = mRecipeDetails.getRecipeImage();

        recipeId = getIntent().getIntExtra(RECIPE_ID_EXTRA_FIELD, recipeId);

        sendData(getBaseContext(), mRecipeDetails);
        // Create the detail fragment and add it to the activity
        // using a fragment transaction.


        // TODO check if this would be the proper view from the layout. Otherwise, create a separate layout file.
        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            //sendData(mContext, mRecipeDetails);

            // TODO Add fragment for Video and steps instructions
            StepsPlayFragment recipeStepsFragment = new StepsPlayFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_recycler, recipeStepsFragment)
                    .commit();

        }

    }}

}
