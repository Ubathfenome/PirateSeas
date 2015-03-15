package com.pirateseas.model.canvasmodel.game.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class Sea extends BasicModel{
	
	private Drawable mImageAux;
	
	public Sea(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasWidth, mCanvasHeight);
		
		setImage(context.getResources().getDrawable(R.drawable.blue_water_texture));
		mImageAux = context.getResources().getDrawable(R.drawable.blue_water_texture);
	}
	
	public void move(double length){
		x = x + length * (-1);
		if ( x > mCanvasWidth + mWidth / 2){
			x = 0 - mWidth / 2;
		} 
		if ( x < 0 - mWidth / 2){
			x = mCanvasWidth + mWidth / 2;
		}
	}
	
	@Override
	public void drawOnScreen(Canvas canvas){
		yUp = (int) y - mHeight / 2;
        xLeft = (int) x - mWidth / 2;
 
        mImage.setBounds(xLeft, yUp, xLeft + mWidth, yUp + mHeight);
        mImage.draw(canvas);
		
		// Si la xLeft no es cero 0 
		// Es necesario pintar un fondo auxiliar por la derecha o la izquierda. 
		if (xLeft < 0) { 
			mImageAux.setBounds((int) (xLeft + mCanvasWidth), yUp, (int) (xLeft + mCanvasWidth) + mWidth, yUp + mHeight);
			mImageAux.draw(canvas);
		} else if (xLeft > 0) { 
			mImageAux.setBounds((int) (xLeft - mCanvasWidth), yUp, (int) (xLeft - mCanvasWidth) + mWidth, yUp + mHeight);
			mImageAux.draw(canvas);
		}
	}
}
