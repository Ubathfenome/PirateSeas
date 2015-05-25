package com.pirateseas.utils.persistence;

import com.pirateseas.global.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	private static final String TAG = "DBHelper";
	
	private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "game_database.db";    

    private static final String DICTIONARY_TABLE1_CREATE =
            "CREATE TABLE " + Constants.DATABASE_TPLAYER + " (" +
            		Constants.TPLAYER_KEY + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 1, " +
            		Constants.TPLAYER_DAYS + " INTEGER NOT NULL DEFAULT 0, " +
            		Constants.TPLAYER_GOLD + " INTEGER NOT NULL DEFAULT 0, " +
            		Constants.TPLAYER_EXP + " INTEGER NOT NULL DEFAULT 0, " +
            		Constants.TPLAYER_MAP_PIECES + " INTEGER NOT NULL DEFAULT 0" +
            " );";
    private static final String DICTIONARY_TABLE2_CREATE =
            "CREATE TABLE " + Constants.DATABASE_TSHIP + " (" +
            		Constants.TSHIP_KEY + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 1, " +
            		Constants.TSHIP_COORD_X + " INTEGER NOT NULL DEFAULT 0, " + 
            		Constants.TSHIP_COORD_Y + " INTEGER NOT NULL DEFAULT 0, " +
            		Constants.TSHIP_TYPE + " INTEGER NOT NULL DEFAULT 0, " +
            		Constants.TSHIP_HEALTH + " INTEGER NOT NULL, " +
            		Constants.TSHIP_AMMO + " INTEGER NOT NULL" +
            " );";
    private static final String DICTIONARY_TABLE3_CREATE =
            "CREATE TABLE " + Constants.DATABASE_TGAME + " (" +
            		Constants.TGAME_KEY + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 1, " +
            		Constants.TPLAYER_KEY + " INTEGER REFERENCES " + Constants.DATABASE_TPLAYER + ", " + 
            		Constants.TSHIP_KEY + " INTEGER REFERENCES " + Constants.DATABASE_TSHIP + ", " +
            		Constants.TGAME_TIMESTAMP + " TIMESTAMP NOT NULL DEFAULT NOW()" + 
            " );";
    
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DICTIONARY_TABLE1_CREATE);
		db.execSQL(DICTIONARY_TABLE2_CREATE);
		db.execSQL(DICTIONARY_TABLE3_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Logs that the database is being upgraded
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DATABASE_TSHIP);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DATABASE_TPLAYER);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DATABASE_TGAME);

        // Recreates the database with a new version
        onCreate(db);
	}

}
