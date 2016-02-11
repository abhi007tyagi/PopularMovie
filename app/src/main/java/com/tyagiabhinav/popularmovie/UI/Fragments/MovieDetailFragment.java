package com.tyagiabhinav.popularmovie.UI.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.tyagiabhinav.popularmovie.DB.MovieContract;
import com.tyagiabhinav.popularmovie.Model.Movie;
import com.tyagiabhinav.popularmovie.Network.MovieSyncAdapter;
import com.tyagiabhinav.popularmovie.PopularMovie;
import com.tyagiabhinav.popularmovie.R;
import com.tyagiabhinav.popularmovie.Util.PreferenceHelper;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private static final String SHARE_TAG = "#PopulateMovie ";
    private static final int MOVIE_LOADER = 1;
    private static final int TRUE = 1;
    private static final int FALSE = 0;

    private View rootView;
    private String selectedMovieId = "";
    private String mShareString = "";

    public MovieDetailFragment(){
        setHasOptionsMenu(true);
    }

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the movie details.
     */
    public static MovieDetailFragment newInstance(Movie movie) {



        Log.d(LOG_TAG, "New Instance Created");

//        //get data from server
//        ServiceAsync extendedMovieService = new ServiceAsync(PopularMovie.getAPPContext(), ServiceAsync.MOVIES_TRAILER_SERVICE, movie.getId());
//        extendedMovieService.execute();

        // create instance
        MovieDetailFragment fragment = new MovieDetailFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(MoviePosterFragment.MOVIE_BUNDLE_KEY, movie);
        fragment.setArguments(args);
        return fragment;
    }

    public Movie getShownMovie() {
        Log.d(LOG_TAG, "getShownMovie");
        Bundle movieBundle = getArguments();
        Movie movie = null;
        if (movieBundle != null) {
            movie = (Movie) movieBundle.getParcelable(MoviePosterFragment.MOVIE_BUNDLE_KEY);
        }
        return movie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Movie movie = getShownMovie();
        if (movie != null) {
            Log.d(LOG_TAG,"TRAILER -->"+movie.getTrailerPath());
            if(movie.getTrailerPath() == null || movie.getTrailerPath().trim().isEmpty()){
                //get data from server
                PreferenceHelper.setServiceType(MovieSyncAdapter.MOVIES_TRAILER_SERVICE);
                PreferenceHelper.setMovieId(movie.getId());
                MovieSyncAdapter.syncImmediately(getActivity());
//                ServiceAsync extendedMovieService = new ServiceAsync(PopularMovie.getAPPContext(), ServiceAsync.MOVIES_TRAILER_SERVICE, movie.getId());
//                extendedMovieService.execute();
            }
            Log.d(LOG_TAG, "REVIEW -->" + movie.getReview());
            if(movie.getReview() == null || movie.getReview().trim().isEmpty()){
                //get data from server
                PreferenceHelper.setServiceType(MovieSyncAdapter.MOVIES_REVIEW_SERVICE);
                PreferenceHelper.setMovieId(movie.getId());
                MovieSyncAdapter.syncImmediately(getActivity());
//                ServiceAsync extendedMovieService = new ServiceAsync(PopularMovie.getAPPContext(), ServiceAsync.MOVIES_REVIEW_SERVICE, movie.getId());
//                extendedMovieService.execute();
            }

            selectedMovieId = movie.getId();

            ((TextView) rootView.findViewById(R.id.detailTitle)).setText(movie.getTitle());
            String releaseDate = getString(R.string.no_release_date);
            if (!movie.getReleaseDate().trim().isEmpty()) {
                releaseDate = movie.getReleaseDate().substring(0, 4);
            }
            ((TextView) rootView.findViewById(R.id.detailReleaseDate)).setText(releaseDate);
            ((TextView) rootView.findViewById(R.id.detailRating)).setText(movie.getUserRating() + "/10");
            ((TextView) rootView.findViewById(R.id.detailVotes)).setText(movie.getUserVote() + " votes");
            ((TextView) rootView.findViewById(R.id.detailPlot)).setText(movie.getPlot());
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Picasso.with(getContext()).load(movie.getPosterPath()).placeholder(R.mipmap.ic_launcher).resize(150, 0).into(((ImageView) rootView.findViewById(R.id.detailPoster)));
            } else {
                Picasso.with(getContext()).load(movie.getPosterPath()).placeholder(R.mipmap.ic_launcher).into(((ImageView) rootView.findViewById(R.id.detailPoster)));
            }

            ToggleButton favBtn = (ToggleButton) rootView.findViewById(R.id.favBtn);
            if(movie.isFavourite() == TRUE){
                Log.d(LOG_TAG, "IS FAVOURITE");
                favBtn.setChecked(true);
//                favBtn.setText(favBtn.getTextOn());
            }else if(movie.isFavourite() == FALSE){
                Log.d(LOG_TAG,"NOT FAVOURITE");
                favBtn.setChecked(false);
//                favBtn.setText(favBtn.getTextOff());
            }

            favBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(LOG_TAG,"Selected Movie Id -->"+selectedMovieId);
                    if (isChecked) {
                        Log.d(LOG_TAG, "is checked");
                        ContentValues favMovieValues = new ContentValues();
                        favMovieValues.put(MovieContract.FavMovieEntry.COL_MOVIE_ID, selectedMovieId);
                        Uri inserted = null;
                        if (favMovieValues.size() > 0) {
                            inserted = PopularMovie.getAPPContext().getContentResolver().insert(MovieContract.FavMovieEntry.CONTENT_URI, favMovieValues);//, MovieContract.MovieEntry.COL_MOVIE_ID + " = ?", new  String[]{movieId});
                        }
                        Log.d(LOG_TAG, "Insert Favourite Complete. " + inserted + " Inserted");

                        ContentValues movieValues = new ContentValues();
                        movieValues.put(MovieContract.MovieEntry.COL_FAVOURITE, TRUE);
                        int updated = 0;
                        if (movieValues.size() > 0) {
                            updated = PopularMovie.getAPPContext().getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, movieValues, MovieContract.MovieEntry.COL_MOVIE_ID + " = ?", new String[]{selectedMovieId});
                        }
                        Log.d(LOG_TAG, "Updated Favourite Complete. " + updated + " Updated");
                    } else {
                        Log.d(LOG_TAG, "not checked");
                        int deleted = 0;
                        deleted = PopularMovie.getAPPContext().getContentResolver().delete(MovieContract.FavMovieEntry.CONTENT_URI, MovieContract.FavMovieEntry.COL_MOVIE_ID + " = ?", new String[]{selectedMovieId});
                        Log.d(LOG_TAG, "Delete Favourite Complete. " + deleted + " Deleted");

                        ContentValues movieValues = new ContentValues();
                        movieValues.put(MovieContract.MovieEntry.COL_FAVOURITE, FALSE);
                        int updated = 0;
                        if (movieValues.size() > 0) {
                            updated = PopularMovie.getAPPContext().getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, movieValues, MovieContract.MovieEntry.COL_MOVIE_ID + " = ?", new String[]{selectedMovieId});
                        }
                        Log.d(LOG_TAG, "Updated Favourite Complete. " + updated + " Updated");
                    }
                }
            });

