package com.vocadb.translator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.vocadb.translator.adapters.SpinnerAdapter;
import com.vocadb.translator.broadcastreceivers.LanguageDetailsChecker;
import com.vocadb.translator.databasehelpers.Database;
import com.vocadb.translator.models.LanguageClass;
import com.vocadb.translator.utilities.ConnectionDetectorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity Class
 *
 * Created by Adah Valeña on 4/5/2016.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    // List
    private List<String> speechSupportedLangs = new ArrayList<String>();
    public ArrayList<LanguageClass> languageList;
    private ArrayList<String> spinner_one_list;
    private ArrayList<String> spinner_two_list;

    // Bools
    private Boolean exit = false;
    protected Boolean clear;

    // UI declarations
    private ImageView rotateImageView;
    private ImageView voiceInputImageView;
    private ImageView clearImageView;
    private ImageView exeInputImageView;

    private EditText sourceEditText;
    private EditText outputEditText;

    private Spinner sourceLangSpinner;
    private Spinner targetLangSpinner;
    private Animation anim_slide_in_left;
    private Animation anim_slide_in_right;

    // Layouts
    private LinearLayout vocaDBLayout;
    private LinearLayout headerLayout;
    private LinearLayout outputLayout;
    private LinearLayout controlPanelLayout;
    private LinearLayout sourcePanelLayout;

    // Database
//    private VocaDBHelper dbHelper;
    private Database db;

    // Datasource
//    private LanguageDataSource languageDataSource;

    // Media
    protected TextToSpeech textToSpeech;
    private MediaPlayer mediaPlayer;

    // Utilities
    private ConnectionDetectorUtil connectionDetectorUtil;

    // Adapters
    private SpinnerAdapter sourceLangSpinnerAdapter;
    private SpinnerAdapter targetLangSpinnerAdapter;

    // Broadcast Receivers
    private LanguageDetailsChecker languageDetailsChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Call init method
        init();

        // Editor
        editor = prefs.edit();
        speechSupportedLangs.add("en");

        // UI References
        rotateImageView = (ImageView) findViewById(R.id.rotateImageView);
        voiceInputImageView = (ImageView) findViewById(R.id.voiceInputImageView);
        clearImageView = (ImageView) findViewById(R.id.clearImageView);
        exeInputImageView = (ImageView) findViewById(R.id.exeInputImageView);

        sourceEditText = (EditText) findViewById(R.id.sourceEditText);
        outputEditText = (EditText) findViewById(R.id.outputEditText);

        sourceLangSpinner = (Spinner) findViewById(R.id.sourceLangSpinner);
        targetLangSpinner = (Spinner) findViewById(R.id.targetLangSpinner);

        // Layouts
        vocaDBLayout = (LinearLayout)findViewById(R.id.vocadbLayout);
        headerLayout = (LinearLayout)findViewById(R.id.headerLayout);
        outputLayout = (LinearLayout)findViewById(R.id.outputLayout);
        controlPanelLayout = (LinearLayout)findViewById(R.id.controlpanelLayout);
        sourcePanelLayout = (LinearLayout)findViewById(R.id.sourcePanelLayout);

        // Button clicks
        rotateImageView.setOnClickListener(this);
        clearImageView.setOnClickListener(this);

        // EditText change
        sourceEditText.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // Check if edittext is not empty
                if (count>0) {
                    // Show clear text button
                    clearImageView.setVisibility(View.VISIBLE);
                    exeInputImageView.setVisibility(View.VISIBLE);
                    voiceInputImageView.setVisibility(View.GONE);
                } else {
                    // Hide clear text button
                    clearImageView.setVisibility(View.GONE);
                    exeInputImageView.setVisibility(View.GONE);
                    voiceInputImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //  doTranslation(langs.get(spinner.getSelectedItem().toString()),langs.get(spinner2.getSelectedItem().toString()),sourceEditText.getText().toString(),outputEditText);
            }

        });

        // Get language data from database
        getLanguage();

        /**
         * Populate source language spinner
         */
