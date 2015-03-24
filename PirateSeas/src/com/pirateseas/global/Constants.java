package com.pirateseas.global;

public class Constants{
	// Tags
	public static final String TAG_SENSOR_LIST = "com.pirateseas.SENSOR_LIST";
	public static final String TAG_DISPLAY_TUTORIAL = "com.pirateseas.DISPLAY_TUTORIAL";
	public static final String TAG_NEW_GAME = "com.pirateseas.NEW_GAME";
	public static final String TAG_PREF_NAME = "com.pirateseas.PREFERENCES";
	public static final String TAG_BRIGHTNESS_LEVEL = "com.pirateseas.BRIGHTNESS";
	
	// Math factors
	public static final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	
	public static final int MILLIS_PER_HOUR = 3600000;
	public static final int BYTES_PER_FLOAT = 4;
	
	// Requests
	public static final int REQUEST_SENSOR_LIST = 0x00;
	
	// Entity states
	public static final int STATE_ALIVE = 0;
	public static final int STATE_DEAD = 1;
	
	public static final int SHOT_AMMO_UNLIMITED = -1;
	public static final int SHOT_FIRED = 0;
	public static final int SHOT_FLYING = 1;
	public static final int SHOT_HIT = 2;
	public static final int SHOT_MISSED = 3;
	
	public static final int BAR_HEALTH = 0;
	public static final int BAR_EXPERIENCE = 1;
	
	// Global variables
	public static final int[] ENTITY_SPEED = {0, 2, 5, 10};
	
	public static final int GAME_FPS = 30;
	public static final int GAME_STATE_NORMAL = 0;
	public static final int GAME_STATE_PAUSE = 1;
	public static final int GAME_STATE_END = 2;
	
	public static final String EMPTY_STRING = "";
	public static final String PREF_SENSOR_LIST = "sensorListPref";
	public static final String PREF_PLAYER_DAYS = "playerDaysPref";
	public static final String PREF_PLAYER_LEVEL = "playerLevelPref";
	public static final String PREF_PLAYER_GOLD = "playerGoldPref";
	public static final String PREF_PLAYER_XP = "playerExperiencePref";
	public static final String PREF_SHIP_COORDINATES_X = "shipCoordinatesXPref";
	public static final String PREF_SHIP_COORDINATES_Y = "shipCoordinatesYPref";
	public static final String PREF_SHIP_AMMUNITION = "shipAmmunitionPref";
	public static final String PREF_SHIP_HEALTH = "shipHealthPref";
	public static final String PREF_SHIP_TYPE = "shipTypePref";
	
	public static final String DEVICE_HEIGHT_RES = "deviceHeightPref";
	public static final String DEVICE_WIDTH_RES = "deviceWidthPref";
	public static final String FONT_NAME = "TooneyNoodleNF";
	
	public static final int SHIP_RELOAD_SHORT = 0;
	public static final int SHIP_RELOAD_MEDIUM = 1;
	public static final int SHIP_RELOAD_LONG = 2;
	
	
	
	
}