package com.example.isabe.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {
    private static final int DEFAULT_VALUE = 1;
    public static String RECIPE_ID_EXTRA_FIELD = "recipe_id";
    int clickedId;
    boolean mTwoPane;
    @BindView(R.id.ingreds_details)
    TextView mTvIngreds;

    public static Intent sendData(Context context, int recipeId){
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_ID_EXTRA_FIELD, recipeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
         // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            int recipeId = getIntent().getIntExtra(RECIPE_ID_EXTRA_FIELD, DEFAULT_VALUE);

            RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(recipeId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_details_container, recipeDetailFragment)
                    .commit();


        // TODO check if this would be the proper view from the layout. Otherwise, create a separate layout file.
        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            recipeDetailFragment = new RecipeDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_details_container, recipeDetailFragment)
                    .commit();

            // TODO Add fragment for Video and steps instructions
           RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
           getSupportFragmentManager().beginTransaction()
                   .add(R.id.frame_recycler, recipeStepsFragment)
                   .commit();

        }

    }
    @OnClick(R.id.ingreds_details)
    public void showDetails(View view) {
    }

    /**@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    **/
}
