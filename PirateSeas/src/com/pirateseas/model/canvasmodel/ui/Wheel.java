package com.pirateseas.model.canvasmodel.ui;

import com.pirateseas.utils.approach2d.Geometry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Wheel extends View {
	private static final String TAG = "Wheel";
	
	private static final int MODULE_MOVED = 8;
	
	boolean mTouched;
	
	private Point startPoint, endPoint;
	private float mDegrees;
	private double mMovedPixels;
	private Point mCenter;
	private Drawable mImage;

	public Wheel(Context context) {
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
	private void init() {
		mDegrees = 0f;
		mMovedPixels = 0;
		setImage(getBackground());
		mCenter = new Point(this.getWidth() / 2, this.getHeight() / 2);
		setPivotX(mCenter.x);
		setPivotY(mCenter.y);
		mTouched = false;
	}

	public Point getCenterPoint() {
		return mCenter;
	}

	public void setCenterPoint(Point point) {
		this.mCenter = point;
	}

	public double getMovedPixels() {
		return mMovedPixels;
	}

	public void setMovedPixels(double mMovedPixels) {
		this.mMovedPixels = mMovedPixels;
	}

	public void setDegrees(float degrees) {
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

	@Override
	public boolean performClick() {
		//init();
		//setRotation(mDegrees);
		return super.performClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startPoint = new Point((int) event.getX(), (int) event.getY());
			mTouched = true;
			break;
		case MotionEvent.ACTION_MOVE:
			endPoint = new Point((int) event.getX(), (int) event.getY());
			
			mMovedPixels = endPoint.x - startPoint.x;
			
			Log.d(TAG, "Distance moved on X = " + mMovedPixels);
			
			if(mMovedPixels >= MODULE_MOVED){
				mDegrees = Geometry.getRotationAngle(startPoint, mCenter, endPoint);
				Log.d(TAG, "Angle = " + mDegrees + "º");
				
				setRotation(mDegrees);
			}
			
			invalidate();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouched = false;
			if(mMovedPixels < MODULE_MOVED)
				performClick();
			break;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Wheel [mDegrees=" + mDegrees + ", mCenter=" + mCenter + "]";
	}

	public void resetWheel() {
		init();
	}

	public boolean isBeingTouched() {
		return mTouched;
	}

}