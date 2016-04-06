package com.vocadb.translator.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adah Valeña on 4/6/2016.
 */
public class LanguageDetailsChecker extends BroadcastReceiver
{

    // List
    private List<String> speechSupportedLangs = new ArrayList<String>();

    public String languagePreference;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("Broadcast", "Supported languages");
        ArrayList<String> temp = new  ArrayList<String>();
        Bundle results = getResultExtras(true);
        if (results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
            languagePreference = results.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE);
        }

        if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
            temp = results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);

            for(int i = 0; i< temp.size(); i++) {
                speechSupportedLangs.add(temp.get(i).split("-")[0]);
            }
        }
    }
}
