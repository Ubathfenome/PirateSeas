package com.pirateseas.model.canvasmodel.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class StatBar extends BasicModel {
	
	private int mType;
	
	private int maxValue;
	private int currentValue;
	
	private static final int BAR_LENGHT = 150;
	
	public StatBar(Context context, double x, double y, double mCanvasWidth, double mCanvasHeight, int type){
		super(context, x, y, mCanvasWidth, mCanvasHeight, null);
		this.mType = type;
	}
	
	public StatBar(Context context, double x, double y, double mCanvasWidth, double mCanvasHeight, int maxValue, int currentValue, int type){
		super(context, x, y, mCanvasWidth, mCanvasHeight, null);
		this.maxValue = maxValue;
		this.currentValue = currentValue;
		
		this.mType = type;
	}

	@Override
	public void drawOnScreen(Canvas canvas){
		int startXPoint = (int) x;
		int yValue = (int) y;
		int endXPoint = startXPoint + BAR_LENGHT;
		int separation = 2;
		
		Paint mPaintLine = new Paint();
		
		canvas.save();
		
		// Bar background
		mPaintLine.setColor(Color.BLACK);
		mPaintLine.setStrokeWidth(15);
		canvas.drawLine(startXPoint, yValue, endXPoint, yValue, mPaintLine);
		
		// Bar max value
		if(mType == Constants.BAR_HEALTH)
			mPaintLine.setColor(Color.RED);
		else if (mType == Constants.BAR_EXPERIENCE)
			mPaintLine.setColor(Color.YELLOW);
		mPaintLine.setStrokeWidth(10);
		canvas.drawLine(startXPoint + separation, yValue, endXPoint - separation, yValue, mPaintLine);
		
		// Bar current value
		if(mType == Constants.BAR_HEALTH)
			mPaintLine.setColor(Color.BLUE);
		else if (mType == Constants.BAR_EXPERIENCE)
			mPaintLine.setColor(Color.GREEN);
		mPaintLine.setStrokeWidth(10);
		
		double unitBar = BAR_LENGHT / 100.0f;
		double completionPercentage = (currentValue * 100.0f) / maxValue;
		double value = (startXPoint + (unitBar * completionPercentage)) - separation;
		canvas.drawLine(startXPoint + separation, yValue, (float) value, yValue, mPaintLine);
		
		canvas.restore();
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public int getType() {
		return mType;
	}

	public int getMaxValue() {
		return maxValue;
	}

	@Override
	public String toString() {
		return "StatBar [name=" + this.getClass().getName() + ", mType=" + mType + ", maxValue=" + maxValue
				+ ", currentValue=" + currentValue + "]";
	}

	public void setMaxValue(int nextLevelThreshold) {
		this.maxValue = nextLevelThreshold;		
	}
	
}