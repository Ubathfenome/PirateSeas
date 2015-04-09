package com.pirateseas.controller.androidGameAPI;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.pirateseas.R;
import com.pirateseas.exceptions.NotEnoughGoldException;

/**
 * 
 * @author p7166421
 *
 * @see: http://developer.android.com/distribute/stories/games.html
 * @see: https://developers.google.com/games/services/common/concepts/savedgames
 * @see: 
 *       http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one
 *       -android-activity-to-another-using-intents
 * 
 */
public class Player implements Parcelable {

	private int level = 0;
	private int gold = 0;
	private int experience = 0;
	private int passedDays = 0;
	private int mapPieces = 0;
	private boolean hasCompleteMap = false;

	public Player() {
		this.level = 0;
		this.gold = 0;
		this.experience = 0;
		this.passedDays = 0;
		this.mapPieces = 0;
		this.hasCompleteMap = false;
	}

	public Player(int level, int gold, int xp, int days, int mapPieces,
			boolean map) {
		this.level = level;
		this.gold = gold;
		this.experience = xp;
		this.passedDays = days;
		this.mapPieces = mapPieces;
		this.hasCompleteMap = map;
	}

	public Player(Parcel source) {
		this.level = source.readInt();
		this.gold = source.readInt();
		this.experience = source.readInt();
		this.passedDays = source.readInt();
		this.mapPieces = source.readInt();
		this.hasCompleteMap = source.readInt() == 1 ? true : false;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
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
	 * @param gold
	 *            the gold to set
	 */
	public void setGold(int gold) {
		this.gold = gold;
	}

	public void addGold(int gold) {
		this.gold += gold > 0 ? gold : 0;
	}

	public void useGold(Context context, int gold)
			throws NotEnoughGoldException {
		if (this.gold < gold)
			throw new NotEnoughGoldException(context.getResources().getString(
					R.string.exception_gold));
		else
			this.gold -= gold;
	}

	/**
	 * @return the experience
	 */
	public int getExperience() {
		return experience;
	}

	/**
	 * @param experience
	 *            the experience to set
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
	 * @param passedDays
	 *            the passedDays to set
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

	public void addMapPiece() {
		this.mapPieces++;
		if (mapPieces % 6 == 0) {
			hasCompleteMap = true;
			mapPieces -= 6;
		}
	}

	public boolean hasCompleteMap() {
		return hasCompleteMap;
	}

	public void giveCompleteMap(boolean hasCompleteMap) {
		this.hasCompleteMap = hasCompleteMap;
	}

	@Override
	public String toString() {
		return "Player [level=" + level + ", gold=" + gold + ", experience="
				+ experience + ", passedDays=" + passedDays + ", mapPieces="
				+ mapPieces + "]";
	}

	/**
	 * Ignore this method
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Write your object's data to the passed-in Parcel
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(level);
		out.writeInt(gold);
		out.writeInt(experience);
		out.writeInt(passedDays);
		out.writeInt(mapPieces);
		out.writeInt(hasCompleteMap ? 1 : 0);
	}

	/**
	 * This is used to regenerate your object. All Parcelables must have a
	 * CREATOR that implements these two methods
	 */
	public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {

		@Override
		public Player createFromParcel(Parcel source) {
			return new Player(source);
		}

		@Override
		public Player[] newArray(int size) {
			return new Player[size];
		}
	};
}