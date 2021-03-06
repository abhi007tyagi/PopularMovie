package com.tyagiabhinav.popularmovie;

import android.app.Application;

/**
 * Created by abhinavtyagi on 05/02/16.
 */
public class PopularMovie extends Application {

    private static PopularMovie mApp;
    private boolean isInitialized;

    private int mPosition = 0;

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

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
