package com.example.isabe.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.objects.RecipeContent;

import java.util.List;

import butterknife.BindBool;

import static com.example.isabe.bakingapp.RecipeDetailFragment.STEP_SELECTION;

/**
 * Created by isabe on 5/13/2018.
 */

public class RecipeStepActivity extends AppCompatActivity {
    public static final String RECIPE_ID = "RECIPE_ID";
    private static final int DEFAULT_RECIPE_ID = 0;
    private static final String STEP_ID = "STEP_ID";
    private static final int DEFAULT_STEP_ID = 0;
    public BakingStep mStepItem;
    private RecipeContent recipeItem;
    public static List<BakingStep> thisRecipeSteps;
    int stepId;
    int recipeId;
    @BindBool(R.bool.isTablet)
    boolean tabletSize;

    public static Intent createIntent(Context context, int recipeId, int stepId) {
        Intent intent = new Intent(context, RecipeStepActivity.class);
        intent.putExtra(RECIPE_ID, recipeId);
        intent.putExtra(STEP_ID, stepId);
        return intent;
    }

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

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /**    if (getResources().getConfiguration().orientation
         == Configuration.ORIENTATION_LANDSCAPE) {
         finish();
         return;
         }
         **/
        mStepItem = getIntent().getParcelableExtra(STEP_SELECTION);

        int stepParcelId = mStepItem.getId();
        String stepShortName = mStepItem.getBriefStepDescription();
        String stepLongDesc = mStepItem.getLongStepDescription();
        String stepVideoUrl = mStepItem.getVideoUrl();
        String stepImageUrl = mStepItem.getThumbnailStepUrl();


        thisRecipeSteps = RecipeDetailFragment.getListOfSteps();

        if (savedInstanceState == null) {
            attachSteps(getBaseContext(), mStepItem);
        }

        if (savedInstanceState != null) {
            mStepItem = getIntent().getParcelableExtra(STEP_SELECTION);
            stepParcelId = getIntent().getIntExtra(STEP_ID, DEFAULT_STEP_ID);
            //  StepsPlayFragment.newInstance(stepParcelId);
        }


        //add parcelable for steps of recipe
        recipeItem = getIntent().getParcelableExtra(RecipeListFragment.RECIPE_SELECTION);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeAsUp:
                mStepItem = getIntent().getParcelableExtra(STEP_SELECTION);
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}





