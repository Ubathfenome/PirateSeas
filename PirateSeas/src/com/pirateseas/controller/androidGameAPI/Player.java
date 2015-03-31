package com.pirateseas.controller.androidGameAPI;

public class Player{
	
	// @see: http://developer.android.com/distribute/stories/games.html
	// @see: https://developers.google.com/games/services/common/concepts/savedgames
	
	private int level = 0;
	private int gold = 0;
	private int experience = 0;
	private int passedDays = 0;
	
	public Player(){
		this.level = 0;
		this.gold = 0;
		this.experience = 0;
		this.passedDays = 0;
	}
	
	public Player(int level, int gold, int xp, int days){
		this.level = level;
		this.gold = gold;
		this.experience = xp;
		this.passedDays = days;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the gold
	 */
	public int getGold() {
		return gold;
	}

	/**
	 * @param gold the gold to set
	 */
	public void setGold(int gold) {
		this.gold = gold;
	}

	/**
	 * @return the experience
	 */
	public int getExperience() {
		return experience;
	}

	/**
	 * @param experience the experience to set
	 */
	public void setExperience(int experience) {
		this.experience = experience;
	}

	/**
	 * @return the passedDays
	 */
	public int getPassedDays() {
		return passedDays;
	}

	/**
	 * @param passedDays the passedDays to set
	 */
	public void setPassedDays(int passedDays) {
		this.passedDays = passedDays;
	}
	
	
}