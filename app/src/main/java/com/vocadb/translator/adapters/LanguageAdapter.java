package com.vocadb.translator.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vocadb.translator.LanguageActivity;
import com.vocadb.translator.R;

import java.util.List;

/**
 * Copyright 2016 VocaDB Software Development
 * Created by Adah Valeña on 4/5/2016.
 *
 * Language Adapter class
 */
public class LanguageAdapter extends BaseAdapter {

    Activity activity;

    LanguageActivity languageActivity;

    String[] languageList;

    private LayoutInflater layoutInflater;

    public LanguageAdapter(LanguageActivity _languageActivity) {
        this.languageActivity = _languageActivity;
    }

    public LanguageAdapter(Activity _activity, String[] _languageList) {
        super();
        this.activity = _activity;
        this.languageList = _languageList;
        layoutInflater = LayoutInflater.from(_activity);
    }

    @Override
    public int getCount() {
        return languageList.length;
    }

    @Override
    public Object getItem(int position) {
        return languageList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView txtLanguageName;
        CheckBox cbLanguage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Initialize ViewHolder
        final ViewHolder mHolder;
        // Get language by position
        String lang = languageList[position];
        // If convertView is null
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.list_language, null, false);
            mHolder.txtLanguageName = (TextView) convertView.findViewById(R.id.language_name);
            mHolder.cbLanguage = (CheckBox) convertView.findViewById(R.id.language_checkbox);
            // Set tag to viewholder
            convertView.setTag(mHolder);
        } else {
            // View holder gettag
            mHolder = (ViewHolder) convertView.getTag();
        }

        // Set value to language text
        mHolder.txtLanguageName.setText(lang);

        return convertView;
    }
}
