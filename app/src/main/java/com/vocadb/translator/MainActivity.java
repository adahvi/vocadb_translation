package com.vocadb.translator;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.vocadb.translator.adapters.CustomSpinnerAdapter;
import com.vocadb.translator.base.Constants;
import com.vocadb.translator.broadcastreceivers.LanguageDetailsChecker;
import com.vocadb.translator.databasehelpers.Database;
import com.vocadb.translator.models.Idioms;
import com.vocadb.translator.models.LanguageClass;
import com.vocadb.translator.models.Words;
import com.vocadb.translator.utilities.ConnectionDetectorUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Copyright 2016 VocaDB Software Development
 * Created by Adah Valeña on 4/5/2016.
 *
 * Main Activity class
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    protected static final int REQ_CODE_SPEECH_INPUT = 0;

    // List
    private List<String> speechSupportedLangs = new ArrayList<String>();
    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(1);
    private ArrayList<NameValuePair> nameValuePair;
    public ArrayList<LanguageClass> languageList;
    private ArrayList<String> spinner_one_list;
    private ArrayList<String> spinner_two_list;
    private ArrayList<Words> arrWordlist;
    private ArrayList<Idioms> arrIdiomlist;

    // Adapters
    private WordCustomAdapter wordDataAdapter;
    private IdiomsCustomAdapter idiomsDataAdapter;

    // Bools
    private Boolean exit = false;
    protected Boolean clear;
    private Boolean dark;
    protected boolean isPlaying;
    private boolean play = false;
    public boolean loadingWords = true;
    private boolean isFavorite;

    // UI declarations
    private TextView toolbarTitle;
    private ImageView rotateImageView;
    private ImageView voiceInputImageView;
    private ImageView clearImageView;
    private ImageView exeInputImageView;
    private ImageView dbImageView;
    private ImageView favImageView;
    private ImageView expandButton;
    private ImageView shareImageView;
    private ImageView speakerImageView;
    private ImageView listenImageView;

    private EditText sourceEditText;
    private EditText outputEditText;

    private ToggleButton toggleButton;
    private Button wordButton;
    private Button idiomButton;

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

    // Int
    private int color;
    private int wordCount;

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
    private CustomSpinnerAdapter sourceLangSpinnerAdapter;
    private CustomSpinnerAdapter targetLangSpinnerAdapter;

    // Broadcast Receivers
    private LanguageDetailsChecker languageDetailsChecker;

    // Aquery library
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Call init method
        init();

        // UI References
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        rotateImageView = (ImageView) findViewById(R.id.rotateImageView);
        voiceInputImageView = (ImageView) findViewById(R.id.voiceInputImageView);
        speakerImageView = (ImageView)findViewById(R.id.speakerImageView);
        listenImageView = (ImageView)findViewById(R.id.listenImageView);
        clearImageView = (ImageView) findViewById(R.id.clearImageView);
        exeInputImageView = (ImageView) findViewById(R.id.exeInputImageView);
        dbImageView = (ImageView)findViewById(R.id.dbImageView);
        favImageView = (ImageView)findViewById(R.id.favImageView);
        shareImageView = (ImageView)findViewById(R.id.shareImageView);

        toggleButton = (ToggleButton)findViewById(R.id.toggle_button);
        wordButton = (Button)findViewById(R.id.words_button);
        idiomButton = (Button)findViewById(R.id.idoms_button);
        expandButton = (ImageView)findViewById(R.id.expand_button);

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
        dbImageView.setOnClickListener(this);
        rotateImageView.setOnClickListener(this);
        clearImageView.setOnClickListener(this);
        favImageView.setOnClickListener(this);
        shareImageView.setOnClickListener(this);

        // Set the theme
        color = prefs.getInt("color", Constants.defaultColor);
        setTheme(isColorDark(color));

