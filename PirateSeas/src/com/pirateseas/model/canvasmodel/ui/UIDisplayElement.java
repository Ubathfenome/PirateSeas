package com.pirateseas.model.canvasmodel.ui;

import com.pirateseas.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class UIDisplayElement extends View {
	
	private int mValue;
	private Paint paint;
	private Drawable mImage;
	
	public UIDisplayElement(Context context, int drawableResource, int value){
		super(context);
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		this.mValue = value;
		mImage = context.getResources().getDrawable(drawableResource);
	}
	
	public UIDisplayElement(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UIDisplayElement, defStyle, 0);
		
		this.mValue = a.getInteger(R.styleable.UIDisplayElement_defaultValue, 0);
		this.mImage = a.getDrawable(R.styleable.UIDisplayElement_drawableResource);
		
		a.recycle();
	}
	
	public int getElementValue(){
		return mValue;
	}
	
	public void setElementValue(int value){
		this.mValue = value;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		return false;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		mImage.draw(canvas);
		canvas.drawText(String.valueOf(mValue),0f,0f,paint);
	}
}