package com.vocadb.translator;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    // UI References
    private TextView versionNameView;
    private TextView appLinkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

}
