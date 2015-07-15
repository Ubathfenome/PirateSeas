package com.pirateseas.model.canvasmodel.game.scene;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class Compass extends BasicModel {	
	private Drawable mImageAux;
	
	private float mValue;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public Compass (Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasWidth, mCanvasHeight, null);
		
		mValue = 0.0f;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(context.getResources().getDrawable(R.drawable.txtr_compass, null));
			mImageAux = context.getResources().getDrawable(R.drawable.txtr_compass, null);
		} else {
			setImage(context.getResources().getDrawable(R.drawable.txtr_compass));
			mImageAux = context.getResources().getDrawable(R.drawable.txtr_compass);
		}
	}
	
	/**
	 * @return the mValue
	 */
	public float getValue() {
		return mValue;
	}

	/**
	 * @param mValue the mValue to set
	 */
	public void setValue(float value) {
		this.mValue = value;
	}
	
	@Override
	public void move(double xLength, double yLength) {
		super.move(xLength, yLength);
		
		double ratio = 360 / mCanvasWidth;
		double roundedXPos = Math.round(x * ratio);
		int preValue = (int) (roundedXPos - 180);
		
		if (x < (int) (Math.round(mCanvasWidth / 2)))
			mValue =  0 + preValue;
		else
			mValue =  360 - preValue;
	}

	@Override
	public void drawOnScreen(Canvas canvas){
		yUp = (int) y;
		xLeft = (int) x;
		
		// XXX TIP Avoid Overlapping images. Support concurrent behaviour.
 
        mImage.setBounds(xLeft, yUp, (int)(xLeft + mWidth), (int)(yUp + mHeight));
        mImage.draw(canvas);
		
		// Si la xLeft no es cero 0 
		// Es necesario pintar un fondo auxiliar por la derecha o la izquierda. 
		if (xLeft < 0 && xLeft + mWidth <= mCanvasWidth) { 
			mImageAux.setBounds((int) (xLeft + mWidth), yUp, (int) (xLeft + mWidth) + mWidth, yUp + mHeight);
			mImageAux.draw(canvas);
		} else if (xLeft > 0) { 
			mImageAux.setBounds((int) (xLeft - mWidth), yUp, (int) (xLeft - mWidth) + mWidth, yUp + mHeight);
			mImageAux.draw(canvas);
		}
	}
}