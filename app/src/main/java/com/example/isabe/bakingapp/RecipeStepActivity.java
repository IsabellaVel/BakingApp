package com.example.isabe.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.objects.RecipeContent;

import java.util.List;

import static com.example.isabe.bakingapp.RecipeDetailFragment.STEP_INDEX;
import static com.example.isabe.bakingapp.RecipeDetailFragment.STEP_SELECTION;

/**
 * Created by isabe on 5/13/2018.
 */

public class RecipeStepActivity extends AppCompatActivity {
    public static final String RECIPE_ID = "RECIPE_ID";
    private static final int DEFAULT_ID = 1;
    public BakingStep mStepItem;
    private RecipeContent recipeItem;
    public static List<BakingStep> thisRecipeSteps;
    int stepId;

    public StepsPlayFragment attachSteps(Context context, Parcelable step) {
        context = getBaseContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEP_SELECTION, mStepItem);
        StepsPlayFragment playFragment = new StepsPlayFragment();
        playFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.stepFragmentContainer, playFragment)
                .commit();
        return playFragment;
    }

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
        mStepItem = getIntent().getParcelableExtra(STEP_SELECTION);
        int stepParcelId = mStepItem.getId();
        String stepShortName = mStepItem.getBriefStepDescription();
        String stepLongDesc = mStepItem.getLongStepDescription();
        String stepVideoUrl = mStepItem.getVideoUrl();
        String stepImageUrl = mStepItem.getThumbnailStepUrl();


        thisRecipeSteps = RecipeDetailFragment.getListOfSteps();

        if (savedInstanceState != null) {
            mStepItem = getIntent().getParcelableExtra(STEP_SELECTION);
            stepParcelId = getIntent().getIntExtra(STEP_INDEX, stepId);
            StepsPlayFragment.newInstance(stepId);
        }


        //add parcelable for steps of recipe
        recipeItem = getIntent().getParcelableExtra(RecipeListFragment.RECIPE_SELECTION);
    }


}


