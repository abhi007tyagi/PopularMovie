package com.tyagiabhinav.popularmovie.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tyagiabhinav.popularmovie.Model.Movie;
import com.tyagiabhinav.popularmovie.R;
import com.tyagiabhinav.popularmovie.UI.Fragments.MovieDetailFragment;
import com.tyagiabhinav.popularmovie.UI.Fragments.MoviePosterFragment;

public class MainActivity extends AppCompatActivity implements MoviePosterFragment.OnMovieSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Movie Poster fragment
        // Create a new Fragment to be placed in the activity layout
        MoviePosterFragment posterFragment = new MoviePosterFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
//        posterFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'moviePosterContainer' FrameLayout
        getSupportFragmentManager().beginTransaction().add(R.id.moviePosterContainer, posterFragment).commit();

        //check orientation
        if (getResources().getBoolean(R.bool.dualPane)) {
            // landscape view
            findViewById(R.id.movieDetailContainer).setVisibility(View.VISIBLE);

            // Movie Detail fragment
            // Create a new Fragment to be placed in the activity layout
            MovieDetailFragment detailFragment = new MovieDetailFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
//        detailFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'moviePosterContainer' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.movieDetailContainer, detailFragment).commit();
        }
//        else{
//            // portrait view
//            findViewById(R.id.movieDetailContainer).setVisibility(View.GONE);
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        Log.d(LOG_TAG, movie.getTitle() + " selected");

        if(getResources().getBoolean(R.bool.dualPane)){
            selectedMovie = movie;
            handler.sendEmptyMessage(DETAIL_FRAGMENT);
        }
        else{
            showDetailScreen(movie);
        }

    }

    private static final int DETAIL_FRAGMENT = 900;
    private Movie selectedMovie;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == DETAIL_FRAGMENT) {
                showDetailScreen(selectedMovie);
            }
        }
    };

    private void showDetailScreen(Movie movie){
        Log.d(LOG_TAG,"showDetailScreen.......XXXXXX");
        // Make new fragment to show this selection.
        MovieDetailFragment detailsFragment = MovieDetailFragment.newInstance(movie);

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (movie != null) {
            ft.replace(R.id.movieDetailContainer, detailsFragment);
        }
        ft.commit();
    }
}
