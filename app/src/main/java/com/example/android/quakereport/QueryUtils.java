package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = "USGS Query Utils error";


        /**  Creating function for the Doin backgroung class and to connect, retrive info from
         the url
         * @return*/

        public static List<word> fetchEarthquakeData(String requestUrl) {
             Log.i(LOG_TAG,"TEST: fetchEarthquakeData() called...");


            //Create URL Object
            URL url = createUrl(requestUrl);
            String JsonResponse = null;
            try {
                JsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error in fetchquakeData fun", e);
            }

            List<word> earthquake = extractEarthquakes(JsonResponse);
            return earthquake;
        }

        private static URL createUrl(String requestUrl){
            URL url = null;
            try{
                url = new URL(requestUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG,"Error in creatURl fun",e);
            }
            return url;
        }

        private static String makeHttpRequest(URL url) throws IOException{
            String JsonResponse= " ";
            if(url==null){
                return JsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream =null;
            try{
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if(urlConnection.getResponseCode()==200){
                    inputStream = urlConnection.getInputStream();
                            JsonResponse = readFromStream(inputStream);
                }else{
                    Log.e(LOG_TAG,"Error url connection status" + urlConnection.getResponseCode());
                }
            }catch (IOException e){
                Log.e(LOG_TAG,"Problem Retriving the earthquake JSON results",e);
            }finally {
                if(urlConnection != null);
                {
                    urlConnection.disconnect();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            } return JsonResponse;
        }
        private static  String readFromStream(InputStream inputStream) throws IOException{
            StringBuilder output = new StringBuilder();
            if(inputStream!= null){

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while(line!= null){
                    output.append(line);
                    line =  reader.readLine();
                }
            } return output.toString();
        }


        /**
         * Return a list of {@link word} objects that has been built up from
         * parsing a JSON response.
         * @return
         */
        public static List<word> extractEarthquakes(String JsonResponse) {
            if(TextUtils.isEmpty(JsonResponse)){
                return null;
            }

            // Create an empty ArrayList that we can start adding earthquakes to
            List<word> earthquakes = new ArrayList<>();

            // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try {
                    JSONObject baseJsonResponse = new JSONObject(JsonResponse);
                JSONArray JasonResArray = baseJsonResponse.getJSONArray("features");
                for (int i=0;i<JasonResArray.length();i++) {
                    JSONObject featuresObj = JasonResArray.getJSONObject(i);
                    JSONObject propertiesObj = featuresObj.getJSONObject("properties");
                    double magnitude = propertiesObj.getDouble("mag");
                    String place = propertiesObj.getString("place");
                    long time = propertiesObj.getLong("time");
                    String URL = propertiesObj.getString("url");

                    word earthquake = new word (magnitude,place,time,URL);
                    earthquakes.add(earthquake);


                }

                // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
                // build up a list of Earthquake objects with the corresponding data.

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }

            // Return the list of earthquakes
            return earthquakes;
        }


    }

