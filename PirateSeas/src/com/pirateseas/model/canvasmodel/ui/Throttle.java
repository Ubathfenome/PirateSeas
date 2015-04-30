package com.pirateseas.model.canvasmodel.ui;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Throttle extends View {
	
	private int maxLevels;
	private int mLevel;
	
	private Drawable mImage;
	
	private Point mStartPoint;
	private Point mLastPoint;
	
	private static final int[] Y_COORDS = {0, 5, 10, 15};
	
	private static final String TAG = "Throttle";

	public Throttle(Context context) {
		this(context, null);
	}

	public Throttle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public Throttle(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		init();
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void init() {
		this.mLevel = 0;
		this.maxLevels = Y_COORDS.length;
				
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			mImage = getResources().getDrawable(R.drawable.ico_throttle_0, null);
		} else {
			mImage = getResources().getDrawable(R.drawable.ico_throttle_0);
		}
	}
	
	@SuppressLint({ "ClickableViewAccessibility", "NewApi" })
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				mStartPoint = new Point((int)event.getX(), (int)event.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				mLastPoint = new Point((int)event.getX(), (int)event.getY());
				String movement = getMovementPosition();
				
				if(movement.equals(Constants.FRONT) || movement.equals(Constants.BACK))
					Log.d(TAG, "Registered Throttle movement: " + movement);
				
				if (mLevel < maxLevels - 1 && movement.equals(Constants.FRONT))
					mLevel++;
				else if (mLevel > 0 && movement.equals(Constants.BACK))
					mLevel--;
				
				switch(mLevel){
					case 0:
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
							mImage = getResources().getDrawable(R.drawable.ico_throttle_0, null);
						} else {
							mImage = getResources().getDrawable(R.drawable.ico_throttle_0);
						}
						break;
					case 1:
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
							mImage = getResources().getDrawable(R.drawable.ico_throttle_1, null);
						} else {
							mImage = getResources().getDrawable(R.drawable.ico_throttle_1);
						}
						break;
					case 2:
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
							mImage = getResources().getDrawable(R.drawable.ico_throttle_2, null);
						} else {
							mImage = getResources().getDrawable(R.drawable.ico_throttle_2);
						}
						break;
					case 3:
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
							mImage = getResources().getDrawable(R.drawable.ico_throttle_3, null);
						} else {
							mImage = getResources().getDrawable(R.drawable.ico_throttle_3);
						}
						break;
				}
				
				setBackground(mImage);
				
				invalidate();
				break;
		}
		return true;
	}
	
	private String getMovementPosition(){
		int deltaX = mLastPoint.x - mStartPoint.x;
		int deltaY = mLastPoint.y - mStartPoint.y;

		if (Math.abs(deltaX) > Math.abs(deltaY)) { // Lateral movement
			return deltaX > 0 ? Constants.RIGHT : Constants.LEFT;
		} else { // Vertical movement
			return deltaY > 0 ? Constants.BACK : Constants.FRONT;
		}
	}

	@Override
	public String toString() {
		return "Throttle [maxLevels=" + maxLevels + ", mLevel=" + mLevel
				+ ", mStartPoint=" + mStartPoint + ", mLastPoint=" + mLastPoint
				+ "]";
	}
	
	
}