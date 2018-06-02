package com.example.isabe.bakingapp.widget;

import android.content.Context;

import com.example.isabe.bakingapp.adapters.RecipeListAdapter;
import com.example.isabe.bakingapp.adapters.RecyclerTouchListener;
import com.example.isabe.bakingapp.objects.Ingredient;
import com.example.isabe.bakingapp.objects.RecipeContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by isabe on 6/2/2018.
 */

public class WidgetHelper {

    private static PreferenceHelper preferenceHelper;
    private List<RecipeContent> recipeContentList = new ArrayList<>();
    private Ingredient ingredient;
    private static RecipeContent recipeItem;
    private static List<Ingredient> ingredientList = new ArrayList<>();
    private static RecipeListAdapter mRecipeAdapter;
    private static Context mContext;
    private static RecyclerTouchListener.ItemClickListener itemClickListener;

    public WidgetHelper(PreferenceHelper preferenceHelper) {
        WidgetHelper.preferenceHelper = preferenceHelper;
    }


    public static PreferenceHelper getPreferenceHelper(){
        return preferenceHelper;
    }

    Set<String> getPrefsRecipeList(){
        return getPreferenceHelper().getRecipeNames();
    }

    public void storeRecipeInPrefs(int appWidgetId, String text){
        getPreferenceHelper().getSelectedRecipe(appWidgetId);
    }

    public static List<Ingredient> getIngredsForRecipe(String recipeName){
        recipeName = recipeItem.getRecipeName();
        ingredientList = recipeItem.getIngredients();
        return ingredientList;
    }

    public static void createAdapter(Context context){
        mContext = context;
        mRecipeAdapter = new RecipeListAdapter(context, (List<RecipeContent>) recipeItem, itemClickListener);

    }

    public static String getSelectedRecipePrefs(int appWidgetId){
        createAdapter(mContext);
        RecipeContent thisRecipe = mRecipeAdapter.getItem(appWidgetId);
        int id = thisRecipe.getId();
        id = appWidgetId;
        return getPreferenceHelper().getSelectedRecipe(appWidgetId);
    }

}
