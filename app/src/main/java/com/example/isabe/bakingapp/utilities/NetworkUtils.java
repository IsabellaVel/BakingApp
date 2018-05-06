package com.example.isabe.bakingapp.utilities;

import android.media.MediaActionSound;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.objects.Ingredient;
import com.example.isabe.bakingapp.objects.RecipeContent;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by isabe on 5/5/2018.
 */

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private NetworkUtils() {

    }

    public static List<RecipeContent> fetchRecipeData(String requestUrl) throws JSONException {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<RecipeContent> recipeList = RecipeDbJSONUtils.getRecipeDetailFromJSON(jsonResponse);

        return recipeList;
    }

    public static List<BakingStep> fetchStepsData(String requestUrl) throws JSONException {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<BakingStep> bakingStepList = RecipeDbJSONUtils.getStepFromJSON(jsonResponse);

        return bakingStepList;
    }

    public static List<Ingredient> fetchIngredientData(String requestUrl) throws JSONException {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Ingredient> ingredientList = RecipeDbJSONUtils.getIgredientsFromJSON(jsonResponse);

        return ingredientList;
    }

    public static URL createUrl(String strignUrl) {
        URL url = null;
        try {
            url = new URL(strignUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error with creating URL., e");
        }
        Log.v(LOG_TAG, "Built URI " + url);
        return url;
    }

    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.getResponseCode();
        } catch (IOException e) {
            int responseCode = urlConnection.getResponseCode();
        }
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
