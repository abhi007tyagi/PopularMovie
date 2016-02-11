package com.tyagiabhinav.popularmovie.DB;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by abhinavtyagi on 08/02/16.
 */
public class MovieProvider extends ContentProvider {

    private static final String LOG_TAG = MovieProvider.class.getSimpleName();

    private MovieDBHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int MOVIE = 100;
    static final int MOVIE_COLUMN = 101;
    static final int MOVIE_TRAILER = 102;
    static final int FAV_MOVIE = 300;
    static final int MOVIE_WITH_FAVORITE = 301;

    private static final SQLiteQueryBuilder sFavMovieQueryBuilder;

    static{
        sFavMovieQueryBuilder = new SQLiteQueryBuilder();

        /// JOIN query for favorite movies...
        // SELECT * FROM movies INNER JOIN fav_movies ON movies.movies_id = fav_movies.movies_id;
        sFavMovieQueryBuilder.setTables(
                MovieContract.MovieEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.FavMovieEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COL_MOVIE_ID +
                        " = " + MovieContract.FavMovieEntry.TABLE_NAME +
                        "." + MovieContract.FavMovieEntry.COL_MOVIE_ID);
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_COLUMN);
//        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_TRAILER);
        matcher.addURI(authority, MovieContract.PATH_FAV_MOVIE, FAV_MOVIE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query -->"+uri);
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "movie/*"
            case MOVIE_COLUMN:
            {
                Log.d(LOG_TAG, "MOVIE_TRAILER");
                String movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        new String[]{movieId},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "fav_movie"
            case FAV_MOVIE: {
                Log.d(LOG_TAG, "FAVORITE_MOVIE");
                retCursor =  sFavMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            // "movie"
            case MOVIE: {
                Log.d(LOG_TAG, "MOVIE");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_COLUMN:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
//            case MOVIE_TRAILER:
//                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_FAVORITE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri();
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAV_MOVIE: {
                long _id = db.insertWithOnConflict(MovieContract.FavMovieEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if ( _id > 0 )
                    returnUri = MovieContract.FavMovieEntry.buildFavMovieUri();
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAV_MOVIE:
                rowsDeleted = db.delete(MovieContract.FavMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FAV_MOVIE:
                rowsUpdated = db.update(MovieContract.FavMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_IGNORE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
