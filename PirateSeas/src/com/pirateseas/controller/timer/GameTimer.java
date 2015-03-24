package com.pirateseas.controller.timer;

public class GameTimer {
	
	private int gameDay;
	private float gameHour;
	
	public GameTimer(){
		gameDay = 0;
		gameHour = 0f;		
	}
	
	public void setTime(float hour){
		float tmpHour = gameHour;
		int tmpDay = 0;
		tmpHour += hour;

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