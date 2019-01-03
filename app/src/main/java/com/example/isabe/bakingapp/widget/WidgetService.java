package com.example.isabe.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.isabe.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isabe on 6/3/2018.
 */

public class WidgetService extends RemoteViewsService {
    private static final String LOG_TAG = WidgetService.class.getSimpleName();
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this.getApplicationContext(), intent);
    }

    public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
        private final Context mContext;
        private final int mAppWidgetId;
        final List<String> ingredientList = new ArrayList<>();

        public WidgetDataProvider(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
           // initData();
        }

// --Commented out by Inspection START (6/5/2018 11:17 PM):
//        private void initData() {
//            ingredientList.clear();
//            for (int i = 1; i <= ingredientList.size(); i++) {
//                ingredientList.add("ListView item " + i);
//            }
//        }
// --Commented out by Inspection STOP (6/5/2018 11:17 PM)

        @Override
        public void onDataSetChanged() {
            //initData();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_ingred_item);
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                    mContext.getString(R.string.ingreds_file_key), Context.MODE_PRIVATE);
            String ingredString = sharedPreferences.getString
                    (mContext.getString(R.string.ingreds_key), getString(R.string.def_string));
            remoteViews.setTextViewText(R.id.widget_ingred_item, ingredString);
            Log.i(LOG_TAG, getString(R.string.ingreds_widget));

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
