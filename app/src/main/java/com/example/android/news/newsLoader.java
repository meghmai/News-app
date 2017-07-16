package com.example.android.news;


import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class newsLoader extends AsyncTaskLoader<List<News>> {
    private static final String LOG_TAG = newsLoader.class.getName();
    private String mUrl;

    public newsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<News> news = QueryUtils.fetchNewsData(mUrl);
        return news;
    }
}

