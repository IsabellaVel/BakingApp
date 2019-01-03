package com.example.isabe.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.isabe.bakingapp.R;
import com.example.isabe.bakingapp.RecipeListActivity;

/**
 * Created by isabe on 6/2/2018.
 */

public class RecipeWidgetProvider extends AppWidgetProvider {
    private static final String LOG_TAG = RecipeWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            String defaultString = context.getString(R.string.nutella_text);

            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    context.getString(R.string.ingreds_file_key), Context.MODE_PRIVATE);
            String recipeName = sharedPreferences.getString(context.getString(R.string.ingreds_recipe_key),
                    defaultString);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            Intent widgetServiceIntent = new Intent(context, WidgetService.class);
            widgetServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            widgetServiceIntent.setData(Uri.parse(widgetServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            remoteViews.setRemoteAdapter(R.id.widget_ingreds_list, widgetServiceIntent);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                    R.id.widget_ingreds_list);

            remoteViews.setTextViewText(R.id.widget_recipe_text, recipeName);

            Intent openWidget = new Intent(context, RecipeListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    openWidget, 0);

            remoteViews.setOnClickPendingIntent(R.id.widget_recipe_text, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }


}