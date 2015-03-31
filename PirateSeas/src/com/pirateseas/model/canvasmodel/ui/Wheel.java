package com.pirateseas.model.canvasmodel.ui;

import com.pirateseas.R;
import com.pirateseas.utils.approach2d.Geometry;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Wheel extends View {
	
	private float mDegrees;
	private Point mCenter;
	private Drawable mImage;
	
	public Wheel(Context context){
		super(context);
		
		mDegrees = 0f;
		mImage = context.getResources().getDrawable(R.drawable.ico_wheel);
		Rect imageBounds = mImage.copyBounds();
		mCenter = new Point(imageBounds.centerX(), imageBounds.centerY());
	}
	
	public Wheel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		mDegrees = 0f;
		mImage = context.getResources().getDrawable(R.drawable.ico_wheel);
		Rect imageBounds = mImage.copyBounds();
		mCenter = new Point(imageBounds.centerX(), imageBounds.centerY());
	}

	public Wheel(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mDegrees = 0f;
		mImage = context.getResources().getDrawable(R.drawable.ico_wheel);
		Rect imageBounds = mImage.copyBounds();
		mCenter = new Point(imageBounds.centerX(), imageBounds.centerY());
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event){
		Point startPoint = null;
		Point endPoint = null;
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				startPoint = new Point((int)event.getX(),(int)event.getY());
				break;
			case MotionEvent.ACTION_UP:
				endPoint = new Point((int)event.getX(),(int)event.getY());
				break;
		}
		
		mDegrees = Geometry.getRotationAngle(startPoint, mCenter, endPoint);
		
		return true;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		canvas.rotate(mDegrees);
		mImage.draw(canvas);
	}

	public float getDegrees() {
		return mDegrees;
	}
		
}