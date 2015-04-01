package com.pirateseas.model.canvasmodel.game.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class Compass extends BasicModel {
	
	private Drawable mImageAux;
	
	public Compass (Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasWidth, mCanvasHeight, null);
		setImage(context.getResources().getDrawable(R.drawable.txtr_compass));
		mImageAux = context.getResources().getDrawable(R.drawable.txtr_compass);
	}
	
	@Override
	public void drawOnScreen(Canvas canvas){
		yUp = (int) y;
		xLeft = (int) x;
 
        mImage.setBounds(xLeft, yUp, (int)(xLeft + mWidth), (int)(yUp + mHeight));
        mImage.draw(canvas);
		
		// Si la xLeft no es cero 0 
		// Es necesario pintar un fondo auxiliar por la derecha o la izquierda. 
		if (xLeft < 0) { 
			mImageAux.setBounds((int) (xLeft + mWidth), yUp, (int) (xLeft + mWidth) + mWidth, yUp + mHeight);
			mImageAux.draw(canvas);
		} else if (xLeft > 0) { 
			mImageAux.setBounds((int) (xLeft - mWidth), yUp, (int) (xLeft - mWidth) + mWidth, yUp + mHeight);
			mImageAux.draw(canvas);
		}
	}
}