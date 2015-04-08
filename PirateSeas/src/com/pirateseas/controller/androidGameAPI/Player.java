package com.pirateseas.controller.androidGameAPI;

public class Player{
	
	// @see: http://developer.android.com/distribute/stories/games.html
	// @see: https://developers.google.com/games/services/common/concepts/savedgames
	
	private int level = 0;
	private int gold = 0;
	private int experience = 0;
	private int passedDays = 0;
	private int mapPieces = 0;
	
	public Player(){
		this.level = 0;
		this.gold = 0;
		this.experience = 0;
		this.passedDays = 0;
		this.mapPieces = 0;
	}
	
	public Player(int level, int gold, int xp, int days, int mapPieces){
		this.level = level;
		this.gold = gold;
		this.experience = xp;
		this.passedDays = days;
		this.mapPieces = mapPieces;
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
	
	public void addGold(int gold) {
		this.gold += gold > 0 ? gold : 0;
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
	
	public void addExperience(int experience) {
		this.experience += experience > 0 ? experience : 0;
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

	public int getMapPieces() {
		return mapPieces;
	}

	public void setMapPieces(int mapPieces) {
		this.mapPieces = mapPieces;
	}
	
	public void addMapPiece(){
		this.mapPieces++;
	}

	@Override
	public String toString() {
		return "Player [level=" + level + ", gold=" + gold + ", experience="
				+ experience + ", passedDays=" + passedDays + ", mapPieces="
				+ mapPieces + "]";
	}
}