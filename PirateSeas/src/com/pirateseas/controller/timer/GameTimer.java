package com.pirateseas.controller.timer;

import com.pirateseas.global.Constants;

public class GameTimer {
	
	private int gameDay;
	private float gameHour;
	
	public GameTimer(){
		gameDay = 0;
		gameHour = 0f;		
	}
	
	public void updateHour(){
		float tmpHour = gameHour;
		tmpHour += 1/((System.currentTimeMillis() % Constants.MILLIS_PER_HOUR)+1);

		if (tmpHour / 24 > 1)
			gameDay++;
				
		this.gameHour = tmpHour % 24;
	}
	
	public float getHour(){
		return gameHour;
	}
	
	public int getDay(){
		return gameDay;
	}
}