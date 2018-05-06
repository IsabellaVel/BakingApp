package com.example.isabe.bakingapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.isabe.bakingapp.objects.RecipeContent;
import com.example.isabe.bakingapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.util.List;

/**
 * Created by isabe on 5/6/2018.
 */

public class RecipeLoader extends AsyncTaskLoader<List<RecipeContent>> {
    private final String mUrl;

    public RecipeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<RecipeContent> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<RecipeContent> recipeContentList = null;

        try {
            recipeContentList = NetworkUtils.fetchRecipeData(mUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipeContentList;
    }
}
