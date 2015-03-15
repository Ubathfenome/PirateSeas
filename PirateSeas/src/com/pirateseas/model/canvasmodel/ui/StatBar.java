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
	
	public StatBar(Context context, double x, double y, double mCanvasWidth, double mCanvasHeight, int type){
		super(context, x, y, mCanvasWidth, mCanvasHeight);
		this.mType = type;
	}
	
	public StatBar(Context context, double x, double y, double mCanvasWidth, double mCanvasHeight, int maxValue, int currentValue, int type){
		super(context, x, y, mCanvasWidth, mCanvasHeight);
		this.maxValue = maxValue;
		this.currentValue = currentValue;
		
		this.mType = type;
	}

	@Override
	public void drawOnScreen(Canvas canvas){
		Paint mPaintLine = new Paint();
		
		// Bar background
		mPaintLine.setColor(Color.BLACK);
		mPaintLine.setStrokeWidth(20);
		canvas.drawLine((int)x, (int) y - 10, (int) mCanvasWidth, (int) y - 10, mPaintLine);
		
		// Bar max value
		if(mType == Constants.BAR_HEALTH)
			mPaintLine.setColor(Color.RED);
		else if (mType == Constants.BAR_EXPERIENCE)
			mPaintLine.setColor(0x0f00ff00);
		mPaintLine.setStrokeWidth(15);
		canvas.drawLine((int) x + 5, (int) y - 10, (int) mCanvasWidth - 2, (int) y - 10, mPaintLine);
		
		// Bar current value
		if(mType == Constants.BAR_HEALTH)
			mPaintLine.setColor(Color.BLUE);
		else if (mType == Constants.BAR_EXPERIENCE)
			mPaintLine.setColor(Color.GREEN);
		mPaintLine.setStrokeWidth(15);
		canvas.drawLine((int) x + 5, (int) y - 10, (int) ((x + 5) + (( mCanvasWidth - 2) / (maxValue + 1)) * currentValue + 1), (int) y - 10, mPaintLine);
	}
	
}