package com.vocadb.translator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vocadb.translator.databasehelpers.Database;
import com.vocadb.translator.databasehelpers.LanguageDataSource;
import com.vocadb.translator.models.Language;
import com.vocadb.translator.models.LanguageClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private List<Language> languages;

    private LanguageDataSource languageDataSource;

    private Database db;

    String localeLanguage, defaultLanguage;

    public LanguageClass languageClass;
    public ArrayList<LanguageClass> languageList;
    public Intent in;
    private String my_language;
    private String my_language_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Call init method
        init();

//        // Get default language
//        defaultLanguage =	Locale.getDefault().getDisplayLanguage().toString();
//
//        // Save value to shared pref
//        localeLanguage =	Locale.getDefault().getDisplayLanguage().toString();
//        defaultLanguage = prefs.getString("default_language", "");
//        editor.putString("default_language", defaultLanguage);
//        editor.commit();
//
//        // Open datasource
//        languageDataSource.open();
//        languages = languageDataSource.getAll();
//        languageDataSource.close();
//
//        // Check if language table is empty
//        if (languages.size() == 0 || !localeLanguage.equalsIgnoreCase(defaultLanguage)) {
//
//        } else {
//
//        }

        languageList = new ArrayList<LanguageClass>();
        in = new Intent(this,MainActivity.class);
        ArrayList<LanguageClass> my_languages = db.getMyLanguages();
        my_language =	Locale.getDefault().getDisplayLanguage().toString();
        my_language_code =	Locale.getDefault().getLanguage().toString();
        String default_language = prefs.getString("default_language", "");
        editor.putString("default_language", my_language);
        editor.commit();

        if((my_languages.size()==0) || (!my_language.equalsIgnoreCase(default_language))) {
            new UpdateLanguagesTask().execute();
        } else {
            startActivity(in);
            finish();
        }
    }

    /**
     * Method to initialize classes/libraries
     */
    private void init() {
        // Shared Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        editor = prefs.edit();

        // Init datasource
        languageDataSource = new LanguageDataSource(this);

        db = new Database(this);
    }


    private class UpdateLanguagesTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {

            languageClass = new LanguageClass(my_language,my_language_code,true);
            //  languageList.add(LanguageClass);
            languageClass = new LanguageClass("Afrikaans","af",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Albanian","sq",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Arabic","ar",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Azerbaijani","az",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Basque","eu",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Bengali","bn",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Belarusian","be",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Bulgarian","bg",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Catalan","ca",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Chinese Simplified","zh-CN",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Chinese Traditional","zh-TW",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Croatian","hr",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Czech","cs",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Danish","da",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Dutch","nl",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("English","en",true);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Esperanto","eo",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Estonian","et",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Filipino","tl",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Finnish","fi",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("French","fr",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Galician","gl",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Georgian","ka",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("German","de",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Greek","el",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Gujarati","gu",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Haitian Creole","ht",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Hebrew","iw",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Hindi","hi",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Hungarian","hu",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Icelandic","is",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Indonesian","id",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Irish","ga",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Italian","it",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Japanese","ja",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Kannada","kn",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Korean","ko",true);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Latin","la",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Latvian","lv",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Lithuanian","lt",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Macedonian","mk",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Malay","ms",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Maltese","mt",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Norwegian","no",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Persian","fa",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Polish","pl",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Portuguese","pt",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Romanian","ro",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Russian","ru",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Serbian","sr",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Slovak","sk",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Slovenian","sl",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Spanish","es",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Swahili","sw",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Swedish","sv",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Tamil","ta",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Telugu","te",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Thai","th",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Turkish","tr",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Ukrainian","uk",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Urdu","ur",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Vietnamese","vi",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Welsh","cy",false);
            languageList.add(languageClass);
            languageClass = new LanguageClass("Yiddish","yi",false);
            languageList.add(languageClass);

            db.removeallLanguage();
            for( int i=0;i<languageList.size();i++){
                if(!db.checkLangauageExists(languageList.get(i).getLanguageName())) {
                    db.addLanguage(languageList.get(i));
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }


    }

//    private class GetLanguagesTask extends AsyncTask<String, String, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//            finish();
//        }
//    }
}
