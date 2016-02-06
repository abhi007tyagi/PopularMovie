package com.tyagiabhinav.popularmovie;

import android.app.Application;

/**
 * Created by abhinavtyagi on 05/02/16.
 */
public class PopularMovie extends Application {

    private static PopularMovie mApp;
    private boolean isInitialized;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!isInitialized) {
            init();
        }
    }

    /**
     * Initialize application
     */
    private void init() {
        mApp = this;
        isInitialized = true;
    }

    /**
     *
     * @return Application context
     */
    public static PopularMovie getAPPContext() {
        return mApp;
    }
}
