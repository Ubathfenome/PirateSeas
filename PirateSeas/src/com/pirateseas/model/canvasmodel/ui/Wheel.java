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

	private Point startPoint, endPoint;
	private double mDegrees;
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
		mCenter = new Point(0, 0);
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

	public void setDegrees(double degrees) {
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
	public boolean performClick() {
		
		return super.performClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startPoint = new Point((int) event.getX(), (int) event.getY());
			Log.d(TAG, "DOWN touch registered (" + startPoint.x + ", "
					+ startPoint.y + ")");
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			endPoint = new Point((int) event.getX(), (int) event.getY());

			Point u = new Point(endPoint.x - startPoint.x, endPoint.y
					- startPoint.y);
			double distance = Math.hypot(u.x, u.y);
			Log.d(TAG, "Touched distance = " + distance + " pixels");

			mDegrees = Geometry.getRotationAngle(startPoint, mCenter, endPoint);

			setMovedPixels(distance);
			setDegrees(mDegrees);
			setPivotX(mCenter.x);
			setPivotY(mCenter.y);
			setRotation((float) mDegrees);
			invalidate();
			Log.d(TAG, "MOVE/CANCEL/UP touch registered (" + endPoint.x + ", "
					+ endPoint.y + ") [" + mDegrees + "º degrees]");
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

}