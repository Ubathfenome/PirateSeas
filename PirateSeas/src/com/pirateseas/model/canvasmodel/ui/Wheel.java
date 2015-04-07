package com.pirateseas.model.canvasmodel.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class Wheel extends View {	
	private double mDegrees;
	private Point mCenter;
	private Drawable mImage;
	
	public Wheel(Context context){
		this(context, null);
	}

	public Wheel(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public Wheel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		init();
	}
	
	@SuppressLint("NewApi")
	private void init(){
		mDegrees = 0f;
		setImage(getBackground());
		mCenter = new Point(0, 0);
	}
	
	public Point getCenterPoint(){
		return mCenter;
	}
	
	public void setCenterPoint(Point point) {
		this.mCenter = point;		
	}
	
	public void setDegrees(double degrees){
		this.mDegrees = degrees;
	}

	public double getDegrees() {
		return mDegrees;
	}

	public Drawable getImage() {
		return mImage;
	}

	public void setImage(Drawable mImage) {
		this.mImage = mImage;
	}

	@Override
	public String toString() {
		return "Wheel [mDegrees=" + mDegrees + ", mCenter=" + mCenter + "]";
	}
		
}