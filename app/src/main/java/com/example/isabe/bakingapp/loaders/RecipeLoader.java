package com.example.isabe.bakingapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.isabe.bakingapp.objects.RecipeContent;
import com.example.isabe.bakingapp.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by isabe on 5/6/2018.
 */

public class RecipeLoader extends AsyncTaskLoader<List<RecipeContent>> {
    private final String mUrl;
    private List<RecipeContent> recipeContentList;

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
        recipeContentList = NetworkUtils.fetchRecipeData(mUrl);
        return recipeContentList;
    }
}
