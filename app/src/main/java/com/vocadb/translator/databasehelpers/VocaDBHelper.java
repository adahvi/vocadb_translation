package com.vocadb.translator.databasehelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vocadb.translator.models.History;
import com.vocadb.translator.models.Language;
import com.vocadb.translator.models.Words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A helper class to manage database creation and version management.
 *
 * vocaDB database
 *
 * Created by Adah Valeña on 4/5/2016.
 */
public class VocaDBHelper extends SQLiteOpenHelper {

    // Database details
    private static final String DATABASE_NAME = "vocadb_trans.db";
    private static final int DATABASE_VERSION = 1;

    // My Language Table
    public static final String TBL_LANGUAGE = "tbl_my_language";
    public static final String LANG_KEY_ID = "id";
    public static final String LANG_KEY_NAME = "language_name";
    public static final String LANG_KEY_CODE = "language_code";
    public static final String LANG_KEY_STATE = "language_state";
    public static final String LANG_KEY_SL_STATE = "language_sl_state";
    public static final String LANG_KEY_SL_STATE_TIME = "language_sl_state_time";
    public static final String LANG_KEY_TL_STATE = "language_tl_state";
    public static final String LANG_KEY_TL_STATE_TIME = "language_tl_state_time";

    // History Table
    public static final String TBL_HISTORY = "tbl_history";
    public static final String HISTORY_KEY_ID = "id";
    public static final String HISTORY_KEY_SL = "source_lang";
    public static final String HISTORY_KEY_TL = "target_lang";
    public static final String HISTORY_KEY_SL_DATA = "source_lang_data";
    public static final String HISTORY_KEY_TL_DATA = "target_lang_data";
    public static final String HISTORY_KEY_FAVORITE = "favorite";
    public static final String HISTORY_KEY_TIME = "time";

    // Words Table
    public static final String TBL_WORDS = "tbl_words";
    public static final String WORDS_KEY_ID = "id";
    public static final String WORDS_KEY_VOCA = "voca";
    public static final String WORDS_KEY_PART = "part";
    public static final String WORDS_KEY_LEVEL = "level";
    public static final String WORDS_KEY_ILEVEL = "ilevel";
    public static final String WORDS_KEY_TL_DATA = "tl_data";
    public static final String WORDS_KEY_IS_DELETED = "is_deleted";

    // Idioms Table
    public static final String TBL_IDIOMS = "tbl_idioms";
    public static final String IDIOMS_KEY_ID = "id";
    public static final String IDIOMS_KEY_VOCA = "voca";
    public static final String IDIOMS_KEY_LEVEL = "level";
    public static final String IDIOMS_KEY_T_DATA = "t_data";
    public static final String IDIOMS_KEY_IS_DELETED = "is_deleted";


    public VocaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    // This method can be used to rebuild the entire database file and thus
    // reclaim unused disk space
    public void runVacuum(SQLiteDatabase db) {
        db.execSQL("VACUUM");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Language Table
        String CREATE_TBL_LANGUAGE = "CREATE TABLE IF NOT EXISTS " + TBL_LANGUAGE + "(" +
                LANG_KEY_ID + " INTEGER PRIMARY KEY," +
                LANG_KEY_NAME + " TEXT," +
                LANG_KEY_CODE + " TEXT," +
                LANG_KEY_STATE + " INTEGER," +
                LANG_KEY_SL_STATE + " INTEGER,"+
                LANG_KEY_SL_STATE_TIME + " TEXT,"+
                LANG_KEY_TL_STATE + " INTEGER,"+
                LANG_KEY_TL_STATE_TIME + " TEXT" + ")";

        // Create History Table
        String CREATE_TBL_HISTORY = "CREATE TABLE IF NOT EXISTS " + TBL_HISTORY + "(" +
                HISTORY_KEY_ID + " INTEGER PRIMARY KEY," +
                HISTORY_KEY_SL + " TEXT," +
                HISTORY_KEY_TL + " TEXT," +
                HISTORY_KEY_SL_DATA + " TEXT," +
                HISTORY_KEY_TL_DATA + " TEXT," +
                HISTORY_KEY_FAVORITE + " INTEGER,"  +
                HISTORY_KEY_TIME + " TEXT" + ")";

        // Create Words Table
        String CREATE_TBL_WORDS = "CREATE TABLE IF NOT EXISTS " + TBL_WORDS + "(" +
                WORDS_KEY_ID + " INTEGER PRIMARY KEY," +
                WORDS_KEY_VOCA + " TEXT," +
                WORDS_KEY_PART + " TEXT," +
                WORDS_KEY_LEVEL + " TEXT," +
                WORDS_KEY_ILEVEL + " INTEGER " +
                WORDS_KEY_TL_DATA + " TEXT," +
                WORDS_KEY_IS_DELETED + " INTEGER," + ")";

        // Create Idioms Table
        String CREATE_TBL_IDIOMS = "CREATE TABLE IF NOT EXISTS " + TBL_IDIOMS + "(" +
                IDIOMS_KEY_ID + " INTEGER PRIMARY KEY," +
                IDIOMS_KEY_VOCA + " TEXT," +
                IDIOMS_KEY_LEVEL + " TEXT," +
                IDIOMS_KEY_T_DATA + " TEXT," +
                IDIOMS_KEY_IS_DELETED + " INTEGER" + ")";

        // Execute create table statements
        db.execSQL(CREATE_TBL_LANGUAGE);
        db.execSQL(CREATE_TBL_HISTORY);
        db.execSQL(CREATE_TBL_WORDS);
        db.execSQL(CREATE_TBL_IDIOMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        onCreate(db);
    }

}
