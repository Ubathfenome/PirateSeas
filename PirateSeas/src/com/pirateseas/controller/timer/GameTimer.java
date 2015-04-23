package com.pirateseas.controller.timer;

import android.os.SystemClock;

public class GameTimer {
	
	private int gameDay;
	private int gameHour;
	private long lastTimestamp;
	
	private static long baseTimestamp;
	
	private static final int SECONDS_PER_IN_GAME_HOUR = 60;
	private static final int MINUTES_PER_IN_GAME_DAY = 10;
	
	public GameTimer(){
		gameDay = 0;
		gameHour = 0;
		lastTimestamp = 0;
		baseTimestamp = 0;
	}
	
	public GameTimer(long bTimestamp){
		this();		
		baseTimestamp = bTimestamp;
	}
	
	public void updateHour(){
		long ts = SystemClock.elapsedRealtime();
		
		long deltaTs;
		double deltaSecs = 0;		// Real-Life seconds
		
		if(lastTimestamp == 0)
			baseTimestamp = ts;
		else{
			deltaTs = ts - baseTimestamp;
			deltaSecs = deltaTs * 0.001;
		}
		
		lastTimestamp = ts;
		
		gameHour = getGameHoursFromSeconds(deltaSecs);
		gameDay = getGameDayFromGameHours(deltaSecs);	
	}

	private int getGameHoursFromSeconds(double realSecs) {
		int inGameHour = (int) (realSecs % SECONDS_PER_IN_GAME_HOUR);
		return inGameHour;
	}
	
	private int getGameDayFromGameHours(double realSecs) {
		float inGameHours = (float) (realSecs / SECONDS_PER_IN_GAME_HOUR);
		int inGameDays = (int) (inGameHours / MINUTES_PER_IN_GAME_DAY);
		return inGameDays;
	}

	public long getBaseTimestamp(){
		return baseTimestamp;
	}
	
	public long getLastTimestamp(){
		return lastTimestamp;
	}
	
	public float getHour(){
		return gameHour;
	}
	
	public int getDay(){
		return gameDay;
	}

	@Override
	public String toString() {
		return "GameTimer [gameDay=" + gameDay + ", gameHour=" + gameHour + "]";
	}
	
	
}