package com.example.isabe.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.isabe.bakingapp.objects.BakingStep;

/**
 * Created by isabe on 5/13/2018.
 */

public class RecipeStepActivity extends AppCompatActivity {
    public static final String RECIPE_ID = "RECIPE_ID";
    private static final int DEFAULT_ID = 1;
    public static final String STEP_ID = "STEP_ID";
    private static final int DEFAULT_STEP_ID = 0;
    public BakingStep mBakingStep;
    int stepId;

    public static Intent createIntent(Context context, int recipeId, int stepId) {
        Intent intent = new Intent(context, RecipeStepActivity.class);
        intent.putExtra(RECIPE_ID, recipeId);
        intent.putExtra(STEP_ID, stepId);
        return intent;
    }

    public void attachSteps(Context context, Parcelable object) {
        context = getBaseContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeDetailFragment.STEP_SELECTION, mBakingStep);
        StepsPlayFragment playFragment = new StepsPlayFragment();
        playFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.stepFragmentContainer, playFragment)
                .commit();
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
        StepsPlayFragment stepsPlayFragment =
                (StepsPlayFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.stepFragmentContainer);

        if (savedInstanceState != null) {
            mBakingStep = savedInstanceState.getParcelable(RecipeDetailFragment.STEP_SELECTION);
        }
        mBakingStep = getIntent().getParcelableExtra(RecipeDetailFragment.STEP_SELECTION);
        int stepParcelId = mBakingStep.getId();
        String stepShortName = mBakingStep.getBriefStepDescription();
        String stepLongDesc = mBakingStep.getLongStepDescription();
        String stepVideoUrl = mBakingStep.getVideoUrl();
        String stepImageUrl = mBakingStep.getThumbnailStepUrl();

        stepParcelId = getIntent().getIntExtra(STEP_ID, stepId);

        if (savedInstanceState == null) {
        attachSteps(getBaseContext(), mBakingStep);
        }
    }
}


