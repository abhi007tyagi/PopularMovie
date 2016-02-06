package com.tyagiabhinav.popularmovie.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.tyagiabhinav.popularmovie.PopularMovie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abhinavtyagi on 05/02/16.
 */
public class HttpRequest {

    private static final String LOG_TAG = HttpRequest.class.getSimpleName();
    public static final String REQUEST_TYPE_GET = "GET";
    public static final String REQUEST_TYPE_POST = "POST";

    /**
     *
     * @param serviceURL
     * @param data
     * @param requestType
     * @return
     */
    public static String postData(String serviceURL, String data, String requestType){

        Log.d(LOG_TAG, "URL-->"+serviceURL);
        String response = "";
        if(isNetworkAvailable()) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(serviceURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod(requestType);
                urlConnection.connect();

                if (data != null && !data.trim().isEmpty()) {
                    Log.d(LOG_TAG, "JSON Data-->" + data);

                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(data);
                    writer.flush();
                    writer.close();
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                Log.d(LOG_TAG, "RAW Response-->" + result.toString());
                response = new String(result.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream!", e);
                    }
                }
            }
        }
        return response;
    }

    /**
     *
     * @return if Network is available
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) PopularMovie.getAPPContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
