package com.pirateseas.utils.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.Log;

import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.entity.Ammunitions;
import com.pirateseas.model.canvasmodel.game.entity.Ship;
import com.pirateseas.model.canvasmodel.game.entity.ShipType;

/**
 * 
 * @author p7166421
 * 
 * @see: https://developers.google.com/games/services/checklist#1_sign-in
 *
 */
public class GameHelper {
	
	public static Player helperPlayer;
	public static Ship helperShip;
	
	private static long lastPlayerRow = 0;
	private static long lastShipRow = 0;
	private static long lastGameRow = 0;
	
	private static final int DEFAULT_DIRECTION = 90;
	private static final int DEFAULT_SHIP_WIDTH = 2;
	private static final int DEFAULT_SHIP_HEIGHT = 3;
	private static final int DEFAULT_SHIP_LENGTH = 5;

	public static boolean saveGameAtPreferences(Context context, Player player, Ship ship){
		
		boolean res = false;
		
		SharedPreferences mPreferences = context.getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt(Constants.PREF_PLAYER_DAYS, player.getPassedDays());
		editor.putInt(Constants.PREF_PLAYER_LEVEL, player.getLevel());
		editor.putInt(Constants.PREF_PLAYER_GOLD, player.getGold());
		editor.putInt(Constants.PREF_PLAYER_XP, player.getExperience());
		editor.putInt(Constants.PREF_PLAYER_MAP_PIECES, player.getMapPieces());
		editor.putInt(Constants.PREF_SHIP_COORDINATES_X, ship.getCoordinates().x);
		editor.putInt(Constants.PREF_SHIP_COORDINATES_Y, ship.getCoordinates().y);
		for(Ammunitions a : Ammunitions.values()){
			editor.putInt(a.getName(), ship.getAmmunition(a));
		}
		editor.putInt(Constants.PREF_SHIP_HEALTH, ship.getHealth());
		editor.putInt(Constants.PREF_SHIP_TYPE, ship.getType().ordinal());
		
		res = editor.commit();
		
		return res;
	}
	
	public static boolean saveGameAtDatabase(Context context, Player player, Ship ship){
		DBInsertNewPlayer(context, player);
		if(lastPlayerRow == -1)
			return false;
		DBInsertNewShip(context, ship);
		if(lastShipRow == -1)
			// XXX RollBack player?
			return false;
		DBInsertNewGame(context);
		if(lastGameRow == -1)
			// XXX RollBack player?
			// XXX RollBack ship?
			return false;
		return true;
	}
	
	private static void DBInsertNewPlayer(Context context, Player player){
		SQLiteDatabase db = null;
		String TAG = "DBInsertNewPlayer"; 
		try {
			DBHelper dbh = new DBHelper(context);
			db = dbh.getWritableDatabase();

			lastPlayerRow = db.insert(Constants.DATABASE_TPLAYER, null, DBGeneratePlayerValues(player));
		} catch (SQLiteException ex) {
			String msg = "DATABASE ERROR: " + ex.getMessage();
			Log.e(TAG, msg);
		} finally {
			if (lastPlayerRow == -1)
				Log.e(TAG, "An error happened at insert");
			if (db != null) {
				db.close();
			}
		}
	}
	
	private static ContentValues DBGeneratePlayerValues(Player player){
		ContentValues register = new ContentValues();
		register.put(Constants.TPLAYER_DAYS, player.getPassedDays());
		register.put(Constants.TPLAYER_GOLD, player.getGold());
		register.put(Constants.TPLAYER_EXP, player.getExperience());
		register.put(Constants.TPLAYER_MAP_PIECES, player.getMapPieces());
		return register;
	}
	
	private static void DBInsertNewShip(Context context, Ship ship){
		SQLiteDatabase db = null;
		String TAG = "DBInsertNewShip"; 
		try {
			DBHelper dbh = new DBHelper(context);
			db = dbh.getWritableDatabase();

			lastShipRow = db.insert(Constants.DATABASE_TSHIP, null, DBGenerateShipValues(ship));
		} catch (SQLiteException ex) {
			String msg = "DATABASE ERROR: " + ex.getMessage();
			Log.e(TAG, msg);
		} finally {
			if (lastShipRow == -1)
				Log.e(TAG, "An error happened at insert");
			if (db != null) {
				db.close();
			}
		}
	}
	
