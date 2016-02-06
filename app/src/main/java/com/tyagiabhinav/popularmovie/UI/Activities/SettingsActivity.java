package com.tyagiabhinav.popularmovie.UI.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tyagiabhinav.popularmovie.UI.Fragments.SettingsFragment;

/**
 * Created by abhinavtyagi on 05/02/16.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
