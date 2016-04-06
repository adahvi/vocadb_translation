package com.vocadb.translator.databasehelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vocadb.translator.models.History;
import com.vocadb.translator.models.Idioms;
import com.vocadb.translator.models.LanguageClass;
import com.vocadb.translator.models.Words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//import android.util.Log;

public class Database extends SQLiteOpenHelper {

	private static final String TABLE_MY_LANGUAGE = "table_my_language";
	private static final String KEY_ID = "key_id";
	private static final String KEY_LANGUAGE_NAME = "key_language_name";
	private static final String DATABASE_NAME = "translator_app.db";
	private static final int DATABASE_VERSION = 1;
	private static final String KEY_LANGUAGE_CODE = "key_language_code";
	private static final String KEY_LANGUAGE_STATE = "key_language_state";
	private static final String TABLE_HISTORY = "table_history";
	private static final String KEY_HISTORY_SL = "key_sl";
	private static final String KEY_HISTORY_TL = "key_tl";
	private static final String KEY_HISTORY_SL_DATA = "key_sl_data";
	private static final String KEY_HISTORY_TL_DATA = "key_tl_data";
	private static final String KEY_HISTORY_FAV = "key_fav";
	private static final String TABLE_WORDS = "table_words";
	private static final String KEY_WORD_VOCA = "key_voca";
	private static final String KEY_WORD_PART = "key_part";
	private static final String KEY_WORD_LEVEL = "key_level";
	private static final String KEY_WORD_T_DATA = "key_tl_data";
	private static final String KEY_WORD_IS_DELETED = "key_is_deleted";
	private static final String TABLE_IDIOMS = "table_idioms";
	private static final String KEY_IDIOM_VOCA = "key_idiom_voca";
	private static final String KEY_IDIOM_LEVEL = "key_idiom_level";
	private static final String KEY_IDIOM_T_DATA = "key_idiom_tdata";
	private static final String KEY_IDIOM_IS_DELETED = "key_isidiom_deleted";
	private static final String KEY_HISTORY_TIME = "key_history_time";
	private static final String KEY_WORD_ILEVEL = "key_word_ilevel";
	private static final String KEY_LANGUAGE_SL_STATE = "key_language_sl_state";
	private static final String KEY_LANGUAGE_SL_STATE_TIME = "key_language_sl_time";
	private static final String KEY_LANGUAGE_TL_STATE = "key_language_tl_state";
	private static final String KEY_LANGUAGE_TL_STATE_TIME = "key_language_tl_time";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public void onCreate(SQLiteDatabase db) {
		///  Language Table
				String CREATE_MY_LANGUAGE_TABLE = "CREATE TABLE " + TABLE_MY_LANGUAGE + "("
						+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_LANGUAGE_NAME + " TEXT," + KEY_LANGUAGE_CODE + " TEXT," + KEY_LANGUAGE_STATE + " INTEGER," + KEY_LANGUAGE_SL_STATE + " INTEGER,"+ KEY_LANGUAGE_SL_STATE_TIME + " TEXT,"+ KEY_LANGUAGE_TL_STATE + " INTEGER,"+ KEY_LANGUAGE_TL_STATE_TIME + " TEXT" + ")";
		
				///  Language Table
				String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
						+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_HISTORY_SL + " TEXT," + KEY_HISTORY_TL + " TEXT," + KEY_HISTORY_SL_DATA + " TEXT," + KEY_HISTORY_TL_DATA + " TEXT," + KEY_HISTORY_FAV + " INTEGER,"  + KEY_HISTORY_TIME + " TEXT" + ")";
				
				///  Words Table
				String CREATE_WORD_TABLE = "CREATE TABLE " + TABLE_WORDS + "("
						+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_WORD_VOCA + " TEXT," + KEY_WORD_PART + " TEXT," + KEY_WORD_LEVEL + " TEXT," + KEY_WORD_T_DATA + " TEXT," + KEY_WORD_IS_DELETED + " INTEGER," + KEY_WORD_ILEVEL + " INTEGER " + ")";
			

				///  Idioms Table
				String CREATE_IDIOMS_TABLE = "CREATE TABLE " + TABLE_IDIOMS + "("
						+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_IDIOM_VOCA + " TEXT," + KEY_IDIOM_LEVEL + " TEXT," + KEY_IDIOM_T_DATA + " TEXT," + KEY_IDIOM_IS_DELETED + " INTEGER" + ")";
			
				
				db.execSQL(CREATE_MY_LANGUAGE_TABLE);
				db.execSQL(CREATE_HISTORY_TABLE);
				db.execSQL(CREATE_WORD_TABLE);
				db.execSQL(CREATE_IDIOMS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_LANGUAGE);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_IDIOMS);
		// Create tables again
			onCreate(db);
	}



	public ArrayList<LanguageClass> getMyLanguages() {
		ArrayList<LanguageClass> mylanlist = new ArrayList<LanguageClass>();
		// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_MY_LANGUAGE;

				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						LanguageClass language = new LanguageClass();
						language.setID(Integer.parseInt(cursor.getString(0)));
						language.setLanguageName(cursor.getString(1));
						language.setLanCode(cursor.getString(2));
						int state = cursor.getInt(3);
						if(state == 1)
						{
							language.setSelected(true);
							
						}
						else{
							language.setSelected(false);
						}
						// Adding contact to list
						mylanlist.add(language);
					} while (cursor.moveToNext());
				}
		db.close();
				// return contact list
				return mylanlist;
	}



	public void removeLanguage(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MY_LANGUAGE,KEY_ID + "=" + id, null);
		db.close();
	}



	public void addLanguage(LanguageClass language) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(KEY_ID, language.getID());
		values.put(KEY_LANGUAGE_NAME, language.getLanguageName()); // Contact Name
		values.put(KEY_LANGUAGE_CODE, language.getLangCode()); // Contact Name
		boolean state = language.isSelected();
		if(state)
		{
		values.put(KEY_LANGUAGE_STATE,1); // Contact Name
		}
		else
		{
			values.put(KEY_LANGUAGE_STATE, 0); // Contact Name
		}
		
		// Inserting Row
		db.insert(TABLE_MY_LANGUAGE, null, values);
		db.close(); // Closing database connection
	}



	public void removeallLanguage() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MY_LANGUAGE, null, null);
		db.close();
	}



	public String getLanCode(String language) {
SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_MY_LANGUAGE, new String[] { KEY_ID,KEY_LANGUAGE_NAME,KEY_LANGUAGE_CODE}, KEY_LANGUAGE_NAME + "=?",
				new String[] {language}, null, null,null);
		   String lan_code = "";
		if(cursor.moveToFirst()) {
			   
			    lan_code = cursor.getString(2);
			    
			   cursor.close();
			   db.close();
			   return lan_code;
		}
	//	Log.i("db", "language="+language);
	//	Log.i("code", "="+lan_code);
		return lan_code;
	}



	public boolean checkLangauageExists(String languageName) {
		 SQLiteDatabase db = this.getReadableDatabase();

		    Cursor cursor = db.query(TABLE_MY_LANGUAGE, new String[] { KEY_ID,
		    		KEY_LANGUAGE_NAME, KEY_LANGUAGE_CODE }, KEY_LANGUAGE_NAME + "=?",
		            new String[] { languageName }, null, null, null, null);


		    if (cursor.getCount() > 0)
		        return true;

		    else
		        return false;
	}



	public void addNewHistory(History history) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(KEY_ID, language.getID());
		values.put(KEY_HISTORY_SL, history.getHistorySL()); 
		values.put(KEY_HISTORY_TL, history.getHistoryTL()); 
		values.put(KEY_HISTORY_SL_DATA, history.getHistorySLData()); 
		values.put(KEY_HISTORY_TL_DATA, history.getHistoryTLData()); 
		boolean state = history.isFav();
		if(state)
		{
		values.put(KEY_HISTORY_FAV,1); 
		}
		else
		{
			values.put(KEY_HISTORY_FAV, 0); 
		}
		values.put(KEY_HISTORY_TIME, history.getTime()); 
		 Cursor cursor = db.query(TABLE_HISTORY, new String[] { KEY_ID,
				 KEY_HISTORY_SL,KEY_HISTORY_SL_DATA }, KEY_HISTORY_SL_DATA + "=?",
		            new String[] {history.getHistorySLData() }, null, null, null, null);


		    if (cursor.getCount() > 0)
		    {
		    	// updating row
			     db.update(TABLE_HISTORY, values, KEY_HISTORY_SL_DATA + " = ?",
			            new String[] {history.getHistorySLData() });
			
		    }
		    else {
	//	    	Log.i("Database", "added deleted word");
				// Inserting Row
				db.insert(TABLE_HISTORY, null, values);		    }
		
		db.close(); // Closing database connection
	}



	public ArrayList<History> getHistoryList() {
		ArrayList<History> history_list = new ArrayList<History>();
		// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_HISTORY;

				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						History history = new History();
						history.setID(cursor.getInt(0));
						history.setSL(cursor.getString(1));
						history.setTL(cursor.getString(2));
						history.setSLData(cursor.getString(3));
						history.setTLData(cursor.getString(4));
						int state = cursor.getInt(5);
						if(state == 1)
						{
							history.setFav(true);
							
						}
						else{
							history.setFav(false);
						}
						history.setTime(cursor.getString(6));
						// Adding contact to list
						history_list.add(history);
					} while (cursor.moveToNext());
				}
		db.close();