//        sourceLangSpinner.getSelectedItem().toString();
        sourceLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(sourceLangSpinner.getSelectedItem().toString().equalsIgnoreCase(targetLangSpinner.getSelectedItem().toString())){
                    Log.i("Need to Switch", "same");
                    SwitchLanguages();
                } else {
                    db.setSourceLanguage(sourceLangSpinner.getItemAtPosition(position).toString());
                    spinner_one_list.clear();
                    ArrayList<LanguageClass> source_languages = db.getSourceLanguages();

                    for(int i=0;i<source_languages.size();i++) {

                        String name = source_languages.get(i).getLanguageName();
                        spinner_one_list.add(name);

                    }

                    for(int i=0;i<languageList.size();i++) {
                        String name = languageList.get(i).getLanguageName();
                        if(!isNotExists(name,spinner_one_list)) {
                            spinner_one_list.add(name);
                        }
                    }

                    sourceLangSpinnerAdapter.notifyDataSetChanged();
                    sourceLangSpinner.setSelection(0);
                    //   doTranslation(db.getLanCode(prev_language),db.getLanCode(spinner.getSelectedItem().toString()),sourceEditText.getText().toString(),sourceEditText);
                    //   prev_language = spinner.getSelectedItem().toString();
                    editor.putInt("spinner_one_selected", sourceLangSpinner.getSelectedItemPosition());
                    editor.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        /**
         * Populate target language spinner
         */
        targetLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(sourceLangSpinner.getSelectedItem().toString().equalsIgnoreCase(targetLangSpinner.getSelectedItem().toString())) {
                    Log.i("Need to Switch", "same");
                    SwitchLanguages();
                } else {
                    db.setTargetLanguage(targetLangSpinner.getItemAtPosition(position).toString());
                    spinner_two_list.clear();
                    ArrayList<LanguageClass> target_languages = db.getTargetLanguages();

                    for(int i=0;i<target_languages.size();i++) {
                        String name = target_languages.get(i).getLanguageName();
                        spinner_two_list.add(name);

                    }

                    for(int i=0; i<languageList.size(); i++) {
                        String name = languageList.get(i).getLanguageName();
                        if(!isNotExists(name,spinner_two_list)) {
                            spinner_two_list.add(name);
                        }
                    }

                    targetLangSpinnerAdapter.notifyDataSetChanged();
                    targetLangSpinner.setSelection(0);

                    // TODO Debug this
                    editor.putInt("spinner_two_selected", sourceLangSpinner.getSelectedItemPosition());
                    editor.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        // Create an ArrayAdapter from the String Array
        sourceLangSpinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, spinner_one_list);
        sourceLangSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_item_one);
        targetLangSpinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, spinner_two_list);
        targetLangSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_item_two);
    }

    /**
     * Method to initialize classes/libraries
     */
    private void init() {
        // Shared Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Connection Detector
        connectionDetectorUtil = new ConnectionDetectorUtil(this);

        // Database Helper
        db = new Database(this);

        languageDetailsChecker = new LanguageDetailsChecker();
        Intent detailsIntent = new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
        sendOrderedBroadcast(detailsIntent, null,languageDetailsChecker, null, Activity.RESULT_OK, null, null);

        // Animations
        anim_slide_in_left = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_left);
        anim_slide_in_right = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_right);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.rotateImageView:
                Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_ani);
                rotateImageView.startAnimation(myRotation);
                myRotation.setAnimationListener(new Animation.AnimationListener(){

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                        SwitchLanguages();
                        sourceLangSpinner.startAnimation(anim_slide_in_right);
                        targetLangSpinner.startAnimation(anim_slide_in_left);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                });
                break;

            case R.id.clearImageView:
                // Clear input texts
                sourceEditText.setText("");
                outputEditText.setText("");
                clear = true;
                outputLayout.setVisibility(View.VISIBLE);
                vocaDBLayout.setVisibility(View.GONE);
                controlPanelLayout.setVisibility(View.VISIBLE);
                sourcePanelLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                break;

            default:
                break;
        }
    }

    /**
     * Method to get language from database
     */
    private void getLanguage() {
        languageList = db.getSelectedLanguages();
        ArrayList<LanguageClass> source_languages = db.getSourceLanguages();
        ArrayList<LanguageClass> target_languages = db.getTargetLanguages();

        // Array list of languages to display in the spinner
        spinner_one_list  = new ArrayList<String>();
        spinner_two_list = new ArrayList<String>();

        for(int i=0; i<source_languages.size(); i++) {
            String name = source_languages.get(i).getLanguageName();
            spinner_one_list.add(name);
        }

        for(int i=0; i<target_languages.size(); i++) {
            String name = target_languages.get(i).getLanguageName();
            spinner_two_list.add(name);
        }

        for(int i=0; i<languageList.size(); i++) {
            String name = languageList.get(i).getLanguageName();

            if(!isNotExists(name, spinner_one_list)) {
                spinner_one_list.add(name);
            }

            if(!isNotExists(name, spinner_two_list)) {
                spinner_two_list.add(name);
            }
        }

        /**
         * Debug spinner lists
         */
        Log.d("Language list", languageList.toString());

        // Create an ArrayAdapter from the String Array
        sourceLangSpinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, spinner_one_list);
        sourceLangSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_item_one);
        targetLangSpinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, spinner_two_list);
        targetLangSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_item_two);

        // Apply the adapter to the spinner
        sourceLangSpinner.setAdapter(sourceLangSpinnerAdapter);
        targetLangSpinner.setAdapter(targetLangSpinnerAdapter);
        int fint = prefs.getInt("first", Integer.MAX_VALUE);

        if(fint == Integer.MAX_VALUE){
            sourceLangSpinner.setSelection(0);
            targetLangSpinner.setSelection(1);
            editor.putInt("first", 003);
            editor.commit();
        }
    }

    /**
     * Method to handle language switching
     */
    protected void SwitchLanguages() {
        int position1 = prefs.getInt("spinner_one_selected", 0);
        int position2 = prefs.getInt("spinner_two_selected", 0);

        // Alert user using toast
        Toast.makeText(getApplicationContext(), targetLangSpinner.getItemAtPosition(position1).toString() + " >> " +
                sourceLangSpinner.getItemAtPosition(position2).toString(), Toast.LENGTH_SHORT).show();

        // Open language table
//        languageDataSource.open();

        db.setTargetLanguage(sourceLangSpinner.getItemAtPosition(position1).toString());
        db.setSourceLanguage(targetLangSpinner.getItemAtPosition(position2).toString());

        spinner_one_list.clear();
        spinner_two_list.clear();

        ArrayList<LanguageClass> source_languages = db.getSourceLanguages();
        for (int i=0;i<source_languages.size();i++) {
            String name = source_languages.get(i).getLanguageName();
            spinner_one_list.add(name);
        }

        ArrayList<LanguageClass> target_languages = db.getTargetLanguages();
        for (int i=0;i<target_languages.size();i++) {
            String name = target_languages.get(i).getLanguageName();
            spinner_two_list.add(name);
        }

        for (int i=0; i<languageList.size(); i++) {
            String name = languageList.get(i).getLanguageName();

            if (!isNotExists(name,spinner_one_list)) {
                spinner_one_list.add(name);
            }

            if (!isNotExists(name,spinner_two_list)) {
                spinner_two_list.add(name);
            }
        }

        // Notify adapter for changes
        sourceLangSpinnerAdapter.notifyDataSetChanged();
        targetLangSpinnerAdapter.notifyDataSetChanged();

        sourceLangSpinner.setSelection(0);
        targetLangSpinner.setSelection(0);
    }


    /**
     * Method to check if language dpes not exists in the db
     *
     * @param name
     * @param spinner_list
     * @return
     */
    private boolean isNotExists(String name, ArrayList<String> spinner_list) {
        for(int i=0; i<spinner_list.size(); i++) {
            if(name.equalsIgnoreCase(spinner_list.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch  (item.getItemId()) {
//            case R.id.action_language: {
//                Intent lintent = new Intent(this, LanguageActivity.class);
//                startActivityForResult(lintent, 2);// Activity is started with requestCode 2
//            }
//            return true;
//
            case  R.id.action_color:{
                startActivityForResult(new Intent(this, ColorPickerActivity.class), 3);// Activity is started with requestCode 2
            }
            return true;
//
            case R.id.action_level: {
                startActivity(new Intent(this, LevelActivity.class));// Activity is started with requestCode 2
            }
            return true;
//
//            case R.id.action_history: {
//                new AlertDialog.Builder(this)
//                        .setTitle("Delete History")
//                        .setMessage("Are you sure you want to delete all History?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                db.removeallHistory();
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//            return true;
//
            case R.id.action_about: {
                startActivity(new Intent(this, AboutActivity.class));
            }
            return true;

            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    /**
     * Handles app when back button is pressed twice
     * on Home screen
     */
    @Override
    public void onBackPressed() {
        if (exit) {
            // Finish activity
            finish();
        } else {
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    public void onPause(){
        // Shutdown tts and release resources
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Shutdown tts and release resources
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}