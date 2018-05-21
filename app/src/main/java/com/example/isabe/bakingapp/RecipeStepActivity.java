package com.example.isabe.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.objects.RecipeContent;

import java.util.List;

/**
 * Created by isabe on 5/13/2018.
 */

public class RecipeStepActivity extends AppCompatActivity {
    public static final String RECIPE_ID = "RECIPE_ID";
    private static final int DEFAULT_ID = 1;
    public static final String STEP_ID = "step_id";
    private static final int DEFAULT_STEP_ID = 0;
    public BakingStep mStepItem;
    private RecipeContent recipeItem;
    public static List<BakingStep> thisRecipeSteps;
    int stepId;

    public static Intent createIntent(Context context, int recipeId, int stepId) {
        Intent intent = new Intent(context, RecipeStepActivity.class);
        intent.putExtra(RECIPE_ID, recipeId);
        intent.putExtra(STEP_ID, stepId);
        return intent;
    }

    public void attachSteps(Context context, Parcelable step) {
        mStepItem = getIntent().getParcelableExtra(RecipeDetailFragment.STEP_SELECTION);

        context = getBaseContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeDetailFragment.STEP_SELECTION, mStepItem);
        StepsPlayFragment playFragment = new StepsPlayFragment();
        playFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.stepFragmentContainer, playFragment)
                .commit();
    }

    /**
     * public void attachStepsFromRecipe(Context context, Parcelable object){
     * //add bundle for Recipe and extract stepList
     * Bundle bundleSteps = new Bundle();
     * bundleSteps.putParcelable(RecipeListFragment.RECIPE_SELECTION, recipeItem);
     * StepsPlayFragment playFragment = new StepsPlayFragment();
     * playFragment.setArguments(bundleSteps);
     * getSupportFragmentManager().beginTransaction()
     * .add(R.id.stepFragmentContainer, playFragment)
     * .commit();
     * }
     **/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        attachSteps(getBaseContext(), mStepItem);
        //attachStepsFromRecipe(getBaseContext(), recipeItem);

        StepsPlayFragment stepsPlayFragment =
                (StepsPlayFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.stepFragmentContainer);

        if (savedInstanceState != null) {
        //    recipeItem = savedInstanceState.getParcelable(RecipeListFragment.RECIPE_SELECTION);
            mStepItem = savedInstanceState.getParcelable(RecipeDetailFragment.STEP_SELECTION);
        }


        mStepItem = getIntent().getParcelableExtra(RecipeDetailFragment.STEP_SELECTION);
        int stepParcelId = mStepItem.getId();
        String stepShortName = mStepItem.getBriefStepDescription();
        String stepLongDesc = mStepItem.getLongStepDescription();
        String stepVideoUrl = mStepItem.getVideoUrl();
        String stepImageUrl = mStepItem.getThumbnailStepUrl();

        stepParcelId = getIntent().getIntExtra(STEP_ID, stepId);

        //add parcelable for steps of recipe
        //   recipeItem = getIntent().getParcelableExtra(RecipeListFragment.RECIPE_SELECTION);
        thisRecipeSteps = RecipeDetailFragment.getListOfSteps();


        if (savedInstanceState == null) {
            mStepItem = getIntent().getParcelableExtra(RecipeDetailFragment.STEP_SELECTION);
            attachSteps(getBaseContext(), mStepItem);
//        attachStepsFromRecipe(getBaseContext(), recipeItem);
        }
    }


}


