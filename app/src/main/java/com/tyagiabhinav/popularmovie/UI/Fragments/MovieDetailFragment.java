package com.tyagiabhinav.popularmovie.UI.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tyagiabhinav.popularmovie.Model.Movie;
import com.tyagiabhinav.popularmovie.R;

public class MovieDetailFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private View rootView;

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the movie details.
     */
    public static MovieDetailFragment newInstance(Movie movie) {
        Log.d(LOG_TAG, "New Instance Created");
        MovieDetailFragment fragment = new MovieDetailFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(MoviePosterFragment.MOVIE_BUNDLE_KEY, movie);
        fragment.setArguments(args);

        return fragment;
    }

    public Movie getShownMovie() {
        Log.d(LOG_TAG, "getShownMovie");
        Bundle movieBundle = getArguments();
        Movie movie = null;
        if (movieBundle != null) {
            movie = (Movie) movieBundle.getSerializable(MoviePosterFragment.MOVIE_BUNDLE_KEY);
        }
        return movie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Movie movie = getShownMovie();//(Movie) movieBundle.getSerializable(MoviePosterFragment.MOVIE_BUNDLE_KEY);
        if (movie != null) {
            ((TextView) rootView.findViewById(R.id.detailTitle)).setText(movie.getTitle());
            ((TextView) rootView.findViewById(R.id.detailReleaseDate)).setText(movie.getReleaseDate().substring(0, 4));
            ((TextView) rootView.findViewById(R.id.detailRating)).setText(movie.getUserRating() + "/10");
            ((TextView) rootView.findViewById(R.id.detailVotes)).setText(movie.getUserVote() + " votes");
            ((TextView) rootView.findViewById(R.id.detailPlot)).setText(movie.getPlot());
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Picasso.with(getContext()).load(movie.getPosterPath()).placeholder(R.mipmap.ic_launcher).resize(150, 0).into(((ImageView) rootView.findViewById(R.id.detailPoster)));
            } else {
                Picasso.with(getContext()).load(movie.getPosterPath()).placeholder(R.mipmap.ic_launcher).into(((ImageView) rootView.findViewById(R.id.detailPoster)));
            }
        }

        return rootView;
    }

}
