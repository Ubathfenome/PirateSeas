package com.pirateseas.model.canvasmodel.game.entity;

import com.pirateseas.R;

public enum ShipType {
	LIGHT (R.drawable.txtr_ship_light, 100, 3, 1f, 5),
	MEDIUM (R.drawable.txtr_ship_medium, 250, 2, 1.5f, 2),
	HEAVY (R.drawable.txtr_ship_heavy, 400, 1, 2f, 1);
	
	private final int mDrawableValue;
	private final int mDefaultHealthPoints;
	private final int mRangeMultiplier;
	private final float mPowerMultiplier;
	private final int mSpeed;
	
	ShipType (int drawableValue, int healthPoints, int range, float power, int speed){
		this.mDrawableValue = drawableValue;
		this.mDefaultHealthPoints = healthPoints;
		this.mRangeMultiplier = range;
		this.mPowerMultiplier = power;
		this.mSpeed = speed;
	}
	
	public int drawableValue(){
		return mDrawableValue;
	}
	
	public int defaultHealthPoints(){
		return mDefaultHealthPoints;
	}
	
	public int rangeMultiplier(){
		return mRangeMultiplier;
	}
	
	public float powerMultiplier(){
		return mPowerMultiplier;
	}

	public int getSpeed() {
		return mSpeed;
	}
}
