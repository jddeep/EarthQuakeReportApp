package com.example.android.quakereport;


import android.annotation.SuppressLint;
import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils extends AppCompatActivity {

    /** Sample JSON response for a USGS query */
    private static final String url = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10";
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Quake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Quake> extractEarthquakesJSON(String Url) throws IOException {
        if (TextUtils.isEmpty(Url)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Quake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject response = new JSONObject(Url);
            JSONArray features = response.getJSONArray("features");
            int f=0;
            String part1 = "";
            String part2 = "";
            final String location_seperator = "of ";
            for(int i=0;i<features.length();i++) {

                JSONObject currentearthquake = features.getJSONObject(i);
                JSONObject properties = currentearthquake.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String location = properties.getString("place");
                if(location.contains(location_seperator)) {
                    String[] parts = location.split(location_seperator);
                    part1 = parts[f]+location_seperator;
                    part2 = parts[f + 1];
                }else {
                    part1="Near Of";
                    part2 = location;
                }
                long time = properties.getLong("time");
                String url = properties.getString("url");

                Quake earthquake = new Quake(mag,part1,part2,time,url);
                earthquakes.add(earthquake);
                f=0;
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
    /**
     * Query the USGS dataset and return a list of {@link Quake} objects.
     */
    public static List<Quake> fetchEarthquakeData(String requestUrl) throws IOException {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Main Activity", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Quake> earthquakes = extractEarthquakesJSON(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }
    private static URL createUrl(String Url){
        URL url = null;
        try{
            url = new URL(Url);
        } catch (MalformedURLException e) {
            Log.e("Main Activity","Error in creating url");
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Main Activity", "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


}