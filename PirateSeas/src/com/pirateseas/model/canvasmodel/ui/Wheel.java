package com.pirateseas.model.canvasmodel.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class Wheel extends View {	
	private float mDegrees;
	private Point mCenter;
	private Drawable mImage;
	private int[] location = new int[2];
	
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
	
	private void init(){
		mDegrees = 0f;
		setImage(getBackground());
		Rect imageBounds = mImage.copyBounds();
		int imgWdth = imageBounds.width();
		int imgHght = imageBounds.height();
		getLocationOnScreen(location);
		mCenter = new Point(location[0] + (imgWdth / 2), location[1] + (imgHght / 2));
	}
	
	public Point getCenterPoint(){
		return mCenter;
	}
	
	@Override
	public void draw(Canvas canvas){
		canvas.save();
		canvas.rotate(mDegrees, mCenter.x, mCenter.y);
		super.draw(canvas);
		canvas.restore();
	}
	
	public void setDegrees(float degrees){
		this.mDegrees = degrees;
	}

	public float getDegrees() {
		return mDegrees;
	}

	public Drawable getImage() {
		return mImage;
	}

	public void setImage(Drawable mImage) {
		this.mImage = mImage;
	}
		
}