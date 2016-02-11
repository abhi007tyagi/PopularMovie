package com.tyagiabhinav.popularmovie.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abhinavtyagi on 11/02/16.
 */
public class PreferenceHelper {

    private static final String APP = "MOVIE_PREFS";
    private static final String MOVIE_ID = "movieId";
    private static final String SERVICE_TYPE = "serviceType";

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static Context context;

    /**
     * Initializes the Preference Helper class
     *
     * @param ctx
     */
    public synchronized static void init(Context ctx) {
        context = ctx;
        prefs = context.getSharedPreferences(APP, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /**
     * Save movieId
     *
     * @param movieId
     */
    public synchronized static void setMovieId(String movieId) {
        editor.putString(MOVIE_ID, movieId);
        editor.commit();
    }

    /**
     * Fetch saved movieId
     *
     * @return
     */
    public synchronized static String getMovieId() {
        return prefs.getString(MOVIE_ID, "");
    }

    /**
     * Save service type
     *
     * @param serviceType
     */
    public synchronized static void setServiceType(int serviceType) {
        editor.putInt(SERVICE_TYPE, serviceType);
        editor.commit();
    }

    /**
     * Fetch saved service type
     *
     * @return
     */
    public synchronized static int getServiceType() {
        return prefs.getInt(SERVICE_TYPE, -1);
    }

}