//        isPlaying = false;
        mediaPlayer.setScreenOnWhilePlaying(true);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                mp.reset();
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp1) { // TODO
                mediaPlayer.start();
            }
        });

        // Words Data Adapter
        wordDataAdapter = new WordCustomAdapter(this, R.layout.row_word, arrWordlist);
        final DynamicListView wordlistView = (DynamicListView) findViewById(R.id.wordslistView);
        // Assign adapter to ListView
        wordlistView.setAdapter(wordDataAdapter);
        wordlistView.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            wordDataAdapter.remove(position);
                        }
                        wordDataAdapter.notifyDataSetChanged();
                    }
                }
        );

        // Idioms Data Adapter
        idiomsDataAdapter = new IdiomsCustomAdapter(this, R.layout.row_idiom, arrIdiomlist);
        final DynamicListView idomslistView = (DynamicListView) findViewById(R.id.idiomslistView);
        idomslistView.setAdapter(idiomsDataAdapter);
        idomslistView.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            idiomsDataAdapter.remove(position);
                        }
                        idiomsDataAdapter.notifyDataSetChanged();
                    }
                }
        );

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Collections.sort(arrWordlist, new Comparator<Words>() {
                        @Override
                        public int compare(
                                Words lhs, Words rhs) {
                            // TODO Auto-generated method stub
                            return lhs.getVoca().compareToIgnoreCase(rhs.getVoca());
                        }
                    });
                    Collections.sort(arrIdiomlist, new Comparator<Idioms>() {

                        @Override
                        public int compare(
                                Idioms lhs, Idioms rhs) {
                            // TODO Auto-generated method stub
                            return lhs.getVoca().compareToIgnoreCase(rhs.getVoca());
                        }

                    });
                    wordDataAdapter = new WordCustomAdapter(getApplicationContext(),
                            R.layout.row_word, arrWordlist);

                    wordlistView.setAdapter(wordDataAdapter);
                    idiomsDataAdapter = new IdiomsCustomAdapter(getApplicationContext(),
                            R.layout.row_idiom, arrIdiomlist);
                    idomslistView.setAdapter(idiomsDataAdapter);
                } else {
                    Collections.sort(arrWordlist, new Comparator<Words>() {

                        @Override
                        public int compare(
                                Words lhs, Words rhs) {
                            // TODO Auto-generated method stub
                            return rhs.getilevel()-lhs.getilevel();
                        }

                    });
                    Collections.sort(arrIdiomlist, new Comparator<Idioms>() {

                        @Override
                        public int compare(
                                Idioms lhs, Idioms rhs) {
                            // TODO Auto-generated method stub
                            return rhs.getiLevel()-lhs.getiLevel();
                        }

                    });
                    wordDataAdapter = new WordCustomAdapter(getApplicationContext(),
                            R.layout.row_word, arrWordlist);
                    wordlistView.setAdapter(wordDataAdapter);
                    idiomsDataAdapter = new IdiomsCustomAdapter(getApplicationContext(),
                            R.layout.row_idiom, arrIdiomlist);
                    idomslistView.setAdapter(idiomsDataAdapter);
                }
            }
        });

        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                outputLayout.setVisibility(View.VISIBLE);
                vocaDBLayout.setVisibility(View.GONE);
                controlPanelLayout.setVisibility(View.VISIBLE);
                sourcePanelLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

            }
        });

        wordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordButton.setEnabled(false);
                idiomButton.setEnabled(true);
                wordlistView.setVisibility(View.VISIBLE);
                idomslistView.setVisibility(View.GONE);
            }
        });

        idiomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordButton.setEnabled(true);
                idiomButton.setEnabled(false);
                wordlistView.setVisibility(View.GONE);
                idomslistView.setVisibility(View.VISIBLE);
            }
        });

        voiceInputImageView.setOnClickListener(this);
        speakerImageView.setOnClickListener(this);
        listenImageView.setOnClickListener(this);

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
        sourceLangSpinner.getSelectedItem().toString();
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

        checkIntent();

        exeInputImageView.setOnClickListener(this);
    }

    /**
     * Method to initialize classes/libraries
     */
    private void init() {
        // Initialize aQuery library
        aq = new AQuery(this);

        // Shared Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Editor
        editor = prefs.edit();
        speechSupportedLangs.add("en");

        // Connection Detector
        connectionDetectorUtil = new ConnectionDetectorUtil(this);

        // Database Helper
        db = new Database(this);

        // Media Player
        mediaPlayer = new MediaPlayer();

        languageDetailsChecker = new LanguageDetailsChecker();
        Intent detailsIntent = new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
        sendOrderedBroadcast(detailsIntent, null,languageDetailsChecker, null, Activity.RESULT_OK, null, null);

        // Array list of languages to display in the spinner
        spinner_one_list  = new ArrayList<String>();
        spinner_two_list = new ArrayList<String>();

        arrWordlist = new ArrayList<Words>();
        arrIdiomlist = new ArrayList<Idioms>();

        // Animations
        anim_slide_in_left = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_left);
        anim_slide_in_right = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_right);
    }

    private void checkIntent() {
        Intent in = getIntent();
        if(in!= null)
        {
            Log.i("Got Dat", "history");
            String inextra = in.getStringExtra("inextra");
            if(inextra != null)
            {
                if(inextra.equals("init_mic"))
                {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,db.getLanCode(sourceLangSpinner.getSelectedItem().toString()));
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speech");
                    try {
                        startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                    } catch (ActivityNotFoundException a) {
                        Toast.makeText(getApplicationContext(), "Speech Text Not Supported",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if(inextra.equals("history")) {
                    String sl = in.getStringExtra("sl");
                    String tl = in.getStringExtra("tl");
                    String sl_data = in.getStringExtra("sl_data");
                    String tl_data = in.getStringExtra("tl_data");
                    isFavorite = in.getBooleanExtra("is_fav", false);
                    getApiData(sl,tl,sl_data);

                    Log.i("Got Dat", "sl_data"+sl_data);
                    Log.i("Got Dat", "tl_data"+tl_data);
                    sourceEditText.setText(sl_data);
                    outputEditText.setText(tl_data);
                    sourceLangSpinner.setSelection(spinner_one_list.indexOf(db.getLanName(sl)));
                    targetLangSpinner.setSelection(spinner_two_list.indexOf(db.getLanName(tl)));
                }
            }
        }
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

            case R.id.exeInputImageView:
                Log.d("click", "translate");
                getApiData(db.getLanCode(sourceLangSpinner.getSelectedItem().toString()),db.getLanCode(targetLangSpinner.getSelectedItem().toString()),sourceEditText.getText().toString());
                clear = false;
                controlPanelLayout.setVisibility(View.VISIBLE);
                outputLayout.setVisibility(View.VISIBLE);
                vocaDBLayout.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sourceEditText.getWindowToken(), 0);
                doTranslation(db.getLanCode(sourceLangSpinner.getSelectedItem().toString()),db.getLanCode(targetLangSpinner.getSelectedItem().toString()),sourceEditText.getText().toString(),outputEditText);
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

            case R.id.dbImageView:
                if(arrWordlist.size()>0 ||arrIdiomlist.size()>0) {
                    outputLayout.setVisibility(View.GONE);
                    vocaDBLayout.setVisibility(View.VISIBLE);
                    controlPanelLayout.setVisibility(View.GONE);
                    sourcePanelLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200));
                    vocaDBLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.FILL_PARENT));
                } else {
                    if(loadingWords) {
                        Toast.makeText(getApplicationContext(), "Wait Getting word list from Server", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "No result of word list", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.favImageView:
                if(isFavorite == true) {
                    isFavorite = false;
                    if((!sourceEditText.getText().toString().equals(""))&&(!outputEditText.getText().toString().equals(""))) {
                        favImageView.setImageResource(R.drawable.ic_fav);
                        //db.addNewHistory(new History(history_id,db.getLanCode(spinner.getSelectedItem().toString()),db.getLanCode(spinner2.getSelectedItem().toString()),sourceEditText.getText().toString(),outputEditText.getText().toString(),is_fav,String.valueOf(System.currentTimeMillis())));
                    }
                } else {
                    if((!sourceEditText.getText().toString().equals(""))&&(!outputEditText.getText().toString().equals(""))) {
                        favImageView.setImageResource(R.drawable.ic_fav_light);
                        isFavorite = true;
                        //db.addNewHistory(new History(history_id,db.getLanCode(spinner.getSelectedItem().toString()),db.getLanCode(spinner2.getSelectedItem().toString()),sourceEditText.getText().toString(),outputEditText.getText().toString(),is_fav,String.valueOf(System.currentTimeMillis())));
                    }}
                break;

            case R.id.shareImageView:
                if(!sourceEditText.getText().toString().matches("") && !outputEditText.getText().toString().matches("")) {
                    String shareBody = "Translated Text from vocaDB translator \n"+sourceEditText.getText().toString()+"\n\n"+outputEditText.getText().toString();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Voice Translator");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent,"Share via"));
                }
                break;

            case R.id.voiceInputImageView:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,db.getLanCode(sourceLangSpinner.getSelectedItem().toString()));
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speech");
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),"Speech Text Not Supported",
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.speakerImageView:
                wordCount = countWords(sourceEditText.getText().toString());
                Log.d("Word count", String.valueOf(wordCount));
                // Check first if tts is in play/pause mode
                if (!play) {
                    speakerImageView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                    play = true;
                    textToSpeech = new TextToSpeech(getApplicationContext(),
                            new TextToSpeech.OnInitListener() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onInit(int status) {
                                    Log.d("MP Status", String.valueOf(status));
                                    if(status != TextToSpeech.ERROR){
                                        Locale locale = new Locale(db.getLanCode(sourceLangSpinner.getSelectedItem().toString()));
                                        textToSpeech.setLanguage(locale);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            ttsGreater21(sourceEditText.getText().toString());
                                        } else {
                                            ttsUnder20(sourceEditText.getText().toString());
                                        }
                                    }
                                }
                            });
                } else {
                    textToSpeech.stop();
                    speakerImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_speaker));
                    play = false;
                }
                break;

            case R.id.listenImageView:
                wordCount = countWords(outputEditText.getText().toString());
                if(wordCount >15) {
                    textToSpeech = new TextToSpeech(getApplicationContext(),
                            new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if(status != TextToSpeech.ERROR){
                                        Locale locale = new Locale(db.getLanCode(targetLangSpinner.getSelectedItem().toString()));
                                        textToSpeech.setLanguage(locale);
                                        textToSpeech.speak(outputEditText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                }
                            });

                } else {
                    if (isInternetPresent()) {
                        if (isPlaying) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }

                        try {
                            isPlaying = true;
                            mediaPlayer.setDataSource("http://vocadb.co.kr/v2_voca/api_vocadb_trans.php?apikey=3065&slang=en&tlang="
                                    + db.getLanCode(sourceLangSpinner.getSelectedItem().toString())
                                    + "&engin=0&q="
                                    + outputEditText.getText().toString());
                            mediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            Log.i("error", "prepare() failed");
                        }
                    } else {
                        textToSpeech = new TextToSpeech(getApplicationContext(),
                                new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int status) {
                                        if(status != TextToSpeech.ERROR){
                                            Locale locale = new Locale(db.getLanCode(targetLangSpinner.getSelectedItem().toString()));
                                            textToSpeech.setLanguage(locale);
                                            textToSpeech.speak(outputEditText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    }
                                });


                    }}
                break;

            default:
                break;
        }
    }

    private Boolean isInternetPresent() {
        return connectionDetectorUtil.isConnectingToInternet();
    }

    protected void getApiData(String slanCode, String tlanCode, String query) {
        int level = prefs.getInt("level", 21);
        nameValuePair = new ArrayList<NameValuePair>(20);
        nameValuePair.add(new BasicNameValuePair("apikey",Constants.DICTIONARY_API_KEY));

        nameValuePair.add(new BasicNameValuePair("slang",slanCode));
        nameValuePair.add(new BasicNameValuePair("tlang",tlanCode));
        nameValuePair.add(new BasicNameValuePair("level",String.valueOf(level)));
        nameValuePair.add(new BasicNameValuePair("q",query));

        if (isInternetPresent()) {
            new GetApiDataTask().execute();
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
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

        sourceLangSpinnerAdapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, spinner_one_list);
        sourceLangSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_item_one);
        targetLangSpinnerAdapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, spinner_two_list);
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

    protected void doTranslation(String from_ln, String to_ln, String source, EditText outputEditText) {
        if (isInternetPresent()) {
            new TranslationTask(from_ln,to_ln,source,outputEditText).execute();
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to count number of words
     *
     * @param s
     * @return
     */
    private static int countWords(String s){
        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    private class TranslationTask extends AsyncTask<String, String, JSONObject> {

        private String from_ln;
        private String to_ln;
        private String source;
        private EditText targetEditText;


        public TranslationTask(String from_ln, String to_ln, String source, EditText outputEditText) {
            this.from_ln = from_ln;
            this.to_ln = to_ln;
            this.source = source;
            this.targetEditText= outputEditText;
        }

        @Override
        protected void onPreExecute() {
            targetEditText.setText("Translating.. Please wait..");
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String x = "";
            try {
                x = Constants.TRANSLATOR_API_URL +
                        "?apikey=" + Constants.TRANSLATOR_API_KEY +
                        "&q="
                        + URLEncoder.encode(source, "UTF-8")
                        + "&engin=0"
                        + "&slang=" +from_ln
                        + "&tlang=" +to_ln;

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JSONObject json = jParser.getTransFromUrl(x, params);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            String result1 = "";
            try {
                JSONObject item = json.getJSONObject("sentences");
                result1 = item.getString("trans");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            targetEditText.setText(result1);
            super.onPostExecute(json);
        }

    }

//
//    // Get translation from API
//    private void getTranslation() throws UnsupportedEncodingException {
//        AqHelper aqHelper = new AqHelper(MainActivity.this);
//        AjaxCallback<JSONObject> cb = aqHelper.ajaxCallback(
//                Constants.TRANSLATOR_API_URL +
//                        "?apikey=" +
//                        Constants.TRANSLATOR_API_KEY +
//                        "&q=" +
//                        URLEncoder.encode(sourceEditText.getText().toString(), "UTF-8") +
//                        "&engin=0" +
//                        "&slang=" + db.getLanCode(sourceLangSpinner.getSelectedItem().toString()) +
//                        "&tlang=" + db.getLanCode(targetLangSpinner.getSelectedItem().toString()),
//                "translationCb", Constants.CACHE_RESET);
//        aq.ajax(cb);
//    }
//
//    /**
//     * Parse json translation object
//     *
//     * @param _url
//     * @param _jo
//     * @param _status
//     * @throws org.json.JSONException
//     */
//    public void translationCb(String _url, JSONObject _jo, AjaxStatus _status)
//            throws JSONException {
//        Log.d("Translation", _jo.toString());
//        AQUtility.debug("Translation", _jo);
////        // Check if data is not null or empty
////        if (_jo.has("data") && !_jo.isNull("data")) {
////            Object data = _jo.get("data");
////            // Check if object is array or object
////            if (data instanceof JSONArray) {
////                // Hide progressbar when adapter is set
////                relativeLayout.setVisibility(View.GONE);
////                programsSwipeLayout.setRefreshing(false);
////                // Show empty text
////                emptyView.setVisibility(View.VISIBLE);
////            } else {
////                JSONObject _joData = _jo.getJSONObject("data");
////                JSONArray _ja = _joData.getJSONArray("programs");
////                if (_status.getCode() == 200) {
////                    AQUtility.debug("Program Source", _status.getSource());
////                    program = new ArrayList<Program>();
////                    program = programParser.parse(_ja);
////
////                    // Check if program is empty
////                    if (program.size() == 0 && program.isEmpty()) {
////                        // Show empty text
////                        emptyView.setVisibility(View.VISIBLE);
////                    } else {
////                        // Pass values to adapter
////                        programAdapter = new ProgramAdapter(ProgramActivity.this, program);
////                        // Set adapter to listview
////                        programListView.setAdapter(programAdapter);
////                        // Notify adapter that there are changes on the list
////                        programAdapter.notifyDataSetChanged();
////
////                        // Hide progressbar when adapter is set
////                        relativeLayout.setVisibility(View.GONE);
////                        programsSwipeLayout.setRefreshing(false);
//
//                        // Item Click Listener
//                        programListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view,
//                                                    int position, long id) {
//                                // ListView Clicked item index
//                                int itemPosition = position;
//                                Intent intent = new Intent(ProgramActivity.this, ProgramDetailsActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putStringArray("program_details", new String[]{
//                                        program.get(position).getId(),
//                                        program.get(position).getTitle(),
//                                        program.get(position).getBanner(),
//                                        program.get(position).getDate(),
//                                        program.get(position).getTime(),
//                                        program.get(position).getLocation(),
//                                        program.get(position).getVenue(),
//                                        program.get(position).getDetails(),
//                                        program.get(position).getSpeaker(),
//                                        program.get(position).getPosition(),
//                                        program.get(position).getCompany(),
//                                        program.get(position).getDescription()
//                                });
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                            }
//                        });
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), _status.getMessage(), Toast.LENGTH_LONG).show();
//                    AQUtility.debug("Error", _status.getCode() + " " + "Status:"
//                            + _status.getMessage());
//                }
//            }
//        }
//    }

    /**
     * Method to check if language dpes not exists in the db
     *
     * @param name
     * @param spinner_list
     * @return
     */
    private boolean isNotExists(String name, ArrayList<String> spinner_list) {
        for(int i=0; i<spinner_list.size(); i++) {
            if(name.equalsIgnoreCase(spinner_list.get(i)))
                return true;
        }
        return false;
    }

    /**
     * Method to check if theme is light or dark
     *
     * @param color
     * @return
     */
    private boolean isColorDark(int color) {
        double a = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if (a < 0.5){
            return false; // Light color
        } else {
            return true; // Dark color
        }
    }

    /**
     * Method to set the app's theme
     *
     * @param dark
     */
    private void setTheme(boolean dark) {
        headerLayout.setBackgroundColor(color);
        outputEditText.setBackgroundColor(color);
        if(dark) {
            toolbarTitle.setTextColor(color);
            outputEditText.setTextColor(Color.WHITE);
            this.setTheme(android.R.style.Theme_Material);
            rotateImageView.setImageResource(R.drawable.ic_switch);
        } else {
            toolbarTitle.setTextColor(color);
            outputEditText.setTextColor(Color.BLACK);
            this.setTheme(android.R.style.Theme_Material_Light);
            rotateImageView.setImageResource(R.drawable.ic_switch_light);
        }
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
            case R.id.action_language: {
                startActivityForResult(new Intent(this, LanguageActivity.class), 2);
            }
            return true;

            case  R.id.action_color:{
                startActivityForResult(new Intent(this, ColorPickerActivity.class), 3); // Activity is started with requestCode 3
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    sourceEditText.setText(result.get(0));
                    doTranslation(db.getLanCode(sourceLangSpinner.getSelectedItem().toString()), db.getLanCode(targetLangSpinner.getSelectedItem().toString()), sourceEditText.getText().toString(), outputEditText);
                }
                break;

            case 2:
                getLanguage();
                break;

            case 3:
                color =	data.getIntExtra("color", R.color.Custom_RED);
                dark = isColorDark(color);
                editor.putInt("color", color);
                editor.commit();
                setTheme(dark);
                getLanguage();
                break;
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

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    private class GetApiDataTask extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            loadingWords = true;
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            new ServiceHandler();
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            if(arrWordlist.size()>0) arrWordlist.clear();
            if(arrIdiomlist.size()>0) arrIdiomlist.clear();

            Log.i("appsmithing-->", "xxx");
            JSONObject json = jParser.getJSONFromUrl(Constants.DICTIONARY_API_URL, nameValuePair);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if(json != null){
                Log.i("res", ""+json.toString());
                try {
                    JSONObject data = (json.getJSONObject("contents")).getJSONObject("words");
                    JSONArray relative_words = data.getJSONArray("relative_words");
                    for (int i = 0; i < relative_words.length(); i++) {
                        JSONObject item = relative_words.getJSONObject(i);
                        Words w1 = new Words(item.getString("voca"), " - "+item.getString("part")+" / ",item.getString("level"),item.getString("means"),item.getInt("ilevel"));
                        Log.i("get ilevel", "="+item.getInt("ilevel"));
                        if(WordNotExists(item.getString("voca"))) {
                            boolean isdeleted = db.checkWordDeleted(item.getString("voca"));
                            if(!isdeleted) {
                                arrWordlist.add(w1);
                            }
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    	e.printStackTrace();
                }

                try {
                    JSONObject data = (json.getJSONObject("contents")).getJSONObject("idioms");
                    JSONArray relative_idioms = data.getJSONArray("relative_idioms");

                    for (int i = 0; i < relative_idioms.length(); i++) {
                        JSONObject item = relative_idioms.getJSONObject(i);
                        Idioms i1 = new Idioms(item.getString("idiom"), item.getString("level"),item.getString("means"),item.getInt("ilevel"));

                        if(IdiomNotExists(item.getString("idiom"))) {
                            boolean isdeleted = db.checkIdomDeleted(item.getString("idiom"));
                            if(!isdeleted) {
                                arrIdiomlist.add(i1);
                            }
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            Collections.sort(arrWordlist, new Comparator<Words>() {

                @Override
                public int compare(
                        Words lhs, Words rhs) {
                    // TODO Auto-generated method stub
                    return rhs.getilevel()-lhs.getilevel();
                }

            });

            Collections.sort(arrIdiomlist, new Comparator<Idioms>() {

                @Override
                public int compare(
                        Idioms lhs, Idioms rhs) {
                    // TODO Auto-generated method stub
                    return rhs.getiLevel()-lhs.getiLevel();
                }

            });

            if(arrWordlist.size()>0) {
                wordButton.setText("Words("+arrWordlist.size()+")");
                wordDataAdapter.notifyDataSetChanged();
                wordButton.setVisibility(View.VISIBLE);
            } else {
                wordButton.setVisibility(View.GONE);
            }

            if(arrIdiomlist.size()>0) {
                idiomButton.setText("Idioms("+arrIdiomlist.size()+")");
                idiomsDataAdapter.notifyDataSetChanged();
                idiomButton.setVisibility(View.VISIBLE);
            } else {
                idiomButton.setVisibility(View.GONE);
            }

            if(arrWordlist.size()>0 || arrIdiomlist.size()>0) {
                toggleButton.setVisibility(View.VISIBLE);
            } else {
                toggleButton.setVisibility(View.GONE);
            }

            loadingWords = false;
            super.onPostExecute(json);
        }

    }

    public boolean WordNotExists(String voca) {
        for(int i=0;i<arrWordlist.size();i++) {
            String voca_name = arrWordlist.get(i).getVoca();
            if(voca.equalsIgnoreCase(voca_name)) {
                return false;
            }
        }
        return true;

    }

    public boolean IdiomNotExists(String voca) {
        for(int i=0;i<arrIdiomlist.size();i++) {
            String voca_name = arrIdiomlist.get(i).getVoca();
            if(voca.equalsIgnoreCase(voca_name)) {
                return false;
            }
        }
        return true;
    }

    private class WordCustomAdapter extends ArrayAdapter<Words> {

        private ArrayList<Words> adpwList;


        public WordCustomAdapter(Context context, int textViewResourceId,
                                 ArrayList<Words> adtawordsList) {
            super(context, textViewResourceId, adtawordsList);
            this.adpwList = adtawordsList;
        }

        public void remove(int position) {
            db.deleteWord(adpwList.get(position));
            adpwList.remove(position);
        }

        private class ViewHolder {

            public TextView voctextView;
            public TextView meaningtextView;
            public TextView tv_leveltextView;
            public TextView tv_parttextView;
            public ImageView iv_circle;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Words words;
            // Log.v("ConvertView", String.valueOf(position));

            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.row_word, null);

                holder = new ViewHolder();
                holder.voctextView = (TextView) convertView.findViewById(R.id.tv_voca);
                holder.meaningtextView = (TextView) convertView.findViewById(R.id.bottomtext);
                holder.tv_leveltextView = (TextView) convertView.findViewById(R.id.tv_level);
                holder.tv_parttextView = (TextView) convertView.findViewById(R.id.tv_part);
                holder.iv_circle = (ImageView) convertView.findViewById(R.id.iv_circle);
                convertView.setTag(holder);


            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position % 2 == 0) {
                convertView.setBackgroundResource(R.color.list_item);
            } else {
                convertView.setBackgroundResource(R.color.White);
            }
            words = adpwList.get(position);
            holder.voctextView.setText(words.getVoca());
            holder.meaningtextView.setText(words.getTLData());
            holder.tv_leveltextView.setText(words.getLevel());
            holder.tv_parttextView.setText(words.getPart());
            boolean isexists = db.checkWordStored(words.getVoca());
            if(isexists)
            {
                holder.iv_circle.setImageResource(R.drawable.ic_red_circle);
            }
            else
            {
                holder.iv_circle.setImageResource(R.drawable.ic_grey_circle);

            }

            holder.iv_circle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean isexists = db.checkWordStored(words.getVoca());

                    if(isexists)
                    {
                        db.UnreadWord(words.getVoca());
                        ((ImageView)v).setImageResource(R.drawable.ic_grey_circle);
                    }
                    else
                    {
                        db.addReadWord(words);
                        ((ImageView)v).setImageResource(R.drawable.ic_red_circle);
                    }


                }
            });

            holder.voctextView.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    if (isInternetPresent()) {
                        if (isPlaying) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }

                        try {
                            isPlaying = true;
                            mediaPlayer.setDataSource(Constants.MEDIA_URL + words.getVoca().toLowerCase() + ".mp3");
                            mediaPlayer.prepareAsync();
                        } catch (IllegalArgumentException e) {
                            isPlaying = false;
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            isPlaying = false;
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            isPlaying = false;
                            e.printStackTrace();
                        } catch (IOException e) {
                            isPlaying = false;
                            e.printStackTrace();
                        }
                    } else {
                        textToSpeech = new TextToSpeech(getApplicationContext(),
                                new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int status) {
                                        if(status != TextToSpeech.ERROR){
                                            Locale locale = new Locale(db.getLanCode(sourceLangSpinner.getSelectedItem().toString()));
                                            textToSpeech.setLanguage(locale);
                                            textToSpeech.speak(words.getVoca(), TextToSpeech.QUEUE_FLUSH, null);

                                        }
                                    }
                                });
                    }
                }
            });
            return convertView;
        }
    }

    private class IdiomsCustomAdapter extends ArrayAdapter<Idioms>{

        private ArrayList<Idioms> adIdiomlist;

        public IdiomsCustomAdapter(Context context, int textViewResourceId,
                                   ArrayList<Idioms> arrIdiomlist) {
            super(context, textViewResourceId, arrIdiomlist);
            this.adIdiomlist = arrIdiomlist;

        }

        public void remove(int position) {
            db.deleteIdiom(adIdiomlist.get(position));
            adIdiomlist.remove(position);
        }

        private class ViewHolder {

            public TextView voctextView;
            public TextView meaningtextView;
            public TextView tv_leveltextView;
            public ImageView iv_circle;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Idioms  idioms = adIdiomlist.get(position);

            ViewHolder holder = null;
            // Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.row_idiom, null);

                holder = new ViewHolder();
                holder.voctextView = (TextView) convertView.findViewById(R.id.tv_voca);
                holder.meaningtextView = (TextView) convertView.findViewById(R.id.bottomtext);
                holder.tv_leveltextView = (TextView) convertView.findViewById(R.id.tv_level);
                holder.iv_circle = (ImageView) convertView.findViewById(R.id.iv_circle);


                boolean isexists = db.checkIdomStored(idioms.getVoca());
                if(isexists)
                {
                    holder.iv_circle.setImageResource(R.drawable.ic_red_circle);
                }
                else
                {
                    holder.iv_circle.setImageResource(R.drawable.ic_grey_circle);

                }
                convertView.setTag(holder);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position % 2 == 0) {
                convertView.setBackgroundResource(R.color.list_item);
            } else {
                convertView.setBackgroundResource(R.color.White);
            }

            holder.voctextView.setText(idioms.getVoca());
            holder.meaningtextView.setText(idioms.getTLData());
            holder.tv_leveltextView.setText(idioms.getLevel());
            holder.iv_circle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean isexists = db.checkIdomStored(idioms.getVoca());

                    if(isexists)
                    {
                        db.UnreadIdiom(idioms.getVoca());
                        ((ImageView)v).setImageResource(R.drawable.ic_grey_circle);
                    }
                    else
                    {
                        db.addReadIdiom(idioms);
                        ((ImageView)v).setImageResource(R.drawable.ic_red_circle);
                    }
                }
            });

            holder.voctextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isPlaying) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }

                    try {
                        isPlaying = true;
                        mediaPlayer.setDataSource(Constants.MEDIA_URL + idioms.getVoca().toLowerCase() + ".mp3");
                        mediaPlayer.prepareAsync();
                    } catch (IllegalArgumentException e) {
                        isPlaying = false;
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        isPlaying = false;
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        isPlaying = false;
                        e.printStackTrace();
                    } catch (IOException e) {
                        isPlaying = false;
                        e.printStackTrace();
                    }


                }
            });
            return convertView;
        }
    }

}