package com.pirateseas.model.canvasmodel.game.entity;

import com.pirateseas.R;

public enum ShipType {
	LIGHT (R.drawable.txtr_ship_light, 100, 3, 1f),
	MEDIUM (R.drawable.txtr_ship_medium, 250, 2, 1.5f),
	HEAVY (R.drawable.txtr_ship_base, 400, 1, 2f);
	
	private final int mDrawableValue;
	private final int mDefaultHealthPoints;
	private final int mRangeMultiplier;
	private final float mPowerMultiplier;
	
	ShipType (int drawableValue, int healthPoints, int range, float power){
		this.mDrawableValue = drawableValue;
		this.mDefaultHealthPoints = healthPoints;
		this.mRangeMultiplier = range;
		this.mPowerMultiplier = power;
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
}
