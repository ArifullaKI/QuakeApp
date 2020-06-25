package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.quakereport.EarthquakeActivity;
import com.example.android.quakereport.QueryUtils;
import com.example.android.quakereport.word;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<word>> {

    private String mUrl;
    public static final String LOG_TAG = EarthquakeLoader.class.getName();

 public EarthquakeLoader(Context context,String url){
     super(context);
    mUrl = url;
 }

 @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"TEST : onStartLoading() called...");
        forceLoad();
    }

    @Override
    public List<word> loadInBackground() {
    Log.i(LOG_TAG,"TEST:loadInBackground() called..");

    if(mUrl == null) {
        return null;
    }

        List<word> result = QueryUtils.fetchEarthquakeData( mUrl);
        return result;
    }
}
