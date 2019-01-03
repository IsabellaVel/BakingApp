package com.example.isabe.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.isabe.bakingapp.objects.RecipeContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//import com.example.isabe.bakingapp.adapters.RecipeListAdapterDefaultGenerated;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

  private boolean mTwoPane;
private List<RecipeContent> mRecipesList = new ArrayList<>();

    @BindView(R.id.recipes_list_rv)
    RecyclerView mRecipesRv;
// --Commented out by Inspection STOP (6/5/2018 11:17 PM)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        RecipeListFragment recipeListFragment = (RecipeListFragment) getSupportFragmentManager().
                findFragmentById(R.id.frameLayout);

        if (recipeListFragment == null) {
            recipeListFragment = RecipeListFragment.newInstance();

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.frameLayout, recipeListFragment)
                    .commit();
        }
    }
}
