package com.pirateseas.model.canvasmodel.ui;

import com.pirateseas.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class UIDisplayElement extends View {
	
	private int mValue;
	private Paint paint;
	private Drawable mImage;
	private TypedArray mArray;
	
	public UIDisplayElement(Context context, int drawableResource, int value){
		super(context);
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		this.mValue = value;
		mImage = context.getResources().getDrawable(drawableResource, null);
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
	
	private void init(){
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(40f);
		paint.setStyle(Style.STROKE);
		
		this.mValue = mArray.getInteger(R.styleable.UIDisplayElement_defaultValue, 0);
		this.mImage = getBackground();
	}
	
	public int getElementValue(){
		return mValue;
	}
	
	public void setElementValue(int value){
		this.mValue = value;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		mImage.draw(canvas);
		// TODO Move text slightly to the left (Center text)
		canvas.drawText(String.valueOf(mValue), mImage.getIntrinsicWidth() / 2 - 20, mImage.getIntrinsicHeight() / 2 + 10,paint);
	}

	@Override
	public String toString() {
		return "UIDisplayElement [name = " + this.getClass().getName() + ", mValue=" + mValue + "]";
	}
	
	
}