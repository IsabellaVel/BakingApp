package com.example.isabe.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by isabe on 5/13/2018.
 */

public class RecipeStepActivity extends AppCompatActivity {
    public static final String RECIPE_ID = "RECIPE_ID";
    private static final int DEFAULT_ID = 1;
    public static final String STEP_ID = "STEP_ID";
    private static final int DEFAULT_STEP_ID = 0;

    public static Intent createIntent(Context context, int recipeId, int stepId) {
        Intent intent = new Intent(context, RecipeStepActivity.class);
        intent.putExtra(RECIPE_ID, recipeId);
        intent.putExtra(STEP_ID, stepId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step);

        int recipeId = getIntent().getIntExtra(RECIPE_ID, DEFAULT_ID);
        int stepId = getIntent().getIntExtra(STEP_ID, DEFAULT_STEP_ID);

        RecipeStepsExoPlayFragment recipeStepsExoPlayFragment =
                (RecipeStepsExoPlayFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.stepFragmentContainer);

    }
}