//		Log.i("Database", "history GET"+history_list.size());
				return history_list;
	}



	public void removeHistory(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_HISTORY,KEY_ID + "=" + id, null);
		db.close();
	}



	public void addReadWord(Words word) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(KEY_ID, language.getID());
		values.put(KEY_WORD_VOCA, word.getVoca()); 
		values.put(KEY_WORD_PART, word.getPart()); 
		values.put(KEY_WORD_LEVEL, word.getLevel()); 
		values.put(KEY_WORD_T_DATA, word.getTLData());
		values.put(KEY_WORD_IS_DELETED,3);
		values.put(KEY_WORD_ILEVEL,word.getilevel());
	//	Log.i("Database", "Word readed");
		// Inserting Row
		db.insert(TABLE_WORDS, null, values);
		db.close(); // Closing database connection
	}



	public void deleteWord(Words word) {
	//	Log.i("wordbd", "delete"+word.getVoca());
		SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DELETE",word.getVoca());
        ContentValues values = new ContentValues();
		//values.put(KEY_ID, language.getID());
		values.put(KEY_WORD_VOCA, word.getVoca()); 
		values.put(KEY_WORD_PART, word.getPart()); 
		values.put(KEY_WORD_LEVEL, word.getLevel()); 
		values.put(KEY_WORD_T_DATA, word.getTLData());
		values.put(KEY_WORD_IS_DELETED,1);
		values.put(KEY_WORD_ILEVEL,word.getilevel());
		   Cursor cursor = db.query(TABLE_WORDS, new String[] { KEY_ID,
		    		KEY_WORD_VOCA,KEY_WORD_IS_DELETED }, KEY_WORD_VOCA + "=? AND " + KEY_WORD_IS_DELETED + "=1" ,
		            new String[] { word.getVoca() }, null, null, null, null);


		    if (cursor.getCount() > 0)
		    {
		    	// updating row

			     db.update(TABLE_WORDS, values, KEY_WORD_VOCA + " = ?",
                         new String[]{word.getVoca()});
		    }
		    else {
	//	    	Log.i("Database", "added deleted word");
				// Inserting Row
				db.insert(TABLE_WORDS, null, values);
                db.update(TABLE_WORDS, values, KEY_WORD_VOCA + " = ?",
                        new String[] {word.getVoca() });
            }
		
		db.close(); // Closing database connection
	  
	}











	public void deleteIdiom(Idioms idioms) {
	//	Log.i("wordbd", "delete"+idioms.getVoca());
		SQLiteDatabase db = this.getWritableDatabase();
		 
		ContentValues values = new ContentValues();
		//values.put(KEY_ID, language.getID());
		values.put(KEY_IDIOM_VOCA, idioms.getVoca()); 
		values.put(KEY_IDIOM_LEVEL, idioms.getLevel()); 
		values.put(KEY_IDIOM_T_DATA, idioms.getTLData());
		values.put(KEY_IDIOM_IS_DELETED,1);
		   Cursor cursor = db.query(TABLE_IDIOMS, new String[] { KEY_ID,
		    		KEY_IDIOM_VOCA,KEY_IDIOM_IS_DELETED }, KEY_IDIOM_VOCA + "=? AND " + KEY_IDIOM_IS_DELETED + "=1" ,
		            new String[] { idioms.getVoca() }, null, null, null, null);


		    if (cursor.getCount() > 0)
		    {
		    	// updating row
			     db.update(TABLE_IDIOMS, values, KEY_IDIOM_VOCA + " = ?",
			            new String[] {idioms.getVoca() });
			
		    }
		    else {
	//	    	Log.i("Database", "added deleted word");
				// Inserting Row
				db.insert(TABLE_IDIOMS, null, values);		    }
		
		db.close(); // Closing database connection
	}







	public boolean checkWordStored(String voca) {
	//	Log.i("wordbd", "check for"+voca);
		 SQLiteDatabase db = this.getReadableDatabase();

		    Cursor cursor = db.query(TABLE_WORDS, new String[] { KEY_ID,
		    		KEY_WORD_VOCA,KEY_WORD_IS_DELETED }, KEY_WORD_VOCA + "=? AND " + KEY_WORD_IS_DELETED + "=3" ,
		            new String[] { voca }, null, null, null, null);


		    if (cursor.getCount() > 0)
		        return true;

		    else
		        return false;
	}



	public void UnreadWord(String voca) {
//		Log.i("wordbd", "unread"+voca);
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_WORD_IS_DELETED,0);
	  
	    // updating row
	     db.update(TABLE_WORDS, values, KEY_WORD_VOCA + " = ?",
	            new String[] {voca });
	    db.close();
	}



	public boolean checkWordDeleted(String voca) {
	//	Log.i("wordbd", "check for deleted"+voca);
		 SQLiteDatabase db = this.getReadableDatabase();

		    Cursor cursor = db.query(TABLE_WORDS, new String[] { KEY_ID,
		    		KEY_WORD_VOCA,KEY_WORD_IS_DELETED }, KEY_WORD_VOCA + "=? AND " + KEY_WORD_IS_DELETED + "=1" ,
		            new String[] { voca }, null, null, null, null);


		    if (cursor.getCount() > 0)
		    {
	//	    	Log.i("wordbd", "true");
		        return true;
			
		    }
		    else {
	//			Log.i("wordbd", "false");
				 return false;
		    }
		       
	}



	public boolean checkIdomDeleted(String voca) {
	//	Log.i("idombd", "check for deleted"+voca);
		 SQLiteDatabase db = this.getReadableDatabase();

		    Cursor cursor = db.query(TABLE_IDIOMS, new String[] { KEY_ID,
		    		KEY_IDIOM_VOCA,KEY_IDIOM_IS_DELETED }, KEY_IDIOM_VOCA + "=? AND " + KEY_IDIOM_IS_DELETED + "=1" ,
		            new String[] { voca }, null, null, null, null);


		    if (cursor.getCount() > 0)
		    {
//		    	Log.i("wordbd", "true");
		        return true;
			
		    }
		    else {
	//			Log.i("wordbd", "false");
				 return false;
		    }
	}



	public boolean checkIdomStored(String voca) {
//		Log.i("idiombd", "check for"+voca);
		 SQLiteDatabase db = this.getReadableDatabase();

		    Cursor cursor = db.query(TABLE_IDIOMS, new String[] { KEY_ID,
		    		KEY_IDIOM_VOCA,KEY_IDIOM_IS_DELETED }, KEY_IDIOM_VOCA + "=? AND " + KEY_IDIOM_IS_DELETED + "=3" ,
		            new String[] { voca }, null, null, null, null);


		    if (cursor.getCount() > 0)
		        return true;

		    else
		        return false;
	}



	public void UnreadIdiom(String voca) {
//		Log.i("wordbd", "unread"+voca);
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_IDIOM_IS_DELETED,0);
	  
	    // updating row
	     db.update(TABLE_IDIOMS, values, KEY_IDIOM_VOCA + " = ?",
	            new String[] {voca });
	    db.close();	
	}



	public void addReadIdiom(Idioms idioms) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(KEY_ID, language.getID());
		values.put(KEY_IDIOM_VOCA, idioms.getVoca()); 
		values.put(KEY_IDIOM_LEVEL, idioms.getLevel()); 
		values.put(KEY_IDIOM_T_DATA, idioms.getTLData()); 
		values.put(KEY_IDIOM_IS_DELETED,3);
		
	//	Log.i("Database", "Word readed");
		// Inserting Row
		db.insert(TABLE_IDIOMS, null, values);
		db.close(); // Closing database connection
	}



	public void removeallHistory() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_HISTORY, null, null);
		db.close();
	}



	public String getLanName(String sl) {
SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_MY_LANGUAGE, new String[] { KEY_ID,KEY_LANGUAGE_NAME,KEY_LANGUAGE_CODE}, KEY_LANGUAGE_CODE + "=?",
				new String[] {sl}, null, null,null);
		   String language = "";
		if(cursor.moveToFirst()) {
			   
			language = cursor.getString(1);
			    
			   cursor.close();
			   db.close();
			   return language;
		}
		
		return language;
	}



	public ArrayList<Words> getWordlist() {
		ArrayList<Words> wordslist = new ArrayList<Words>();
		// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_WORDS + " WHERE " + KEY_WORD_IS_DELETED + "!=1";

				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);
				///  Words Table


				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						Words word = new Words();
						word.setID(cursor.getInt(0));
						word.setVoca(cursor.getString(1));
						word.setPart(cursor.getString(2));
						word.setLevel(cursor.getString(3));
						word.setTdata(cursor.getString(4));
						word.setilabel(cursor.getInt(6));
						// Adding contact to list
						wordslist.add(word);
					} while (cursor.moveToNext());
				}
		db.close();
				// return contact list
				return wordslist;
	}



	public boolean checkHistoryFav(int id) {
		 SQLiteDatabase db = this.getReadableDatabase();

		    Cursor cursor = db.query(TABLE_HISTORY, new String[] { KEY_ID,
		    		KEY_HISTORY_FAV }, KEY_ID + "=? AND " + KEY_HISTORY_FAV + "=1" ,
		            new String[] { String.valueOf(id) }, null, null, null, null);


		    if (cursor.getCount() > 0)
		        return true;

		    else
		        return false;
	}



	public void setHistoryFav(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
	   ContentValues values = new ContentValues();
	    values.put(KEY_HISTORY_FAV,1);
	  
	    // updating row
	     db.update(TABLE_HISTORY, values, KEY_ID + " = ?",
	            new String[] {String.valueOf(id) });
	    db.close();
	}



	public void UnsetHistoryFav(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_HISTORY_FAV,0);
	    // updating row
	     db.update(TABLE_HISTORY, values, KEY_ID + " = ?",
	            new String[] {String.valueOf(id) });
	    db.close();
	}



	public ArrayList<LanguageClass> getSourceLanguages() {
		ArrayList<LanguageClass> mylanlist = new ArrayList<LanguageClass>();
		// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_MY_LANGUAGE + " WHERE " + KEY_LANGUAGE_SL_STATE + "==1";

				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						LanguageClass language = new LanguageClass();
						language.setID(Integer.parseInt(cursor.getString(0)));
						language.setLanguageName(cursor.getString(1));
						language.setLanCode(cursor.getString(2));
						int state = cursor.getInt(3);
						language.setTime(cursor.getString(5));
						if(state == 1)
						{
							language.setSelected(true);
							
						}
						else{
							language.setSelected(false);
						}
						// Adding contact to list
						mylanlist.add(language);
					} while (cursor.moveToNext());
				}
		db.close();
		Collections.sort(mylanlist, new Comparator<LanguageClass>() {

			@Override
			public int compare(
					LanguageClass lhs, LanguageClass rhs) {
				// TODO Auto-generated method stub
				return rhs.getTime().compareToIgnoreCase(lhs.getTime());
			}
	       
	    });
				return mylanlist;
	}



	public ArrayList<LanguageClass> getTargetLanguages() {
		ArrayList<LanguageClass> mylanlist = new ArrayList<LanguageClass>();
		// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_MY_LANGUAGE + " WHERE " + KEY_LANGUAGE_TL_STATE + "==1";

				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						LanguageClass language = new LanguageClass();
						language.setID(Integer.parseInt(cursor.getString(0)));
						language.setLanguageName(cursor.getString(1));
						language.setLanCode(cursor.getString(2));
						int state = cursor.getInt(3);
						language.setTime(cursor.getString(7));
						if(state == 1)
						{
							language.setSelected(true);
							
						}
						else{
							language.setSelected(false);
						}
						// Adding contact to list
						mylanlist.add(language);
					} while (cursor.moveToNext());
				}
		db.close();
		Collections.sort(mylanlist, new Comparator<LanguageClass>() {

			@Override
			public int compare(
					LanguageClass lhs, LanguageClass rhs) {
				// TODO Auto-generated method stub
				return rhs.getTime().compareToIgnoreCase(lhs.getTime());
			}
	       
	    });
				return mylanlist;
	}



	public void setSourceLanguage(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
//		 Log.i("set source", "name="+name);
	    ContentValues values = new ContentValues();
	    values.put(KEY_LANGUAGE_SL_STATE,1);
	    values.put(KEY_LANGUAGE_SL_STATE_TIME,String.valueOf(System.currentTimeMillis()));
	    // updating row
	     db.update(TABLE_MY_LANGUAGE, values, KEY_LANGUAGE_NAME + " = ?",
	            new String[] {String.valueOf(name) });
	    db.close();
	}



	public void setTargetLanguage(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
	//	 Log.i("set target", "name="+name);
	    ContentValues values = new ContentValues();
	    values.put(KEY_LANGUAGE_TL_STATE,1);
	    values.put(KEY_LANGUAGE_TL_STATE_TIME,String.valueOf(System.currentTimeMillis()));
	    // updating row
	     db.update(TABLE_MY_LANGUAGE, values, KEY_LANGUAGE_NAME + " = ?",
	            new String[] {String.valueOf(name) });
	    db.close();
	}



	public ArrayList<LanguageClass> getSelectedLanguages() {
		ArrayList<LanguageClass> mylanlist = new ArrayList<LanguageClass>();
		// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_MY_LANGUAGE + " WHERE " + KEY_LANGUAGE_STATE + "==1";

				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						LanguageClass language = new LanguageClass();
						language.setID(Integer.parseInt(cursor.getString(0)));
						language.setLanguageName(cursor.getString(1));
						language.setLanCode(cursor.getString(2));
						int state = cursor.getInt(3);
						if(state == 1)
						{
							language.setSelected(true);
							
						}
						else{
							language.setSelected(false);
						}
						// Adding contact to list
						mylanlist.add(language);
					} while (cursor.moveToNext());
				}
		db.close();
				// return contact list
				return mylanlist;
	}



	public void UpdateMyLanguage(LanguageClass language) {
		SQLiteDatabase db = this.getWritableDatabase();
//		 Log.i("Update language", "id="+language.getID());
	    ContentValues values = new ContentValues();
	    boolean state = language.isSelected();
		if(state)
		{
		values.put(KEY_LANGUAGE_STATE,1); // Contact Name
		}
		else
		{
			values.put(KEY_LANGUAGE_STATE, 0); // Contact Name
			values.put(KEY_LANGUAGE_SL_STATE, 0); // Contact Name
			values.put(KEY_LANGUAGE_TL_STATE, 0); // Contact Name
		}
	    // updating row
	     db.update(TABLE_MY_LANGUAGE, values, KEY_ID + " = ?",
	            new String[] {String.valueOf(language.getID()) });
	    db.close();
	}



	

}
