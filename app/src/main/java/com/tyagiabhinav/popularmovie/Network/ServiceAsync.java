package com.tyagiabhinav.popularmovie.Network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.tyagiabhinav.popularmovie.BuildConfig;

/**
 * Created by abhinavtyagi on 05/02/16.
 */
public class ServiceAsync extends AsyncTask<Void, Void, String> {
    private static final String LOG_TAG = ServiceAsync.class.getSimpleName();
    private final int serviceType;

    public interface ServiceAsyncResponse {
        void processFinish(String response);
    }

    private Context context;
    private ServiceAsync.ServiceAsyncResponse delegate;


    private static final String HOST_URL = "http://api.themoviedb.org";
    private static final String API_KEY = "api_key";
    private static final String DISCOVER_SERVICE = "http://api.themoviedb.org/3/discover/movie?";
    private static final String SORT = "sort_by";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String POPULARITY_ASEC = "vote_average.desc";

    public static final int DISCOVER_MOVIES_SERVICE_DESC = 1;
    public static final int DISCOVER_MOVIES_SERVICE_HIGH = 2;

    public ServiceAsync( Context ctx, Object obj, int serviceType) {
        this.context = ctx;
        this.serviceType = serviceType;
        if (obj instanceof ServiceAsync.ServiceAsyncResponse) {
            this.delegate = (ServiceAsync.ServiceAsyncResponse) obj;
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        String response = "";
        Uri uri;
        try {
            switch (serviceType){
                case DISCOVER_MOVIES_SERVICE_DESC:
                    uri = Uri.parse(DISCOVER_SERVICE).buildUpon()
                            .appendQueryParameter(SORT, POPULARITY_DESC)
                            .appendQueryParameter(API_KEY, BuildConfig.TMDB_KEY)
                            .build();
                    Log.d(LOG_TAG,"URL Generated -->"+uri.toString());
                    response = HttpRequest.postData(uri.toString(), null, HttpRequest.REQUEST_TYPE_GET);
                    break;
                case DISCOVER_MOVIES_SERVICE_HIGH:
                    uri = Uri.parse(DISCOVER_SERVICE).buildUpon()
                            .appendQueryParameter(SORT, POPULARITY_ASEC)
                            .appendQueryParameter(API_KEY, BuildConfig.TMDB_KEY)
                            .build();
                    Log.d(LOG_TAG,"URL Generated -->"+uri.toString());
                    response = HttpRequest.postData(uri.toString(), null, HttpRequest.REQUEST_TYPE_GET);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        delegate.processFinish(response);
    }


}

