package com.pirateseas.utils.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;

import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.global.Constants;
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

	public static boolean saveGame(Context context, Player player, Ship ship){
		
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
		editor.putInt(Constants.PREF_SHIP_AMMUNITION, ship.getAmmunition());
		editor.putInt(Constants.PREF_SHIP_HEALTH, ship.getHealth());
		editor.putInt(Constants.PREF_SHIP_TYPE, ship.getType().ordinal());
		
		res = editor.commit();
		
		return res;
	}
	
	public static boolean loadGame(Context context, Player player, Ship ship){
		
		SharedPreferences mPreferences = context.getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE);
		
		player.setPassedDays(mPreferences.getInt(Constants.PREF_PLAYER_DAYS, 0));
		player.setGold(mPreferences.getInt(Constants.PREF_PLAYER_GOLD, 0));
		player.setExperience(mPreferences.getInt(Constants.PREF_PLAYER_XP, 0));
		player.setMapPieces(mPreferences.getInt(Constants.PREF_PLAYER_MAP_PIECES, 0));
		
		helperPlayer = player;
		
		Point p = new Point(mPreferences.getInt(Constants.PREF_SHIP_COORDINATES_X, 0), mPreferences.getInt(Constants.PREF_SHIP_COORDINATES_Y, 0));
		int ammo = mPreferences.getInt(Constants.PREF_SHIP_AMMUNITION, 0);
		int hp = mPreferences.getInt(Constants.PREF_SHIP_HEALTH, 1);
		ShipType st = ShipType.values()[mPreferences.getInt(Constants.PREF_SHIP_TYPE, 0)];
		ship = new Ship(context, ship, st, p, 2, 3, 5, hp, ammo);
			
		helperShip = ship;
		
		return true;
	}
	
}
