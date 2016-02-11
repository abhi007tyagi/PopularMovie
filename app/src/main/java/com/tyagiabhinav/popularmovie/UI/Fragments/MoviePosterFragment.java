package com.tyagiabhinav.popularmovie.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tyagiabhinav.popularmovie.DB.MovieContract;
import com.tyagiabhinav.popularmovie.Model.Movie;
import com.tyagiabhinav.popularmovie.Network.ServiceAsync;
import com.tyagiabhinav.popularmovie.R;
import com.tyagiabhinav.popularmovie.UI.Activities.DetailsActivity;
import com.tyagiabhinav.popularmovie.UI.Adapters.PosterCursorAdapter;

import java.util.ArrayList;

public class MoviePosterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MoviePosterFragment.class.getSimpleName();

    private View rootView;
    //    private PosterAdapter posterAdapter;
    private PosterCursorAdapter posterCursorAdapter;
    private ArrayList<Movie> movies;

    public static final String MOVIE_BUNDLE_KEY = "movieBundle";

    OnMovieSelectedListener mListener;
    private GridView gridView;
    private int mPosition;
    private static final int MOVIE_LOADER = 0;
    private static final int MOVIE_LOADER_DATA = 1;

    String sortOrder = MovieContract.MovieEntry.COL_POPULARITY + " DESC";
    private String selectedMovieId = "";

    private int numberOfRuns = 0;

    public MoviePosterFragment() {
//        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
//        mPosition = PopularMovie.getAPPContext().getmPosition();
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movie_poster, container, false);
        gridView = (GridView) rootView.findViewById(R.id.movieGrid);

        posterCursorAdapter = new PosterCursorAdapter(getActivity(), null, 0);
        gridView.setAdapter(posterCursorAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                Log.d(LOG_TAG, position + " selected");
                                                mPosition = position;
//                                                PopularMovie.getAPPContext().setmPosition(mPosition);
                                                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                                                if (cursor != null) {
//                                                    selectedMovieId = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_MOVIE_ID));
//                                                    Log.d(LOG_TAG, selectedMovieId + " selected movie id");
//                                                    if (numberOfRuns < 1) {
//                                                        getLoaderManager().initLoader(MOVIE_LOADER_DATA, null, MoviePosterFragment.this);
//                                                    }
                                                    if (getResources().getBoolean(R.bool.dualPane)) {
                                                        mListener.onMovieSelected(getMovie(cursor));
                                                    } else {
                                                        Intent detailIntent = new Intent(getActivity(), DetailsActivity.class);
                                                        Bundle movieBundle = new Bundle();
                                                        movieBundle.putParcelable(MOVIE_BUNDLE_KEY, getMovie(cursor));
                                                        detailIntent.putExtras(movieBundle);
                                                        startActivity(detailIntent);
                                                    }
                                                }
                                            }
                                        }

        );

        posterCursorAdapter.notifyDataSetChanged();

        sortOrder();

        return rootView;
    }

    private Movie getMovie(Cursor cursor) {
        Log.d(LOG_TAG, "onStart");
        Movie movie = new Movie();
        movie.setId(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_MOVIE_ID)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_MOVIE_TITLE)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_POSTER_PATH)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_RELEASE_DATE)));
        movie.setUserRating(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_USER_RATING)));
        movie.setUserVote(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_USER_VOTE)));
        movie.setPlot(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_PLOT)));
        movie.setTrailerPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_TRAILER_PATH)));
        movie.setReview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_REVIEW)));
        movie.setIsFavourite(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COL_FAVOURITE)));

        return movie;
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "onStart");
        super.onStart();
        updatePosters();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
//
//        // Locate MenuItem with ShareActionProvider
//        MenuItem item = menu.findItem(R.id.action_share);
//        item.setVisible(false);
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Activity Created");
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updatePosters() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = sharedPrefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_desc));

        //Default order
        if (sortType.equalsIgnoreCase(getString(R.string.pref_sort_desc))) {
            sortOrder = MovieContract.MovieEntry.COL_POPULARITY + " DESC";
            ServiceAsync discoverMovieService = new ServiceAsync(getActivity(), this, ServiceAsync.DISCOVER_MOVIES_SERVICE_DESC);
            discoverMovieService.execute();
        } else if (sortType.equalsIgnoreCase(getString(R.string.pref_sort_high))) {
            sortOrder = MovieContract.MovieEntry.COL_USER_RATING + " DESC";
            ServiceAsync discoverMovieService = new ServiceAsync(getActivity(), this, ServiceAsync.DISCOVER_MOVIES_SERVICE_HIGH);
            discoverMovieService.execute();
        }
