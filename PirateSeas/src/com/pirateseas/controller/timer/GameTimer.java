package com.pirateseas.controller.timer;

import com.pirateseas.global.Constants;

import android.os.SystemClock;

public class GameTimer {
	
	private int gameDay;
	private int gameHour;
	private long lastTimestamp;
	
	private static long baseTimestamp;
	
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
			deltaSecs = deltaTs * Constants.MILLIS_TO_SECONDS_INV;
		}
		
		lastTimestamp = ts;
		
		gameHour = getGameHoursFromSeconds(deltaSecs);
		gameDay = getGameDayFromGameHours(deltaSecs);	
	}

	private int getGameHoursFromSeconds(double realSecs) {
		int inGameHour = (int) (realSecs % Constants.HOURS_PER_DAY);
		return inGameHour;
	}
	
	private int getGameDayFromGameHours(double realSecs) {
		float inGameHours = (float) (realSecs / Constants.HOURS_PER_DAY);
		int inGameDays = (int) (inGameHours / Constants.GAME_MPIGD);
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