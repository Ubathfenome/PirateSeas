package com.pirateseas.model.canvasmodel.ui;

import com.pirateseas.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class UIDisplayElement extends View {
	
	private int mValue;
	private boolean mReloading;
	private Paint paint;
	private Drawable mImage;
	private Drawable mImageCancel;
	private TypedArray mArray;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public UIDisplayElement(Context context, int drawableResource, int value){
		super(context);
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		this.mValue = value;
		this.mReloading = false;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			mImage = context.getResources().getDrawable(drawableResource, null);
			mImageCancel = context.getResources().getDrawable(R.drawable.ico_cancel, null);
		} else {
			mImage = context.getResources().getDrawable(drawableResource);
			mImageCancel = context.getResources().getDrawable(R.drawable.ico_cancel);
		}
	}
	
	public UIDisplayElement(Context context) {
		this(context, null);
	}
	
	public UIDisplayElement(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public UIDisplayElement(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		mArray = context.obtainStyledAttributes(attrs, R.styleable.UIDisplayElement, defStyle, 0);
		
		init();
		
		mArray.recycle();
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void init(){
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(40f);
		paint.setStyle(Style.STROKE);
		
		this.mValue = mArray.getInteger(R.styleable.UIDisplayElement_defaultValue, 0);
		this.mImage = getBackground();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			mImageCancel = getResources().getDrawable(R.drawable.ico_cancel, null);
		} else {
			mImageCancel = getResources().getDrawable(R.drawable.ico_cancel);
		}
	}
	
	public int getElementValue(){
		return mValue;
	}
	
	public void setElementValue(int value){
		this.mValue = value;
	}
	
	/**
	 * @return the mReloading
	 */
	public boolean isReloading() {
		return mReloading;
	}

	/**
	 * @param mReloading the mReloading to set
	 */
	public void setReloading(boolean mReloading) {
		this.mReloading = mReloading;
	}

	@Override
	public void onDraw(Canvas canvas){
		mImage.draw(canvas);
		if(mReloading)
			mImageCancel.draw(canvas);
		canvas.drawText(String.valueOf(mValue), mImage.getIntrinsicWidth() / 16, mImage.getIntrinsicHeight() / 2 + 10,paint);
	}

	@Override
	public String toString() {
		return "UIDisplayElement [name = " + this.getClass().getName() + ", mValue=" + mValue + "]";
	}
	
	
}