//            if (movie.getTrailerPath() != null && !movie.getTrailerPath().trim().isEmpty()) {
//                final String[] trailer = movie.getTrailerPath().split(";;");
//                if (trailer[0] != null && !trailer[0].trim().isEmpty() && trailer[1] != null && !trailer[1].trim().isEmpty()) {
//                    TextView trailerBtn = (TextView) rootView.findViewById(R.id.trailer);
//                    trailerBtn.setVisibility(View.VISIBLE);
//                    trailerBtn.setText(trailer[0]);
//                    trailerBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Log.d(LOG_TAG, "Trailer Clicked");
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer[1]));
//                            startActivity(intent);
//                        }
//                    });
//                }

//            }
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.share_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(shareTrailer());
        }
        else{
            Log.d(LOG_TAG,"Share Action Provider is NULL");
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private Intent shareTrailer(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, SHARE_TAG + mShareString);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        Uri trailer = MovieContract.MovieEntry.buildMovieDataUri(selectedMovieId);
        return new CursorLoader(getActivity(),
                trailer,
                new String[]{MovieContract.MovieEntry.COL_TRAILER_PATH, MovieContract.MovieEntry.COL_REVIEW},
                MovieContract.MovieEntry.COL_MOVIE_ID + " = ?",
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished -->" + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String trailerResult = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_TRAILER_PATH));
            if (trailerResult != null) {
                Log.d(LOG_TAG, "Result -->" + trailerResult);
                final String[] trailer = trailerResult.split(";;");
                if (trailer[0] != null && !trailer[0].trim().isEmpty() && trailer[1] != null && !trailer[1].trim().isEmpty()) {
                    TextView trailerBtn = (TextView) rootView.findViewById(R.id.trailer);
                    trailerBtn.setVisibility(View.VISIBLE);
                    trailerBtn.setText(trailer[0]);
                    mShareString = trailer[0]+" -- "+trailer[1];
                    trailerBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(LOG_TAG, "Trailer Clicked");
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer[1]));
                            startActivity(intent);
                        }
                    });
                }
            }
            String reviewResult = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COL_REVIEW));
            if (reviewResult != null) {
                Log.d(LOG_TAG, "Result -->" + reviewResult);
                final String[] review = reviewResult.split(";;");
                if (review[0] != null && !review[0].trim().isEmpty() && review[1] != null && !review[1].trim().isEmpty()) {
                    TextView reviewComments = (TextView) rootView.findViewById(R.id.review);
                    reviewComments.setVisibility(View.VISIBLE);
                    reviewComments.setText(Html.fromHtml("<i><b>Review</b> by " + review[0] + "</i><br>" + review[1] + "<br>"));
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
