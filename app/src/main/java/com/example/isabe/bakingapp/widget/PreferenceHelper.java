package com.example.isabe.bakingapp.widget;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.isabe.bakingapp.objects.RecipeContent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by isabe on 6/2/2018.
 */

public class PreferenceHelper {

    private final SharedPreferences sharedPreferences;
    private List<RecipeContent> listOfRecipes;
    private static final String PREFS = "shared_prefs";
    private static final String RECIPES = "recipes_list";

    public PreferenceHelper(Context context){
        sharedPreferences = (SharedPreferences) context.getSharedPreferences(
                PREFS, Context.MODE_PRIVATE);
           }

    public void chooseRecipeFromList(List<RecipeContent> recipeList){
        Set<String> recipe = new HashSet<>();
        for (RecipeContent recipeContent : recipeList){
            recipe.add(recipeContent.getRecipeName());
        }
        sharedPreferences.edit().putStringSet(RECIPES, recipe).apply();
    }

    public Set<String> getRecipeNames(){
        return sharedPreferences.getStringSet(RECIPES, new HashSet<String>(0));
    }

    public String getSelectedRecipe(int id){
        return sharedPreferences.getString("selected_recipe" + id, "");
    }

    public void storeSelectedRecipe(int id, String text){
        sharedPreferences.edit().putString("selected_recipe" + id, text).apply();
    }
}
