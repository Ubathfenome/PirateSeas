package com.pirateseas.model.canvasmodel.ui;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Throttle extends View {
	
	private int maxLevels;
	private int mLevel;
	private Drawable mImageBase = null;
	private Drawable mImageStick = null;
	private Drawable mImageHandle = null;
	
	private Point mStartPoint;
	private Point mLastPoint;
	
	private static final int[] Y_COORDS = {0, 5, 10, 15};
	
	private static final int MODULE_MOVED = 5;
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
			
		if(!isInEditMode()){
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
				mImageBase = getResources().getDrawable(R.drawable.ico_throttle_base, null);
				mImageStick = getResources().getDrawable(R.drawable.ico_throttle_stick, null);
				mImageHandle = getResources().getDrawable(R.drawable.ico_throttle_handle, null);
			} else {
				mImageBase = getResources().getDrawable(R.drawable.ico_throttle_base);
				mImageStick = getResources().getDrawable(R.drawable.ico_throttle_stick);
				mImageHandle = getResources().getDrawable(R.drawable.ico_throttle_handle);
			}
		} else {
			mImageBase = getBackground();
			mImageStick = getBackground();
			mImageHandle = getBackground();
		}
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				mStartPoint = new Point((int)event.getX(), (int)event.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				mLastPoint = new Point((int)event.getX(), (int)event.getY());
				String movement = getMovementPosition();
				
				Log.d(TAG, "Registered Throttle movement: " + movement);
				if (mLevel < maxLevels && movement.equals(Constants.FRONT))
					mLevel++;
				else if (mLevel > 0 && movement.equals(Constants.BACK))
					mLevel--;
				break;
			case MotionEvent.ACTION_UP:
				break;
		}
		return true;
	}
	
	private String getMovementPosition(){
		String direction = null;
		boolean changed  = false;
		
		if((mStartPoint.x - mLastPoint.x) > MODULE_MOVED)
		{
			direction = Constants.LEFT;
			changed = true;
		}else if((mStartPoint.x - mLastPoint.x) > -MODULE_MOVED)
		{
			changed = true;
			direction = Constants.RIGHT;
		}else if((mStartPoint.y - mLastPoint.y) > MODULE_MOVED)
		{
			changed = true;
			direction = Constants.BACK;
		}else if((mStartPoint.y - mLastPoint.y) > -MODULE_MOVED)
		{
			changed = true;
			direction = Constants.FRONT;
		}
		
		if(changed)
		{
			mStartPoint = mLastPoint;
		}
		return direction.toString();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		// TODO Check display drawables
		
		mImageBase.draw(canvas);
		
		int yCoord = Y_COORDS[mLevel];
		
		switch(mLevel){
			case 0:
				mImageStick = moveDrawable(yCoord, mImageStick);
				mImageHandle = moveDrawable(yCoord, mImageHandle);
				break;
			case 1:
				mImageStick = moveDrawable(yCoord + 5, mImageStick);
				mImageHandle = moveDrawable(yCoord + 7, mImageHandle);
				break;
			case 2:
				mImageStick = moveDrawable(yCoord + 7, mImageStick);
				mImageHandle = moveDrawable(yCoord + 10, mImageHandle);
				break;
			case 3:
				mImageStick = moveDrawable(yCoord + 10 , mImageStick);
				mImageHandle = moveDrawable(yCoord + 15, mImageHandle);
				break;
		}
		mImageStick.draw(canvas);
		mImageHandle.draw(canvas);
		
		invalidate();
	}
	
	private Drawable moveDrawable(int y, Drawable image) {
		Rect bounds = image.copyBounds();
		bounds.top += y;
		bounds.bottom += y;
		image.setBounds(bounds);
		return image;
	}

	@Override
	public String toString() {
		return "Throttle [maxLevels=" + maxLevels + ", mLevel=" + mLevel
				+ ", mStartPoint=" + mStartPoint + ", mLastPoint=" + mLastPoint
				+ "]";
	}
	
	
}