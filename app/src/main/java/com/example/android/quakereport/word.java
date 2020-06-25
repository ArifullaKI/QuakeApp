package com.example.android.quakereport;

public class word {

    private String mPlace;
    private double mMag;
    private long mDate;
    private String mUrl;




    public word(double mag,String place, long timeInMilliSec,String url){
        mMag = mag;
        mPlace = place;
        mDate = timeInMilliSec;
        mUrl = url;

    }



    public double getmMag(){
        return mMag;
    }

    public String getmPlace(){
        return mPlace;
    }

    public long getmDate(){
        return mDate;
    }

    public String getmUrl(){return mUrl;}

}
