package com.tyagiabhinav.popularmovie.Util;

import android.content.ContentValues;
import android.util.Log;

import com.tyagiabhinav.popularmovie.DB.MovieContract.MovieEntry;
import com.tyagiabhinav.popularmovie.PopularMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by abhinavtyagi on 04/02/16.
 */
public class Parser {

    private static final String LOG_TAG = Parser.class.getSimpleName();

    private static final String RESULTS = "results";
    private static final String MOVIE_ID = "id";
    private static final String MOVIE_TITLE = "original_title";
    private static final String POSTER_PATH = "poster_path";
    private static final String RELEASE_DATE = "release_date";
    private static final String DURATION = "";
    private static final String USER_RATING = "vote_average";
    private static final String USER_VOTES = "vote_count";
    private static final String PLOT = "overview";
    private static final String POPULARITY = "popularity";
    private static final String TRAILER = "";
    private static final String REVIEW = "";
    private static final String TRAILER_KEY = "key";
    private static final String TRAILER_NAME = "name";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";

    private static final String POSTER_PATH_PREFIX = "http://image.tmdb.org/t/p/w185/";
    private static final String YOUTUBE_PATH_PREFIX = "https://www.youtube.com/watch?v=";

    public static void parseMovieList(String jsonResponse) {
//        ArrayList<Movie> movies = new ArrayList<>();
        if (jsonResponse != null && !jsonResponse.trim().isEmpty()) {
            try {
                JSONObject json = new JSONObject(jsonResponse);
                JSONArray movieArray = json.getJSONArray(RESULTS);
                int size = movieArray.length();

//                movies = new ArrayList<Movie>();

                Vector<ContentValues> moviesVector = new Vector<ContentValues>(size);
                for (int index = 0; index < size; index++) {
//                    Movie movie = new Movie();
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MovieEntry.COL_MOVIE_ID, movieArray.getJSONObject(index).getString(MOVIE_ID));
//                    movie.setId(movieArray.getJSONObject(index).getString(MOVIE_ID));
                    movieValues.put(MovieEntry.COL_MOVIE_TITLE, movieArray.getJSONObject(index).getString(MOVIE_TITLE));
//                    movie.setTitle(movieArray.getJSONObject(index).getString(MOVIE_TITLE));
                    movieValues.put(MovieEntry.COL_POSTER_PATH, POSTER_PATH_PREFIX + movieArray.getJSONObject(index).getString(POSTER_PATH));
//                    movie.setPosterPath(POSTER_PATH_PREFIX + movieArray.getJSONObject(index).getString(POSTER_PATH));
                    movieValues.put(MovieEntry.COL_RELEASE_DATE, movieArray.getJSONObject(index).getString(RELEASE_DATE));
//                    movie.setReleaseDate(movieArray.getJSONObject(index).getString(RELEASE_DATE));
                    movieValues.put(MovieEntry.COL_USER_RATING, movieArray.getJSONObject(index).getDouble(USER_RATING));
//                    movie.setUserRating(movieArray.getJSONObject(index).getString(USER_RATING));
                    movieValues.put(MovieEntry.COL_USER_VOTE, movieArray.getJSONObject(index).getInt(USER_VOTES));
//                    movie.setUserVote(movieArray.getJSONObject(index).getString(USER_VOTES));
                    movieValues.put(MovieEntry.COL_PLOT, movieArray.getJSONObject(index).getString(PLOT));
//                    movie.setPlot(movieArray.getJSONObject(index).getString(PLOT));
                    movieValues.put(MovieEntry.COL_POPULARITY, movieArray.getJSONObject(index).getString(POPULARITY));
//                    movies.add(movie);
                    moviesVector.add(movieValues);
                }
                int inserted = 0;
                if (moviesVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[moviesVector.size()];
                    moviesVector.toArray(cvArray);
                    inserted = PopularMovie.getAPPContext().getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, cvArray);
                }
                Log.d(LOG_TAG, "Fetch Movies Complete. " + inserted + " Inserted");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        return movies;
    }

    public static void parseTrailer(String jsonResponse){
        Log.d(LOG_TAG,"parseTrailer");

        if (jsonResponse != null && !jsonResponse.trim().isEmpty()) {
            try {
                JSONObject json = new JSONObject(jsonResponse);
                String movieId = json.getString(MOVIE_ID);
                JSONArray trailerArray = json.getJSONArray(RESULTS);
                int size = trailerArray.length();

//                Vector<ContentValues> moviesVector = new Vector<ContentValues>(size);
//                for (int index = 0; index < size; index++) {
                ContentValues movieValues = new ContentValues();
                if(size > 0) {
                    //saving only first trailer
//                    movieValues.put(MovieEntry.COL_MOVIE_ID, movieId);
                    String trailerPath = YOUTUBE_PATH_PREFIX + trailerArray.getJSONObject(0).getString(TRAILER_KEY);
                    String trailerName = trailerArray.getJSONObject(0).getString(TRAILER_NAME);
                    movieValues.put(MovieEntry.COL_TRAILER_PATH, trailerName + ";;" + trailerPath);
//                    moviesVector.add(movieValues);
                }
//                }
                int updated = 0;
                if(movieValues.size() > 0) {
                    updated = PopularMovie.getAPPContext().getContentResolver().update(MovieEntry.CONTENT_URI, movieValues, MovieEntry.COL_MOVIE_ID + " = ?", new  String[]{movieId});
                }
                Log.d(LOG_TAG, "Fetch Trailer Complete. " + updated + " Updated");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void parseReviews(String jsonResponse){
        Log.d(LOG_TAG,"parseReviews");

        if (jsonResponse != null && !jsonResponse.trim().isEmpty()) {
            try {
                JSONObject json = new JSONObject(jsonResponse);
                String movieId = json.getString(MOVIE_ID);
                JSONArray reviewArray = json.getJSONArray(RESULTS);
                int size = reviewArray.length();

//                Vector<ContentValues> moviesVector = new Vector<ContentValues>(size);
//                for (int index = 0; index < size; index++) {
                ContentValues movieValues = new ContentValues();
                if(size > 0) {
                    //saving only first review
//                    movieValues.put(MovieEntry.COL_MOVIE_ID, movieId);
                    String reviewAuthor = reviewArray.getJSONObject(0).getString(REVIEW_AUTHOR);
                    String reviewComment = reviewArray.getJSONObject(0).getString(REVIEW_CONTENT);
                    movieValues.put(MovieEntry.COL_REVIEW, reviewAuthor + ";;" + reviewComment);
//                    moviesVector.add(movieValues);
                }
//                }
                int updated = 0;
                if(movieValues.size() > 0) {
                    updated = PopularMovie.getAPPContext().getContentResolver().update(MovieEntry.CONTENT_URI, movieValues, MovieEntry.COL_MOVIE_ID + " = ?", new  String[]{movieId});
                }
                Log.d(LOG_TAG, "Fetch Review Complete. " + updated + " Updated");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
