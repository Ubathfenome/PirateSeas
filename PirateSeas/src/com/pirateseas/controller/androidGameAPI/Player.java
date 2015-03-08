package com.pirateseas.controller.androidGameAPI;

public class Player{
	
	// TODO Revisar si es necesaria esta clase @source: http://developer.android.com/distribute/stories/games.html
	
	private int level = 0;
	private int gold = 0;
	private int experience = 0;
	
	public Player(int level, int gold, int xp){
		this.level = level;
		this.gold = gold;
		this.experience = xp;
	}
}