package com.example.isabe.bakingapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isabe.bakingapp.adapters.IngredientsAdapter;
import com.example.isabe.bakingapp.adapters.RecipeStepsAdapter;
import com.example.isabe.bakingapp.adapters.RecyclerTouchListener;
import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.objects.Ingredient;
import com.example.isabe.bakingapp.objects.RecipeContent;
import com.example.isabe.bakingapp.widget.RecipeWidgetProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.isabe.bakingapp.RecipeListFragment.RECIPE_SELECTION;
import static com.example.isabe.bakingapp.adapters.IngredientsAdapter.formatIngredient;

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
    public static final String STEP_INDEX = "index";

    private RecipeStepsAdapter recipeStepsAdapter;
    // --Commented out by Inspection (6/5/2018 11:17 PM):private RecipeListAdapter mRecipeAdapter;
    private static List<BakingStep> mBakingSteps = new ArrayList<>();
    private List<Ingredient> mIngredients = new ArrayList<>();
    private Ingredient mIngredItem;

    private RecyclerTouchListener onStepClickListener;
    @BindView(R.id.ingreds_details)
    TextView tvRecipeIngreds;

    @BindView(R.id.recipe_steps_list_rv)
    RecyclerView mRecipeStepsRecyclerView;
    private LinearLayoutManager layoutManager;
    @BindBool(R.bool.two_pane_mode)
    boolean mTwoPane;
    // --Commented out by Inspection STOP (6/5/2018 11:17 PM)
    private int mCurrCheckedPosition = 0;
    private BakingStep thisRecipeStep;
    @BindString(R.string.recipe_ingredients)
    String stIngredsHeader;
    private IngredientsAdapter mIngredientsAdapter;
    @BindBool(R.bool.isTablet)
    boolean tabletSize;

    private String stringIngreds;
    private RecipeContent recipeContent;

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STEP_INDEX, mCurrCheckedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recipeStepsAdapter = new RecipeStepsAdapter(getActivity(), mBakingSteps, onStepClickListener);

        //remove this code to test fragment initialization
        layoutManager = new LinearLayoutManager(getContext());
        mRecipeStepsRecyclerView.setLayoutManager(layoutManager);

        recipeStepsAdapter.setHasStableIds(true);
        mRecipeStepsRecyclerView.setHasFixedSize(true);
        mRecipeStepsRecyclerView.setAdapter(recipeStepsAdapter);

        View stepsPlayFrame = getActivity().findViewById(R.id.frame_recycler);
        mTwoPane = stepsPlayFrame != null && stepsPlayFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            mCurrCheckedPosition = savedInstanceState.getInt(STEP_INDEX, 0);

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            recipeContent = args.getParcelable(RECIPE_SELECTION);
            assert recipeContent != null;
            mBakingSteps = recipeContent.getBakingSteps();
            mIngredients = recipeContent.getIngredients();


        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(recipeContent.getRecipeName());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        unbinder = ButterKnife.bind(this, rootView);


        recipeStepsAdapter = new RecipeStepsAdapter(getActivity(), mBakingSteps, onStepClickListener);

        setUpRecyclerListener(mRecipeStepsRecyclerView);

        stringIngreds = prepareIngredsString(mIngredients);
        saveIngreds(recipeContent.getRecipeName(), stringIngreds);

        showIngredients(mIngredients);

        if (mBakingSteps != null && mBakingSteps.isEmpty()) {
            recipeStepsAdapter.addAll(mBakingSteps);
            Log.e(LOG_TAG, getString(R.string.load_finished_steps));

        }

        return rootView;
    }

    private void showIngredients(List<Ingredient> ingreds) {
        mIngredientsAdapter = new IngredientsAdapter(getActivity(), ingreds);
        mIngredientsAdapter.setHasStableIds(true);

        StringBuilder stringBuilder = new StringBuilder();
        stIngredsHeader = "<b>" + getString(R.string.ingredients) + "<b> ";
        //  stringBuilder.append(Html.fromHtml(stIngredsHeader));
        for (int iIngred = 0; iIngred < mIngredients.size(); iIngred++) {

            mIngredItem = ingreds.get(iIngred);

            String nameIngredient = mIngredItem.getIngredientName();
            float qty = mIngredItem.getQuantityIngredient();
            String uMeasure = mIngredItem.getUnit();
            stringBuilder.append("\n");
            stringBuilder.append(formatIngredient(getContext(), nameIngredient, qty, uMeasure));
        }
        tvRecipeIngreds.setText(stringBuilder.toString());

    }

    private String prepareIngredsString(List<Ingredient> ingredientsList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int iIngred = 0; iIngred < mIngredients.size(); iIngred++) {

            mIngredItem = ingredientsList.get(iIngred);

            String nameIngredient = mIngredItem.getIngredientName();
            float qty = mIngredItem.getQuantityIngredient();
            String uMeasure = mIngredItem.getUnit();
            stringBuilder.append("\n");
            stringBuilder.append(formatIngredient(getContext(), nameIngredient, qty, uMeasure));
        }
        return stringBuilder.toString().toLowerCase().trim();
    }

    private void saveIngreds(String recipeName, String stringIngreds) {
        Activity activity = getActivity();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                activity.getString(R.string.ingreds_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.ingreds_recipe_key), recipeName);
        showIngredients(mIngredients);

        editor.putString(getString(R.string.ingreds_key), stringIngreds);
        editor.commit();

        Intent intent = new Intent(activity, RecipeWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int appWidgetIds[] = AppWidgetManager.getInstance(activity.getApplication())
                .getAppWidgetIds(new ComponentName(activity.getApplication(),
                        RecipeWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        activity.sendBroadcast(intent);

        Log.i(LOG_TAG, getString(R.string.recipes_saved_shared));

    }

    private void setUpRecyclerListener(RecyclerView recyclerListener) {
        mRecipeStepsRecyclerView = recyclerListener;

        mRecipeStepsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecipeStepsRecyclerView,
                new RecyclerTouchListener.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //TODO
                        mCurrCheckedPosition = position;

                        thisRecipeStep = recipeStepsAdapter.getItem(position);
                        assert thisRecipeStep != null;
                        int thisRecipeId = thisRecipeStep.getId();
                        String thisStepShortDesc = thisRecipeStep.getBriefStepDescription();
                        String thisStepLongDesc = thisRecipeStep.getLongStepDescription();
                        String thisStepVideoUrl = thisRecipeStep.getVideoUrl();
                        String thisStepImageUrl = thisRecipeStep.getThumbnailStepUrl();

                        if (tabletSize) {
                            Fragment exoPlayerFragment = new StepsPlayFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(STEP_SELECTION, thisRecipeStep);
                            exoPlayerFragment.setArguments(bundle);
                            //showStepsPlayer(mCurrCheckedPosition);
                            getFragmentManager().beginTransaction()
                                    .add(R.id.frame_recycler, exoPlayerFragment)
                                    .commit();

                            if (mCurrCheckedPosition == 0) {
                                getFragmentManager().beginTransaction().replace(R.id.frame_recycler, exoPlayerFragment)
                                        .commit();
                            } else {
                                getFragmentManager().beginTransaction().replace(R.id.frame_recycler, exoPlayerFragment)
                                        .commit();
                            }

                        } else {
                            thisRecipeStep = recipeStepsAdapter.getItem(position);
                            assert thisRecipeStep != null;

                            Intent showStepsDetails = new Intent(getActivity().getBaseContext(),
                                    RecipeStepActivity.class);
                            showStepsDetails.putExtra(STEP_SELECTION, thisRecipeStep);
                            getActivity().startActivity(showStepsDetails);

                        }
                    }
                }));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static List<BakingStep> getListOfSteps() {
        return mBakingSteps;
    }

// --Commented out by Inspection START (6/5/2018 11:17 PM):
//    public List<Ingredient> getmIngredients() {
//        return mIngredients;
//    }
// --Commented out by Inspection STOP (6/5/2018 11:17 PM)

}



