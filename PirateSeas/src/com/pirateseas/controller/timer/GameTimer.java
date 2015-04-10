package com.pirateseas.controller.timer;

import android.os.SystemClock;

public class GameTimer {
	
	private int gameDay;
	private float gameHour;
	private long lastTimestamp;
	
	public GameTimer(){
		gameDay = 0;
		gameHour = 0f;
		lastTimestamp = 0;
	}
	
	public void updateHour(){
		long ts = SystemClock.elapsedRealtime();
		double tmpHour = gameHour;
		long deltaTs = ts - lastTimestamp;
		long deltaSecs = deltaTs / 1000;	// Real-Life seconds
		double deltaGHours = deltaSecs / 360;// In-Game hours; 1 Real-Life minutes = 1 In-Game hour
		
		lastTimestamp = lastTimestamp == 0 ? ts : deltaTs;
		
		tmpHour += deltaGHours;
		
		gameDay += (tmpHour / 24);
				
		this.gameHour = (float) (tmpHour % 24);
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