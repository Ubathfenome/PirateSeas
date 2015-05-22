package com.pirateseas.utils.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	private static final String TAG = "DBHelper";
	
	private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "game_database.db";
    private static final String DATABASE_TGAME = "t_game";
    private static final String DATABASE_TSHIP = "t_ship";
    private static final String DATABASE_TPLAYER = "t_player";
    
    private static final String TGAME_KEY = "codg";
    private static final String TGAME_TIMESTAMP = "startTime";
    
    private static final String TPLAYER_KEY = "codp";
    private static final String TPLAYER_DAYS = "days";
    private static final String TPLAYER_GOLD = "gold";
    private static final String TPLAYER_EXP = "experience";
    private static final String TPLAYER_MAP_PIECES = "mapPieces";
    
    private static final String TSHIP_KEY = "cods";
    private static final String TSHIP_COORD_X = "coordX";
    private static final String TSHIP_COORD_Y = "coordY";
    private static final String TSHIP_TYPE = "type";
    private static final String TSHIP_HEALTH = "health";
    private static final String TSHIP_AMMO = "ammo";
    

    private static final String DICTIONARY_TABLE1_CREATE =
            "CREATE TABLE " + DATABASE_TPLAYER + " (" +
            TPLAYER_KEY + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 0, " +
            TPLAYER_DAYS + " INTEGER NOT NULL DEFAULT 0, " +
            TPLAYER_GOLD + " INTEGER NOT NULL DEFAULT 0, " +
            TPLAYER_EXP + " INTEGER NOT NULL DEFAULT 0, " +
            TPLAYER_MAP_PIECES + " INTEGER NOT NULL DEFAULT 0" +
            " );";
    private static final String DICTIONARY_TABLE2_CREATE =
            "CREATE TABLE " + DATABASE_TSHIP + " (" +
            TSHIP_KEY + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 0, " +
            TSHIP_COORD_X + " INTEGER NOT NULL DEFAULT 0, " + 
            TSHIP_COORD_Y + " INTEGER NOT NULL DEFAULT 0, " +
            TSHIP_TYPE + " INTEGER NOT NULL DEFAULT 0, " +
            TSHIP_HEALTH + " INTEGER NOT NULL, " +
            TSHIP_AMMO + " INTEGER NOT NULL" +
            " );";
    private static final String DICTIONARY_TABLE3_CREATE =
            "CREATE TABLE " + DATABASE_TGAME + " (" +
            TGAME_KEY + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE  DEFAULT 0, " +
            TPLAYER_KEY + " INTEGER REFERENCES " + DATABASE_TPLAYER + ", " + 
            TSHIP_KEY + " INTEGER REFERENCES " + DATABASE_TSHIP + ", " +
            TGAME_TIMESTAMP + " TIMESTAMP NOT NULL DEFAULT NOW()" + 
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
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TSHIP);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TPLAYER);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TGAME);

        // Recreates the database with a new version
        onCreate(db);
	}

}
