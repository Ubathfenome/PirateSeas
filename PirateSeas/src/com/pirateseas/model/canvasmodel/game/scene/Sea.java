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
		super(context, x, y, mCanvasWidth, mCanvasHeight, null);
		
		setImage(context.getResources().getDrawable(R.drawable.blue_water_texture));
		mImageAux = context.getResources().getDrawable(R.drawable.blue_water_texture);
	}
	
	@Override
	public void drawOnScreen(Canvas canvas){
		yUp = (int) y;
		xLeft = (int) x;
 
        mImage.setBounds(xLeft, yUp, (int)(xLeft + mCanvasWidth), (int)(yUp + mCanvasHeight));
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