	private static ContentValues DBGenerateShipValues(Ship ship){
		ContentValues register = new ContentValues();
		register.put(Constants.TSHIP_COORD_X, ship.getCoordinates().x);
		register.put(Constants.TSHIP_COORD_Y, ship.getCoordinates().y);
		register.put(Constants.TSHIP_TYPE, ship.getType().ordinal());
		register.put(Constants.TSHIP_HEALTH, ship.getHealth());
		register.put(Constants.TSHIP_AMMO_DEFAULT, ship.getAmmunition(Ammunitions.DEFAULT));
		register.put(Constants.TSHIP_AMMO_AIMED, ship.getAmmunition(Ammunitions.AIMED));
		register.put(Constants.TSHIP_AMMO_DOUBLE, ship.getAmmunition(Ammunitions.DOUBLE));
		register.put(Constants.TSHIP_AMMO_SWEEP, ship.getAmmunition(Ammunitions.SWEEP));
		register.put(Constants.TSHIP_SELECTED_AMMO, ship.getSelectedAmmunition());
		return register;
	}
	
	private static void DBInsertNewGame(Context context){
		SQLiteDatabase db = null;
		String TAG = "DBInsertNewGame"; 
		try {
			DBHelper dbh = new DBHelper(context);
			db = dbh.getWritableDatabase();

			lastGameRow = db.insert(Constants.DATABASE_TGAME, null, DBGenerateGameValues());
		} catch (SQLiteException ex) {
			String msg = "DATABASE ERROR: " + ex.getMessage();
			Log.e(TAG, msg);
		} finally {
			if (lastGameRow == -1)
				Log.e(TAG, "An error happened at insert");
			if (db != null) {
				db.close();
			}
		}
	}
	
	private static ContentValues DBGenerateGameValues(){
		ContentValues register = new ContentValues();
		register.put(Constants.TPLAYER_KEY, lastPlayerRow);
		register.put(Constants.TSHIP_KEY, lastShipRow);
		register.put(Constants.TGAME_TIMESTAMP, SystemClock.elapsedRealtime());
		return register;
	}
	
	public static boolean loadGameAtPreferences(Context context, Player player, Ship ship){
		
		SharedPreferences mPreferences = context.getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE);
		
		player.setPassedDays(mPreferences.getInt(Constants.PREF_PLAYER_DAYS, 0));
		player.setGold(mPreferences.getInt(Constants.PREF_PLAYER_GOLD, 0));
		player.setExperience(mPreferences.getInt(Constants.PREF_PLAYER_XP, 0));
		player.setMapPieces(mPreferences.getInt(Constants.PREF_PLAYER_MAP_PIECES, 0));
		
		helperPlayer = player;
		
		Point p = new Point(mPreferences.getInt(Constants.PREF_SHIP_COORDINATES_X, 0), mPreferences.getInt(Constants.PREF_SHIP_COORDINATES_Y, 0));
		int ammo = mPreferences.getInt(Constants.PREF_SHIP_AMMUNITIONS, 20);
		ShipType st = ShipType.values()[mPreferences.getInt(Constants.PREF_SHIP_TYPE, 0)];
		int hp = mPreferences.getInt(Constants.PREF_SHIP_HEALTH, st.defaultHealthPoints());
		ship = new Ship(context, ship, st, p, DEFAULT_DIRECTION, DEFAULT_SHIP_WIDTH, DEFAULT_SHIP_HEIGHT, DEFAULT_SHIP_LENGTH, hp, ammo);
			
		helperShip = ship;
		
