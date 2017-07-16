package com.example.android.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {
    private List<News> news = new ArrayList<>();
    private NewsAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView newslist;
    private TextView emptyStateView;
    private LoaderManager loaderManager;
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int NEWS_LOADER_ID = 1;
    private static final String NEWS_URL = "https://content.guardianapis.com/search?format=json&from-date=2010-01-01&show-fields=headline,thumbnail&order-by=newest&show-tags=contributor&api-key=f030f5fb-e61a-4642-96af-1e4a5bebe89a";

    @Override
    public void onRefresh() {
        if (isConnected()) {
            loaderManager = getLoaderManager();
            Log.i(LOG_TAG, "test: calling initLoader()");
            loaderManager.restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            emptyStateView.setText(getString(R.string.nointernet));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newslist = (ListView) findViewById(R.id.listview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        emptyStateView = (TextView) findViewById(R.id.empty);
        newslist.setEmptyView(emptyStateView);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newslist.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        if (isConnected()) {
                                            loaderManager = getLoaderManager();
                                            Log.i(LOG_TAG, "test: calling initLoader()");
                                            loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
                                        } else {
                                            swipeRefreshLayout.setRefreshing(false);
                                            emptyStateView.setText(getString(R.string.nointernet));
                                        }

                                    }
                                }
        );

        newslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentnews = mAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentnews.getMurl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        swipeRefreshLayout.setRefreshing(true);
        return new newsLoader(MainActivity.this, NEWS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        swipeRefreshLayout.setRefreshing(false);
        mAdapter.clear();
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }
}
