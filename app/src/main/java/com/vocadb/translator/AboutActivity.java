package com.vocadb.translator;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.vocadb.translator.base.BaseActivity;

/**
 * Copyright 2016 VocaDB Software Development
 * Created by Adah Valeña on 4/5/2016.
 *
 * About Activity class
 */
public class AboutActivity extends BaseActivity {

    // UI References
    private TextView versionNameView;
    private TextView appLinkView;

    // Color
    private int color;

    // Resources
    private Resources res;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init resources
        res = this.getResources();

        // Set view theme
        setTheme();

        // Initialize UI components
        versionNameView = (TextView) findViewById(R.id.app_version);
        appLinkView = (TextView) findViewById(R.id.app_url);

        // Set version
        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();

        String appVersion = "";
        try {
            appVersion = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Set value version name
        versionNameView.setText("Version" + appVersion);
    }

    /**
     * Method to initialize classes/libraries
     */
    private void init() {
    }

    /**
     * Method to change screen theme based on user preferences
     */
    private void setTheme() {
        // Get color from shared preferences
        color = getSavedColor();
        // Change toolbar title color
        getToolbarTitle().setTextColor(color);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
