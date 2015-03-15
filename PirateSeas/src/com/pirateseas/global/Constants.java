package com.pirateseas.global;

public class Constants{
	// Tags
	public static final String TAG_SENSOR_LIST = "com.pirateseas.SENSOR_LIST";
	public static final String TAG_DISPLAY_TUTORIAL = "com.pirateseas.DISPLAY_TUTORIAL";
	public static final String TAG_NEW_GAME = "com.pirateseas.NEW_GAME";
	
	// Math factors
	public static final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	
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
	
}