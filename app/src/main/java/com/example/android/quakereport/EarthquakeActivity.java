/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<word>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private wordAdapter mAdapter;

    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "Test : Earthquake Activity onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);



        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        earthquakeListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new wordAdapter(this, new ArrayList<word>());

        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                word curentEarthquake = (word) mAdapter.getItem(position);

                Uri earthquakeUri = Uri.parse(curentEarthquake.getmUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);

            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();


            Log.i(LOG_TAG, "TEST : calling initLoader()...");

            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        }else {

            View loadingIndicator = findViewById(R.id.loading_spinner);

            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet);
        }


    }

    @Override
    public Loader<List<word>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<word>> loader, List<word> result) {

        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_earthquake);
        //Clear the adapter of previous earthquake data.
        mAdapter.clear();
        // If there is a valid list of {@link word}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (result != null && !result.isEmpty()) {
            mAdapter.addAll(result);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<word>> loader) {
        mAdapter.clear();
    }
}




