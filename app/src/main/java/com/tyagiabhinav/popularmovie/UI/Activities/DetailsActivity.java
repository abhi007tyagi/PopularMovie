package com.tyagiabhinav.popularmovie.UI.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tyagiabhinav.popularmovie.UI.Fragments.MovieDetailFragment;

/**
 * Created by abhinavtyagi on 06/02/16.
 */
public class DetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            // If the screen is now in landscape mode, we can show the
//            // dialog in-line with the list so we don't need this activity.
//            finish();
//            return;
//        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content,movieDetailFragment).commit();
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
//
//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//
////            Bundle arguments = new Bundle();
////            arguments.putParcelable(MovieDetailFragment.DETAIL_URI, getIntent().getData());
//
//            MovieDetailFragment fragment = new MovieDetailFragment();
//            fragment.setArguments(getIntent().getExtras());
//
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.movieDetailContainer, fragment)
//                    .commit();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, SettingsActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}