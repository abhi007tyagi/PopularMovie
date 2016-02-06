package com.tyagiabhinav.popularmovie.Util;

import com.tyagiabhinav.popularmovie.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abhinavtyagi on 04/02/16.
 */
public class Parser {

    private static final String RESULTS = "results";
    private static final String MOVIE_ID = "id";
    private static final String MOVIE_TITLE = "original_title";
    private static final String POSTER_PATH = "poster_path";
    private static final String RELEASE_DATE = "release_date";
    private static final String DURATION = "";
    private static final String USER_RATING = "vote_average";
    private static final String USER_VOTES = "vote_count";
    private static final String PLOT = "overview";
    private static final String TRAILER = "";
    private static final String REVIEW = "";

    private static final String POSTER_PATH_PREFIX = "http://image.tmdb.org/t/p/w185/";


    private String id;
    private String title;
    private String posterPath;
    private String releaseDate;
    private String duration;
    private String userRating;
    private String plot;
    private String trailerPath;
    private String review;

    public static ArrayList<Movie> parseMovieList(String jsonResponse) {
        ArrayList<Movie> movies = new ArrayList<>();
        if (jsonResponse != null && !jsonResponse.trim().isEmpty()) {
            try {
                JSONObject json = new JSONObject(jsonResponse);
                JSONArray movieArray = json.getJSONArray("results");
                int size = movieArray.length();

                movies = new ArrayList<Movie>();
                for (int index = 0; index < size; index++) {
                    Movie movie = new Movie();
                    movie.setId(movieArray.getJSONObject(index).getString(MOVIE_ID));
                    movie.setTitle(movieArray.getJSONObject(index).getString(MOVIE_TITLE));
                    movie.setPosterPath(POSTER_PATH_PREFIX + movieArray.getJSONObject(index).getString(POSTER_PATH));
                    movie.setReleaseDate(movieArray.getJSONObject(index).getString(RELEASE_DATE));
                    movie.setUserRating(movieArray.getJSONObject(index).getString(USER_RATING));
                    movie.setUserVote(movieArray.getJSONObject(index).getString(USER_VOTES));
                    movie.setPlot(movieArray.getJSONObject(index).getString(PLOT));
                    movies.add(movie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movies;
    }
}
