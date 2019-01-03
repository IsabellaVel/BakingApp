package com.example.isabe.bakingapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.isabe.bakingapp.objects.BakingStep;
import com.example.isabe.bakingapp.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isabe on 5/6/2018.
 */

public class StepsLoader extends AsyncTaskLoader<List<BakingStep>> {
        private final String mUrl;
        private List<BakingStep> bakingStepList = new ArrayList<>();

        public StepsLoader(Context context, String url) {
                super(context);
                mUrl = url;
        }

        protected void onStartLoading() {
                forceLoad();
        }

        @Override
        public List<BakingStep> loadInBackground() {
                if (mUrl == null) {
                        return null;
                }
                bakingStepList = NetworkUtils.fetchStepsData(mUrl);
                return bakingStepList;
        }
}