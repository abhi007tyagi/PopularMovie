package com.tyagiabhinav.popularmovie.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tyagiabhinav.popularmovie.DB.MovieContract.MovieEntry;
import com.tyagiabhinav.popularmovie.DB.MovieContract.FavMovieEntry;

/**
 * Created by abhinavtyagi on 08/02/16.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "popularmovie.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold movies details.
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COL_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MovieEntry.COL_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COL_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COL_PLOT + " TEXT NOT NULL, " +
                MovieEntry.COL_DURATION + " TEXT, " +
                MovieEntry.COL_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COL_REVIEW + " TEXT, " +
                MovieEntry.COL_TRAILER_PATH + " TEXT, " +
                MovieEntry.COL_USER_RATING + " REAL NOT NULL, " +
                MovieEntry.COL_POPULARITY + " REAL NOT NULL, " +
                MovieEntry.COL_USER_VOTE + " INTEGER NOT NULL, " +
                MovieEntry.COL_FAVOURITE + " INTEGER" +
                " );";

        final String SQL_CREATE_FAV_MOVIE_TABLE = "CREATE TABLE " + FavMovieEntry.TABLE_NAME + " (" +
                FavMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                // the ID of the movie entry associated with selected fav movie
                FavMovieEntry.COL_MOVIE_ID + " TEXT UNIQUE NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAV_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}