//        else if(sortType.equalsIgnoreCase(getString(R.string.pref_sort_fav))) {
//            Uri favMovies = MovieContract.FavMovieEntry.buildFavMovieUri();
//            Cursor cur = getActivity().getContentResolver().query(favMovies, null, null, null, null);
//            posterCursorAdapter.changeCursor(cur);
//        }
//        Uri movieByPopularity = MovieContract.MovieEntry.buildMovieUri(System.currentTimeMillis());
//        Cursor cur = getActivity().getContentResolver().query(movieByPopularity, null, null, null, sortOrder);
//        posterCursorAdapter = new PosterCursorAdapter(getActivity(), cur, 0);
//        gridView.setAdapter(posterCursorAdapter);
    }

    private void sortOrder() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = sharedPrefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_desc));

        if (sortType.equalsIgnoreCase(getString(R.string.pref_sort_desc))) {
            sortOrder = MovieContract.MovieEntry.COL_POPULARITY + " DESC";
        } else if (sortType.equalsIgnoreCase(getString(R.string.pref_sort_high))) {
            sortOrder = MovieContract.MovieEntry.COL_USER_RATING + " DESC";
        } else if (sortType.equalsIgnoreCase(getString(R.string.pref_sort_fav))) {
            sortOrder = MovieContract.FavMovieEntry.COL_MOVIE_ID + " DESC";
        }
    }

//    @Override
//    public void processFinish( String response) {
//        Log.d(LOG_TAG, "Response Received");
//        movies = Parser.parseMovieList(response);
//        Parser.parseMovieList(response);

//        String sortOrder = MovieContract.MovieEntry.COL_POPULARITY + " DESC";
//        Uri movieByPopularity = MovieContract.MovieEntry.buildMovieUri(System.currentTimeMillis());
//        Cursor cur = getActivity().getContentResolver().query(movieByPopularity, null, null, null, sortOrder);
//        posterCursorAdapter = new PosterCursorAdapter(getActivity(), cur, 0);

//        if(movies != null && !movies.isEmpty()) {
////            posterAdapter = new PosterAdapter(getActivity(), movies);
//            mListener.onMovieSelected(movies.get(0));
//            // Get a reference to the GridView, and attach this adapter to it.
//
////            gridView.setAdapter(posterAdapter);
//            gridView.setAdapter(posterCursorAdapter);
//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Log.d(LOG_TAG, movies.get(position).getTitle() + " selected");
//                        mPosition = position;
//                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                            mListener.onMovieSelected(movies.get(position));
//                        } else {
//                            Intent detailIntent = new Intent(getActivity(), DetailsActivity.class);
//                            Bundle movieBundle = new Bundle();
//                            movieBundle.putSerializable(MOVIE_BUNDLE_KEY, movies.get(position));
//                            detailIntent.putExtras(movieBundle);
//                            startActivity(detailIntent);
//                        }
//                    }
//                }
//
//            );
////                posterAdapter.setNotifyOnChange(true);
//            posterCursorAdapter.notifyDataSetChanged();
//            }
//        }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnMovieSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMovieSelectedListener");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader");
//        Toast.makeText(getActivity(),"Sorted by: "+sortOrder, Toast.LENGTH_LONG).show();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = sharedPrefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_desc));
//
//        Loader<Cursor> loader = null;
//
//        switch (id) {
//            case MOVIE_LOADER_DATA:
//                Uri movies = MovieContract.MovieEntry.buildMovieDataUri(selectedMovieId);
//                loader = new CursorLoader(getActivity(),
//                        movies,
//                        null,
//                        MovieContract.MovieEntry.COL_MOVIE_ID + " = ?",
//                        null,
//                        sortOrder);
//                numberOfRuns++;
//            break;
//            case MOVIE_LOADER:
                Uri movie = MovieContract.MovieEntry.buildMovieUri();
                if (sortType.equalsIgnoreCase(getString(R.string.pref_sort_fav))) {
                    movie = MovieContract.FavMovieEntry.buildFavMovieUri();
                } else {
                    movie = MovieContract.MovieEntry.buildMovieUri();
                }
                return new CursorLoader(getActivity(),
                        movie,
                        null,
                        null,
                        null,
                        sortOrder);
//                break;
//        }
//        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished: position -->" + mPosition);
        if (loader != null && cursor != null) {
//            switch (loader.getId()) {
//                case MOVIE_LOADER:
                    posterCursorAdapter.swapCursor(cursor);
                    if (mPosition != GridView.INVALID_POSITION) {
                        // If we don't need to restart the loader, and there's a desired position to restore
                        // to, do so now.
                        gridView.smoothScrollToPosition(mPosition);
                        if (cursor.getCount() > 0 && cursor.getCount() > mPosition && getResources().getBoolean(R.bool.dualPane)) {
                            cursor.moveToPosition(mPosition);
                            Movie selectedMovie = getMovie(cursor);
                            Log.d(LOG_TAG, "Selected Movie -->" + selectedMovie.getTitle());
                            mListener.onMovieSelected(selectedMovie);
                        }
                    }
//                    break;
//                case MOVIE_LOADER_DATA:
//                    if (cursor.getCount() > 0) {
//                        cursor.moveToFirst();
//                        if (getResources().getBoolean(R.bool.dualPane)) {
//                            mListener.onMovieSelected(getMovie(cursor));
//                        } else {
//                            Intent detailIntent = new Intent(getActivity(), DetailsActivity.class);
//                            Bundle movieBundle = new Bundle();
//                            movieBundle.putParcelable(MOVIE_BUNDLE_KEY, getMovie(cursor));
//                            detailIntent.putExtras(movieBundle);
//                            startActivity(detailIntent);
//                        }
//                    }
//                    break;
//            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        posterCursorAdapter.swapCursor(null);
    }


    // Container Activity must implement this interface
    public interface OnMovieSelectedListener {
        public void onMovieSelected(Movie movie);
    }
}
