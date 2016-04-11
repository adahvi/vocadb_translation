package com.vocadb.translator.databasehelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;

import com.vocadb.translator.models.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 VocaDB Software Development
 * Created by Adah Valeña on 4/5/2016.
 *
 * An interface for the creation of Connection objects which represent a connection to a database.
 */
public class LanguageDataSource {

    private SQLiteDatabase database;
    private VocaDBHelper dbHelper;

    private String[] allColumns = {
            VocaDBHelper.LANG_KEY_ID,
            VocaDBHelper.LANG_KEY_NAME,
            VocaDBHelper.LANG_KEY_CODE,
            VocaDBHelper.LANG_KEY_STATE,
            VocaDBHelper.LANG_KEY_SL_STATE,
            VocaDBHelper.LANG_KEY_SL_STATE_TIME,
            VocaDBHelper.LANG_KEY_TL_STATE,
            VocaDBHelper.LANG_KEY_TL_STATE_TIME
    };

    public LanguageDataSource(Context _context) {
        dbHelper = new VocaDBHelper(_context);
    }

    public LanguageDataSource(SQLiteDatabase _database) {
        database = _database;
    }

    public SQLiteDatabase open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        return database;
    }

    public void close() {
        dbHelper.close();
    }

    public List<Language> getAll() {
        return find(null, null, null, null, null);
    }

    public List<Language> getAll(String whereClause, String[] whereArgs, String orderBy, String limit) {
        return find(whereClause, whereArgs, null, orderBy, limit);
    }

    public Language findById(String _id) {
        List<Language> lang = find(VocaDBHelper.LANG_KEY_ID + "=?",
                new String[] { _id }, null, null, null);
        if (lang.size() == 0)
            return null;
        return lang.get(0);
    }

    public List<Language> find(String whereClause, String[] whereArgs,
                               String groupBy, String orderBy, String limit) {
        Cursor c;

        if (whereClause == null) {
            c = database.query(VocaDBHelper.TBL_LANGUAGE, allColumns, null, null,
                    groupBy, null, orderBy, limit);
        } else {
            c = database.query(VocaDBHelper.TBL_LANGUAGE, allColumns,
                    whereClause, whereArgs, groupBy, null, orderBy, limit);
        }

        List<Language> langList = new ArrayList<Language>();
        try {
            while (c.moveToNext()) {
                Language lang = cursorToLanguage(c);
                langList.add(lang);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return langList;
    }

    private Language cursorToLanguage(Cursor _c) {
        Language lang = new Language();
        lang.setLanguageName(_c.getString(0));
        lang.setLanguageCode(_c.getString(1));
        lang.setSelected(_c.getInt(2));
        return lang;
    }
}