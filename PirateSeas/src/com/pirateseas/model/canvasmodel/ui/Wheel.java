package com.pirateseas.model.canvasmodel.ui;

import com.pirateseas.R;
import com.pirateseas.utils.approach2d.Geometry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
			
			Log.d(TAG, "Pre-modified distance moved on X = " + mMovedPixels);
			
			// Si la cantidad movida es mayor que la anchura del control, ajustar la cantidad al ancho del control
			int maxValue = (int) (this.getWidth() * 0.1);
			if (Math.abs(mMovedPixels) > maxValue)
				mMovedPixels = mMovedPixels < 0 ? (-maxValue) : maxValue;
			
			Log.d(TAG, "Post-modified distance moved on X = " + mMovedPixels);
			
			if (Math.abs(mMovedPixels) >= MODULE_MOVED){
				mDegrees = Geometry.getRotationAngle(startPoint, mCenter, endPoint);
				// Log.d(TAG, "Angle = " + mDegrees + "º");
				
				setPivotX(mCenter.x);
				setPivotY(mCenter.y);
				setRotation(mDegrees);
			} else {
				mMovedPixels = 0;
				Log.v(TAG, "Ignored mMovedPixels value");
			}
			invalidate();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if(mMovedPixels < MODULE_MOVED)
				performClick();
			mTouched = false;
			//resetWheel();
			invalidate();
			break;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Wheel [mDegrees=" + mDegrees + ", mCenter=" + mCenter + "]";
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void resetWheel() {
		mDegrees = 0f;
		mMovedPixels = 0;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(this.getResources().getDrawable(R.drawable.ico_wheel, null));
		} else {
			setImage(this.getResources().getDrawable(R.drawable.ico_wheel));
		}
	}

	public boolean isBeingTouched() {
		return mTouched;
	}

	public void rotateWheel() {
		setRotation(mDegrees);
	}

}