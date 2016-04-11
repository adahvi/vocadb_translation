package com.vocadb.translator;
//
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.vocadb.translator.adapters.LanguageAdapter;
//import com.vocadb.translator.base.BaseActivity;
//
///**
// * Copyright 2016 VocaDB Software Development
// * Created by Adah Valeña on 4/5/2016.
// *
// * Language Activity class
// */
//public class LanguageActivity extends BaseActivity implements View.OnClickListener {
//
//    // Adapters
//    private LanguageAdapter languageAdapter;
//
//    // String Resource
//    private String[] languages;
//
//    // ListView
//    private ListView languageListView;
//
//    // UI Declarations
//    private Button btnCancel;
//    private Button btnOk;
//
//    // Color
//    private int color;
//
//    // Resources
//    private Resources res;
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_language;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Call init method
//        init();
//
//        // Init resources
//        res = this.getResources();
//        languages = getResources().getStringArray(R.array.lang_name_array);
//        languageListView = (ListView) findViewById(R.id.language_listview);
//        btnCancel = (Button) findViewById(R.id.cancel_button);
//        btnOk = (Button) findViewById(R.id.ok_button);
//
//        // Set view theme
//        setTheme();
//
//        // Set onclick listeners for button
//        btnCancel.setOnClickListener(this);
//        btnOk.setOnClickListener(this);
//
//        // Set adapter
//        languageAdapter = new LanguageAdapter(this, languages);
//        // Set adapter to listview
//        languageListView.setAdapter(languageAdapter);
//        // Notify adapter that there are changes on the list
//        languageAdapter.notifyDataSetChanged();
//    }
//
//    /**
//     * Method to initialize classes/libraries
//     */
//    private void init() {
//
//    }
//
//    /**
//     * Method to change screen theme based on user preferences
//     */
//    private void setTheme() {
//        // Get color from shared preferences
//        color = getSavedColor();
//        // Change toolbar title color
//        getToolbarTitle().setTextColor(color);
//        // Change button colors
//        btnCancel.setBackgroundColor(color);
//        btnOk.setBackgroundColor(color);
//    }
//
//    /**
//     * Method to handle all view click events
//     *
//     * @param v
//     */
//    @Override
//    public void onClick(View v) {
//        switch(v.getId()) {
//
//            case R.id.cancel_button:
//                // Finish and go back to previous activity
//                finish();
//                break;
//
//            case R.id.ok_button:
//                // TODO Save language value to Shared prefs
//                // Finish and go back to previous activity
//                finish();
//                break;
//
//            default:
//                break;
//        }
//    }
//}

import java.util.ArrayList;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vocadb.translator.base.BaseActivity;
import com.vocadb.translator.base.Constants;
import com.vocadb.translator.databasehelpers.Database;
import com.vocadb.translator.models.Language;
import com.vocadb.translator.models.LanguageClass;

public class LanguageActivity  extends BaseActivity implements View.OnClickListener {

    MyCustomAdapter dataAdapter = null;
    private Database db;
    private ProgressDialog pdialog;

    private SharedPreferences prefs;
    private int color;
    private ArrayList<String> list;

    // UI Declarations
    private Button btnCancel;
    private Button btnOk;

    // Resources
    private Resources res;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_language;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Call init method
        init();

        // Init resources
        res = this.getResources();

        // UI References
        btnCancel = (Button) findViewById(R.id.cancel_button);
        btnOk = (Button) findViewById(R.id.ok_button);

        // Set onclick listeners for button
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        // Set view theme
        setTheme();

//        pdialog = new ProgressDialog(this);
        //Generate list View from ArrayList
        displayListView();

    }

    /**
      * Method to initialize classes/libraries
    */
    private void init() {
        // Database
        db = new Database(this);

        // Preference Manager
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit();
    }

    /**
     * Method to change screen theme based on user preferences
     */
    private void setTheme() {
        // Get color from shared preferences
        color = getSavedColor();
        // Change toolbar title color
        getToolbarTitle().setTextColor(color);
        // Change button colors
        btnCancel.setBackgroundColor(color);
        btnOk.setBackgroundColor(color);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();

        setResult(7, intent);
        finish();//finishing activity
        super.onBackPressed();
    }

    private void displayListView() {

        ArrayList<LanguageClass> my_languages = db.getMyLanguages();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this, R.layout.list_language, my_languages);
        ListView listView = (ListView) findViewById(R.id.language_listview);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        //create an ArrayAdaptar from the String Array
        list = new ArrayList<String>();
        for (int i = 0; i < my_languages.size(); i++) {
            String name = "";
            boolean state = my_languages.get(i).isSelected();
            if (state) {


            }
            name = my_languages.get(i).getLanguageName();
            list.add(name);

        }


    }

    private class MyCustomAdapter extends ArrayAdapter<LanguageClass> {

        private ArrayList<LanguageClass> languageList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<LanguageClass> languageList) {
            super(context, textViewResourceId, languageList);
            this.languageList = new ArrayList<LanguageClass>();
            this.languageList.addAll(languageList);
        }

        private class ViewHolder {

            CheckBox check_box;
            public TextView textView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            // Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_language, null);

                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.language_name);
                holder.check_box = (CheckBox) convertView.findViewById(R.id.language_checkbox);

                convertView.setTag(holder);

                holder.check_box.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        LanguageClass LanguageClass = (LanguageClass) cb.getTag();

                        LanguageClass.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            LanguageClass LanguageClass = languageList.get(position);
            holder.textView.setText(LanguageClass.getLanguageName());
            holder.check_box.setChecked(LanguageClass.isSelected());
            holder.check_box.setTag(LanguageClass);

            return convertView;

        }

    }

    /**
     * Method to handle all view click events
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.cancel_button:
                // Finish and go back to previous activity
                setResult(2,new Intent());
                finish();
                break;

            case R.id.ok_button:
                // TODO Save language value to Shared prefs
                new UpdateTask().execute();
                // Finish and go back to previous activity
                finish();
                break;

            default:
                break;
        }
    }

    private class UpdateTask extends AsyncTask<String, String, Void> {
        @Override
        protected Void doInBackground(String... params) {
            ArrayList<LanguageClass> languageList = dataAdapter.languageList;
            for (int i = 0; i < languageList.size(); i++) {
                LanguageClass LanguageClass = languageList.get(i);
                if (LanguageClass.isSelected()) {

                }
                db.UpdateMyLanguage(new LanguageClass(LanguageClass.getID(), LanguageClass.getLanguageName(), LanguageClass.getLangCode(), LanguageClass.isSelected()));
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
//            pdialog = new ProgressDialog(LanguageActivity.this);
//            pdialog.setMessage("Updating Database");
//            pdialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(),
                    "Language Updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();

            setResult(2, intent);
            finish();//finishing activity
//            pdialog.dismiss();
        }


    }
}