package com.pirateseas.global;

public class Constants{
	// Modes
	private static final int MODE_DEBUG = 0x0;
	@SuppressWarnings("unused")
	private static final int MODE_RELEASE = 0x1;
	
	public static final int MODE = MODE_DEBUG;
	
	public static final int GAMEMODE_ADVANCE = 0x1;
	public static final int GAMEMODE_BATTLE = 0x2;
	
	// Tags
	public static final String TAG_EXE_MODE = "com.pirateseas.EXE_MODE";
	public static final String TAG_SENSOR_LIST = "com.pirateseas.SENSOR_LIST";
	public static final String TAG_LOAD_GAME = "com.pirateseas.LOAD_GAME";
	public static final String TAG_DISPLAY_TUTORIAL = "com.pirateseas.DISPLAY_TUTORIAL";
	public static final String TAG_NEW_GAME = "com.pirateseas.NEW_GAME";
	public static final String TAG_GAME_OVER = "com.pirateseas.GAME_OVER";
	public static final String TAG_PREF_NAME = "com.pirateseas.PREFERENCES";
	public static final String TAG_BRIGHTNESS_LEVEL = "com.pirateseas.BRIGHTNESS";
	public static final String TAG_REMOVE_PREFERENCES = "com.pirateseas.REMOVE_PREFERENCES";
	public static final String TAG_SCREEN_SELECTION_ISLANDDATA = "com.pirateseas.SCREEN_SELECTION_ISLANDDATA";
	public static final String TAG_SCREEN_SELECTION_PLAYERDATA = "com.pirateseas.SCREEN_SELECTION_PLAYERDATA";
	public static final String TAG_LOAD_SCREEN = "com.pirateseas.LOAD_SCREEN";
	public static final String TAG_RANDOM_ENCOUNTER = "com.pirateseas.RANDOM_ENCOUNTER";
	
	// Math factors	
	public static final int BYTES_PER_FLOAT = 4;
	
	// Requests
	public static final int REQUEST_SENSOR_LIST = 0x00;
	public static final int REQUEST_ISLAND = 0x01;
	public static final int REQUEST_REMOVE_PREFERENCES = 0x02;
	public static final int REQUEST_SCREEN_SELECTION = 0x03;
	public static final int REQUEST_LOAD_SCREEN = 0x04;
	
	// Entities
	public static final int STATE_ALIVE = 0;
	public static final int STATE_DEAD = 1;
	
	public static final int SHIP_RELOAD = 2;
	public static final int SHIP_BASIC_RANGE = 3;
	public static final float SHIP_BASIC_DAMAGE = 10;
	
	public static final int SHOT_AMMO_UNLIMITED = -1;
	public static final int SHOT_FIRED = 0;
	public static final int SHOT_FLYING = 1;
	public static final int SHOT_HIT = 2;
	public static final int SHOT_MISSED = 3;
	
	// Bar Types
	public static final int BAR_HEALTH = 0;
	public static final int BAR_EXPERIENCE = 1;
	
	// Global variables	
	public static final int GAME_FPS = 30;				// FPS = Frames-Per-Second
	public static final int GAME_MPIGD = 10;			// MPIGD = Minutes-Per-In-Game-Day
	
	public static final int GAME_STATE_NORMAL = 0;
	public static final int GAME_STATE_PAUSE = 1;
	public static final int GAME_STATE_END = 2;
	
	public static final int LIGHT_THRESHOLD = 600;
	
	public static final int SECONDS_PER_IN_GAME_HOUR = 60;
	public static final int HOURS_PER_DAY = GAME_MPIGD * SECONDS_PER_IN_GAME_HOUR;
	public static final int MILLIS_TO_SECONDS = 1000;
	public static final double MILLIS_TO_SECONDS_INV = Math.pow(MILLIS_TO_SECONDS, -1); 
	public static final double NANOS_TO_SECONDS = Math.pow(10, -9);
	
	public static final String EMPTY_STRING = "";
	public static final String PREF_SENSOR_LIST = "sensorListPref";
	public static final String PREF_PLAYER_TIMESTAMP = "playerTimestampPref";
	public static final String PREF_PLAYER_DAYS = "playerDaysPref";
	public static final String PREF_PLAYER_LEVEL = "playerLevelPref";
	public static final String PREF_PLAYER_GOLD = "playerGoldPref";
	public static final String PREF_PLAYER_XP = "playerExperiencePref";
	public static final String PREF_PLAYER_MAP_PIECES = "playerMapPiecesPref";
	public static final String PREF_SHIP_COORDINATES_X = "shipCoordinatesXPref";
	public static final String PREF_SHIP_COORDINATES_Y = "shipCoordinatesYPref";
	public static final String PREF_SHIP_AMMUNITIONS = "shipAmmunitionPref";
	public static final String PREF_SHIP_HEALTH = "shipHealthPref";
	public static final String PREF_SHIP_TYPE = "shipTypePref";
	
	public static final String PREF_DEVICE_VOLUME = "deviceVolumePref";
	public static final String PREF_DEVICE_HEIGHT_RES = "deviceHeightPref";
	public static final String PREF_DEVICE_WIDTH_RES = "deviceWidthPref";
	public static final String PREF_DEVICE_NOSENSORS = "deviceNoSensorsPref";
	
	public static final String PREF_CONTROL_MODE = "gameControlMode";
	public static final boolean PREF_GAME_SENSORS = false;
	public static final boolean PREF_GAME_TOUCH = true;
	
	public static final String PREF_USE_AMMO_KEYS = "useAmmoKeys";
	
	public static final String FONT_NAME = "TooneyNoodleNF";
	
	public static final String FRONT = "Front";
	public static final String BACK = "Back";
	public static final String RIGHT = "Right";
	public static final String LEFT = "Left";
	
	public static final String ITEMLIST_NATURE = "Nature";
	public static final String NATURE_SHOP = "Shop";
	public static final String NATURE_TREASURE = "Treasure";
	
	// Database
    public static final String DATABASE_TGAME = "t_game";
    public static final String DATABASE_TSHIP = "t_ship";
    public static final String DATABASE_TPLAYER = "t_player";
    
    public static final String TGAME_KEY = "codg";
    public static final String TGAME_TIMESTAMP = "startTime";
    
    public static final String TPLAYER_KEY = "codp";
    public static final String TPLAYER_DAYS = "days";
    public static final String TPLAYER_GOLD = "gold";
    public static final String TPLAYER_EXP = "experience";
    public static final String TPLAYER_MAP_PIECES = "mapPieces";
    
    public static final String TSHIP_KEY = "cods";
    public static final String TSHIP_COORD_X = "coordX";
    public static final String TSHIP_COORD_Y = "coordY";
    public static final String TSHIP_TYPE = "type";
    public static final String TSHIP_HEALTH = "health";
    public static final String TSHIP_AMMO_DEFAULT = "ammoDefault";
    public static final String TSHIP_AMMO_AIMED = "ammoAimed";
    public static final String TSHIP_AMMO_DOUBLE = "ammoDouble";
    public static final String TSHIP_AMMO_SWEEP = "ammoSweep";
	public static final String TSHIP_SELECTED_AMMO = "ammoSelection";
		
	public static boolean isInDebugMode(int mMode) {
		return mMode == Constants.MODE_DEBUG ? true : false;
	}
}