package com.pirateseas.model.canvasmodel.game.entity;

import com.pirateseas.R;

public enum Ammunitions {
	DEFAULT(R.drawable.txtr_ammo_default, "DEFAULT"),
	AIMED(R.drawable.txtr_ammo_aimed, "AIMED"),
	DOUBLE(R.drawable.txtr_ammo_double, "DOUBLE"),
	SWEEP(R.drawable.txtr_ammo_sweep, "SWEEP");
	
	private final int mDrawableValue;
	private final String mName;
	
	Ammunitions(int drawableValue, String name) {
		this.mDrawableValue = drawableValue;
		this.mName = name;
	}
	
	public String getName(){
		return mName;
	}
	
	public int drawableValue(){
		return mDrawableValue;
	}
}
