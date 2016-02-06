package com.tyagiabhinav.popularmovie.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tyagiabhinav.popularmovie.Model.Movie;
import com.tyagiabhinav.popularmovie.Network.ServiceAsync;
import com.tyagiabhinav.popularmovie.R;
import com.tyagiabhinav.popularmovie.UI.Activities.DetailsActivity;
import com.tyagiabhinav.popularmovie.UI.Adapters.PosterAdapter;
import com.tyagiabhinav.popularmovie.Util.Parser;

import java.util.ArrayList;

public class MoviePosterFragment extends Fragment implements ServiceAsync.ServiceAsyncResponse{

    private static final String LOG_TAG = MoviePosterFragment.class.getSimpleName();

    private View rootView;
    private PosterAdapter posterAdapter;
    private ArrayList<Movie> movies;

    public static final String MOVIE_BUNDLE_KEY = "movieBundle";

    OnMovieSelectedListener mListener;

    public MoviePosterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(LOG_TAG,"onCreateView");
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movie_poster, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG,"onStart");
        super.onStart();
        updatePosters();
    }

    private void updatePosters() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = sharedPrefs.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_desc));
        if(sortType.equalsIgnoreCase(getString(R.string.pref_sort_desc))) {
            ServiceAsync discoverMovieService = new ServiceAsync(getActivity(), this, ServiceAsync.DISCOVER_MOVIES_SERVICE_DESC);
            discoverMovieService.execute();
        }else if(sortType.equalsIgnoreCase(getString(R.string.pref_sort_high))) {
            ServiceAsync discoverMovieService = new ServiceAsync(getActivity(), this, ServiceAsync.DISCOVER_MOVIES_SERVICE_HIGH);
            discoverMovieService.execute();
        }
    }

    @Override
    public void processFinish( String response) {
        Log.d(LOG_TAG, "Response Received");
        movies = Parser.parseMovieList(response);
        if(movies != null && !movies.isEmpty()) {
            posterAdapter = new PosterAdapter(getActivity(), movies);
            mListener.onMovieSelected(movies.get(0));
            // Get a reference to the GridView, and attach this adapter to it.
            GridView gridView = (GridView) rootView.findViewById(R.id.movieGrid);
            gridView.setAdapter(posterAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(LOG_TAG,movies.get(position).getTitle() + " selected");
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            mListener.onMovieSelected(movies.get(position));
                        } else {
                            Intent detailIntent = new Intent(getActivity(), DetailsActivity.class);
                            Bundle movieBundle = new Bundle();
                            movieBundle.putSerializable(MOVIE_BUNDLE_KEY, movies.get(position));
                            detailIntent.putExtras(movieBundle);
                            startActivity(detailIntent);
                        }
                    }
                }

            );
                posterAdapter.setNotifyOnChange(true);
            }
        }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnMovieSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMovieSelectedListener");
        }
    }

    // Container Activity must implement this interface
    public interface OnMovieSelectedListener {
        public void onMovieSelected(Movie movie);
    }
}
