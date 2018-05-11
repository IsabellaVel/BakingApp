package com.example.isabe.bakingapp.utilities;

import android.text.TextUtils;
import android.util.Log;
import android.util.StateSet;

import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.objects.Ingredient;
import com.example.isabe.bakingapp.objects.RecipeContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Created by isabe on 5/5/2018.
 */

public class RecipeDbJSONUtils {
    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_STEPS = "steps";
    private static final String RECIPE_SERVINGS = "servings";
    private static final String RECIPE_IMAGE = "image";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_SHORT_DESCRIPTION = "shortDescription";
    private static final String RECIPE_DESCRIPTION = "description";
    private static final String RECIPE_VIDEO_URL = "videoURL";
    private static final String RECIPE_THUMBNAIL_URL = "thumbnailURL";

    private static final String LOG_TAG = RecipeDbJSONUtils.class.getSimpleName();

    public static List<RecipeContent> getRecipeDetailFromJSON(String recipeJSONString) {
        List<Ingredient> ingredientList = new ArrayList<>();
        List<BakingStep> bakingStepList = new ArrayList<>();

        if (TextUtils.isEmpty(recipeJSONString)) {
            return null;
        }

        ArrayList<RecipeContent> recipeJsonList = new ArrayList<>();

        try {
            JSONArray recipesJSONArray = new JSONArray(recipeJSONString);

            for (int i = 0; i < recipesJSONArray.length(); i++) {
                JSONObject currentRecipe = recipesJSONArray.getJSONObject(i);
                String id = currentRecipe.optString(RECIPE_ID);
                String name = currentRecipe.optString(RECIPE_NAME);
                String servings = currentRecipe.optString(RECIPE_SERVINGS);
                String image = currentRecipe.optString(RECIPE_IMAGE);

                JSONArray recipesIngreds = currentRecipe.getJSONArray(RECIPE_INGREDIENTS);
                ArrayList<Ingredient> ingredientsList = new ArrayList<>();

                for (int iIngr = 0; iIngr < recipesIngreds.length(); iIngr++) {
                    JSONObject currentIngreds = recipesIngreds.getJSONObject(iIngr);
                    int qty = currentIngreds.optInt(RECIPE_QUANTITY);
                    String measure = currentIngreds.optString(RECIPE_MEASURE);
                    String ingredient = currentIngreds.optString(RECIPE_INGREDIENT);

                    Ingredient ingredItem = new Ingredient(qty, measure, ingredient);
                    ingredientsList.add(ingredItem);

                }

                JSONArray recipesSteps = currentRecipe.getJSONArray(RECIPE_STEPS);
                ArrayList<BakingStep> bakingStepArrayList = new ArrayList<>();


                for (int iSteps = 0; iSteps < recipesSteps.length(); iSteps++) {
                    JSONObject currentSteps = recipesSteps.getJSONObject(iSteps);
                    int idStep = currentSteps.optInt(RECIPE_ID);
                    String shortDescr = currentSteps.optString(RECIPE_SHORT_DESCRIPTION);
                    String descr = currentSteps.optString(RECIPE_DESCRIPTION);
                    String videoURL = currentSteps.optString(RECIPE_VIDEO_URL);
                    String thumbnailURL = currentSteps.optString(RECIPE_THUMBNAIL_URL);

                    BakingStep stepItem = new BakingStep(idStep, shortDescr, descr, videoURL, thumbnailURL);
                    bakingStepArrayList.add(stepItem);
                }

                RecipeContent recipeItem = new RecipeContent
                        (id, name, ingredientsList, bakingStepArrayList, image);
                recipeJsonList.add(recipeItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem parsing Recipe JSON results.", e);
        }
        return recipeJsonList;
    }

    public static List<Ingredient> getIgredientsFromJSON(String recipeJSONString) {
        if (TextUtils.isEmpty(recipeJSONString)) {
            return null;
        }

        List<Ingredient> ingredientList = new ArrayList<Ingredient>();

        try {
            JSONArray recipesJSONArray = new JSONArray(recipeJSONString);
            ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();

            for (int i = 0; i < recipesJSONArray.length(); i++) {
                JSONObject currentRecipe = recipesJSONArray.getJSONObject(i);
                JSONArray recipesIngreds = currentRecipe.getJSONArray(RECIPE_INGREDIENTS);
                ArrayList<Ingredient> ingredientsList = new ArrayList<>();

                for (int iIngr = 0; iIngr < recipesIngreds.length(); iIngr++) {
                    JSONObject currentIngreds = recipesIngreds.getJSONObject(iIngr);
                    int qty = currentIngreds.optInt(RECIPE_QUANTITY);
                    String measure = currentIngreds.optString(RECIPE_MEASURE);
                    String ingredient = currentIngreds.optString(RECIPE_INGREDIENT);

                    Ingredient ingredInList = new Ingredient(qty, measure, ingredient);
                    ingredientsList.add(ingredInList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem parsing Ingredients JSON results.", e);
        }
        return ingredientList;
    }

    public static List<BakingStep> getStepFromJSON(String recipeJSONString) {
        if (TextUtils.isEmpty(recipeJSONString)) {
            return null;
        }

        List<BakingStep> bakingStepList = new ArrayList<BakingStep>();

        try {
            JSONArray recipesJSONArray = new JSONArray(recipeJSONString);

            for (int i = 0; i < recipesJSONArray.length(); i++) {
                JSONObject currentRecipe = recipesJSONArray.getJSONObject(i);

                JSONArray recipesSteps = currentRecipe.getJSONArray(RECIPE_STEPS);

                for (int iSteps = 0; iSteps < recipesSteps.length(); iSteps++) {
                    JSONObject currentSteps = recipesSteps.getJSONObject(iSteps);
                    int idStep = currentSteps.optInt(RECIPE_ID);
                    String shortDescr = currentSteps.optString(RECIPE_SHORT_DESCRIPTION);
                    String descr = currentSteps.optString(RECIPE_DESCRIPTION);
                    String videoURL = currentSteps.optString(RECIPE_VIDEO_URL);
                    String thumbnailURL = currentSteps.optString(RECIPE_THUMBNAIL_URL);

                    BakingStep stepInList = new BakingStep(idStep, shortDescr, descr, videoURL, thumbnailURL);
                    bakingStepList.add(stepInList);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem parsing Recipe JSON results.", e);
        }
        return bakingStepList;
    }

}