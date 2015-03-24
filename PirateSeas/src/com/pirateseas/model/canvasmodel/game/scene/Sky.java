package com.pirateseas.model.canvasmodel.game.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;
import com.pirateseas.utils.DrawableHelper;

public class Sky extends BasicModel{
	
	private static final String TAG = "com.pirateseas.SKY";
	
	private Drawable mImageAux;
	
	public Sky(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasWidth, mCanvasHeight);
		
		setImage(context.getResources().getDrawable(R.drawable.txtr_sky_clear));
		mImageAux = context.getResources().getDrawable(R.drawable.txtr_sky_clear);
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
		yUp = 0;
		xLeft = 0;
 
        mImage.setBounds(xLeft, yUp, (int) (xLeft + mCanvasWidth), (int) (yUp + mCanvasHeight));
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
	
	public void rotate(float angle){
		Log.d(TAG, "Rotating sky by " + angle + " degrees");
		this.mImage = DrawableHelper.rotateDrawable(mImage, context, angle);
	}
	
}
