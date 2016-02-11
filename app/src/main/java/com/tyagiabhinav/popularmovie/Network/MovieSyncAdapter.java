package com.tyagiabhinav.popularmovie.Network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.tyagiabhinav.popularmovie.BuildConfig;
import com.tyagiabhinav.popularmovie.R;
import com.tyagiabhinav.popularmovie.Util.Parser;
import com.tyagiabhinav.popularmovie.Util.PreferenceHelper;

/**
 * Created by abhinavtyagi on 11/02/16.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    private ContentResolver mContentResolver;

    private static final String HOST_URL = "http://api.themoviedb.org/3";
    private static final String API_KEY = "api_key";
    private static final String DISCOVER_SERVICE = "discover/movie";
    private static final String TRAILER_SERVICE = "videos";
    private static final String REVIEW_SERVICE = "reviews";
    private static final String MOVIE = "movie";
    private static final String SORT = "sort_by";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String POPULARITY_ASEC = "vote_average.desc";

    public static final int DISCOVER_MOVIES_SERVICE_DESC = 1;
    public static final int DISCOVER_MOVIES_SERVICE_HIGH = 2;
    public static final int MOVIES_TRAILER_SERVICE = 3;
    public static final int MOVIES_REVIEW_SERVICE = 4;

    // Instance fields
    private String movieId;
    private int serviceType;

    /**
     * Set up the sync adapter
     */
    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        this.mContentResolver = context.getContentResolver();
        this.movieId = PreferenceHelper.getMovieId();
        this.serviceType = PreferenceHelper.getServiceType();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public MovieSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        this.mContentResolver = context.getContentResolver();
        this.movieId = PreferenceHelper.getMovieId();
        this.serviceType = PreferenceHelper.getServiceType();
    }

    /**
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync");
        String response = "";
        Uri uri;
        try {
            switch (serviceType) {
                case DISCOVER_MOVIES_SERVICE_DESC:
                    uri = Uri.parse(HOST_URL).buildUpon()
                            .appendEncodedPath(DISCOVER_SERVICE)
                            .appendQueryParameter(SORT, POPULARITY_DESC)
                            .appendQueryParameter(API_KEY, BuildConfig.TMDB_KEY)
                            .build();
                    Log.d(LOG_TAG, "URL Generated -->" + uri.toString());
                    response = HttpRequest.postData(uri.toString(), null, HttpRequest.REQUEST_TYPE_GET);
                    Parser.parseMovieList(response);
                    break;
                case DISCOVER_MOVIES_SERVICE_HIGH:
                    uri = Uri.parse(HOST_URL).buildUpon()
                            .appendEncodedPath(DISCOVER_SERVICE)
                            .appendQueryParameter(SORT, POPULARITY_ASEC)
                            .appendQueryParameter(API_KEY, BuildConfig.TMDB_KEY)
                            .build();
                    Log.d(LOG_TAG, "URL Generated -->" + uri.toString());
                    response = HttpRequest.postData(uri.toString(), null, HttpRequest.REQUEST_TYPE_GET);
                    Parser.parseMovieList(response);
                    break;
                case MOVIES_TRAILER_SERVICE:
                    uri = Uri.parse(HOST_URL).buildUpon()
                            .appendEncodedPath(MOVIE)
                            .appendEncodedPath(this.movieId)
                            .appendEncodedPath(TRAILER_SERVICE)
                            .appendQueryParameter(API_KEY, BuildConfig.TMDB_KEY)
                            .build();
                    Log.d(LOG_TAG, "URL Generated -->" + uri.toString());
                    response = HttpRequest.postData(uri.toString(), null, HttpRequest.REQUEST_TYPE_GET);
                    Parser.parseTrailer(response);
                    break; // commented to run at same time
                case MOVIES_REVIEW_SERVICE:
                    uri = Uri.parse(HOST_URL).buildUpon()
                            .appendEncodedPath(MOVIE)
                            .appendEncodedPath(this.movieId)
                            .appendEncodedPath(REVIEW_SERVICE)
                            .appendQueryParameter(API_KEY, BuildConfig.TMDB_KEY)
                            .build();
                    Log.d(LOG_TAG, "URL Generated -->" + uri.toString());
                    response = HttpRequest.postData(uri.toString(), null, HttpRequest.REQUEST_TYPE_GET);
                    Parser.parseReviews(response);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
//        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
//        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
//        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
//        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

}