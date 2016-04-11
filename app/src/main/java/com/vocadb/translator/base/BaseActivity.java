package com.vocadb.translator.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.vocadb.translator.R;

/**
 * Copyright 2016 VocaDB Software Development
 * Created by Adah Valeña on 4/5/2016.
 *
 * Base activity class that handles all common functions used by many activities
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResource();

    // Shared Preferences
    private SharedPreferences prefs;

    // Color
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout
        setContentView(getLayoutResource());

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Shared Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    /**
     * Method to get toolbar title
     */
    public TextView getToolbarTitle() {
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        return toolbarTitle;
    }

    /**
     * Method to retrieve user preferred color/theme
     */
    public int getSavedColor() {
        // Get color from shared preference
        return prefs.getInt("color", Constants.defaultColor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