		return true;
	}
	
	public static boolean loadGameAtDatabase(Context context){
		helperPlayer = DBSelectPlayer(context);
		helperShip = DBSelectShip(context);
		return DBSelectGame(context);
	}
	
	private static Player DBSelectPlayer(Context context){		
		Player player = new Player();
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			DBHelper dbh = new DBHelper(context);
			db = dbh.getReadableDatabase();

			// Obtain the score code
			c = db.query(true, Constants.DATABASE_TPLAYER, 
					new String[] { 
						Constants.TPLAYER_KEY,
						Constants.TPLAYER_DAYS, 
						Constants.TPLAYER_GOLD, 
						Constants.TPLAYER_EXP, 
						Constants.TPLAYER_MAP_PIECES },
					null, null,	null, null,	null, null);
			if (c.moveToLast()) {
				lastPlayerRow = c.getInt(c.getColumnIndexOrThrow(Constants.TPLAYER_KEY));
				player.setPassedDays(c.getInt(c.getColumnIndexOrThrow(Constants.TPLAYER_DAYS)));
				player.setGold(c.getInt(c.getColumnIndexOrThrow(Constants.TPLAYER_GOLD)));
				player.setExperience(c.getInt(c.getColumnIndexOrThrow(Constants.TPLAYER_EXP)));
				player.setMapPieces(c.getInt(c.getColumnIndexOrThrow(Constants.TPLAYER_MAP_PIECES)));
			}
		} catch (IllegalArgumentException ex) {
			String msg = "DATABASE ERROR: " + ex.getMessage();
			Log.e("DBSelectPlayer", msg);
		} finally {
			if (c != null)
				c.close();
			if (db != null)
				db.close();
		}
		
		return player;		
	}
	
	private static Ship DBSelectShip(Context context){
		Ship ship = new Ship();
		Ship fussionShip = null;
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			DBHelper dbh = new DBHelper(context);
			db = dbh.getReadableDatabase();

			// Obtain the score code
			c = db.query(true, Constants.DATABASE_TSHIP, 
					new String[] { 
						Constants.TSHIP_KEY,
						Constants.TSHIP_COORD_X, 
						Constants.TSHIP_COORD_Y, 
						Constants.TSHIP_TYPE, 
						Constants.TSHIP_HEALTH,
						Constants.TSHIP_AMMO_DEFAULT,
						Constants.TSHIP_AMMO_AIMED,
						Constants.TSHIP_AMMO_DOUBLE,
						Constants.TSHIP_AMMO_SWEEP,
						Constants.TSHIP_SELECTED_AMMO},
					null, null, null, null, null, null);
			if (c.moveToLast()) {
				lastShipRow = c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_KEY));
				int[] ammoTypes = {
						c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_AMMO_DEFAULT)),
						c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_AMMO_AIMED)),
						c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_AMMO_DOUBLE)),
						c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_AMMO_SWEEP))};
				int selectedAmmoType = c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_SELECTED_AMMO));
				fussionShip = new Ship(context, ship, 
						ShipType.values()[c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_TYPE))], 
						new Point(
								c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_COORD_X)), 
								c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_COORD_Y))),
						DEFAULT_DIRECTION, 
						DEFAULT_SHIP_WIDTH, 
						DEFAULT_SHIP_HEIGHT, 
						DEFAULT_SHIP_LENGTH,
						c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_HEALTH)),
						ammoTypes,
						selectedAmmoType);
			}
		} catch (IllegalArgumentException ex) {
			String msg = "DATABASE ERROR: " + ex.getMessage();
			Log.e("DBSelectShip", msg);
		} finally {
			if (c != null)
				c.close();
			if (db != null)
				db.close();
		}
		
		return fussionShip;
	}
	
	private static boolean DBSelectGame(Context context){
		boolean isLoaded = false;
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			DBHelper dbh = new DBHelper(context);
			db = dbh.getReadableDatabase();

			// Obtain the score code
			c = db.query(true, Constants.DATABASE_TGAME, 
					new String[] {
						Constants.TGAME_KEY,
						Constants.TPLAYER_KEY,
						Constants.TSHIP_KEY,
						Constants.TGAME_TIMESTAMP},
					Constants.DATABASE_TGAME + "." + Constants.TPLAYER_KEY + " = '" + lastPlayerRow + "' AND " + 
					Constants.DATABASE_TGAME + "." + Constants.TSHIP_KEY + " = '" + lastShipRow + "';",
					null, null, null, null, null);
			if (c.moveToLast()) {
				if(lastPlayerRow == c.getInt(c.getColumnIndexOrThrow(Constants.TPLAYER_KEY)) && 
						lastShipRow == c.getInt(c.getColumnIndexOrThrow(Constants.TSHIP_KEY)))
					isLoaded = true;
			}
		} catch (IllegalArgumentException ex) {
			String msg = "DATABASE ERROR: " + ex.getMessage();
			Log.e("DBSelectGame", msg);
		} finally {
			if (c != null)
				c.close();
			if (db != null)
				db.close();
		}
		return isLoaded;
	}
	
}
