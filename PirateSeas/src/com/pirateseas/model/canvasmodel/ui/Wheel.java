package com.pirateseas.model.canvasmodel.ui;

import com.pirateseas.utils.approach2d.DrawableHelper;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class Wheel extends View {
	
	private Context context;
	
	private float mDegrees;
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
		
		this.context = context;
		
		init();
		
	}
	
	private void init(){
		mDegrees = 0f;
		mImage = getBackground();
		Rect imageBounds = mImage.copyBounds();
		mCenter = new Point(imageBounds.centerX(), imageBounds.centerY());
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
		
}