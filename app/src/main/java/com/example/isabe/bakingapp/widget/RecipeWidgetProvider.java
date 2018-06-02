package com.example.isabe.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.isabe.bakingapp.R;
import com.example.isabe.bakingapp.RecipeListActivity;
import com.example.isabe.bakingapp.adapters.IngredientsAdapter;
import com.example.isabe.bakingapp.objects.Ingredient;

import java.util.List;

/**
 * Created by isabe on 6/2/2018.
 */

public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        Intent intent = new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_recipe_image, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            String recipeName = WidgetHelper.getSelectedRecipePrefs(appWidgetId);

            List<Ingredient> ingredientList = WidgetHelper.getIngredsForRecipe(recipeName);
            updateAppWidgetViews(context, appWidgetManager, appWidgetId, recipeName, ingredientList);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidgetViews(Context context, AppWidgetManager appWidgetManager,
                                            int appWidgetId, String recipeName, List<Ingredient> ingredientList) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_list);
        remoteViews.setTextViewText(R.id.widget_recipe_text, recipeName);
        remoteViews.removeAllViews(R.id.widget_ingreds_list);

        for (Ingredient ingredient : ingredientList) {
            RemoteViews ingredsView = new RemoteViews(context.getPackageName(),
                    R.layout.widget_ingredient_item);

            String item = IngredientsAdapter.formatIngredient(
                    context, ingredient.getIngredientName(), ingredient.getQuantityIngredient(),
                    ingredient.getUnit());

            ingredsView.setTextViewText(R.id.widget_ingred_item, item);
            remoteViews.addView(R.id.widget_ingreds_list, ingredsView);

        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}