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
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public Compass (Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasWidth, mCanvasHeight, null);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(context.getResources().getDrawable(R.drawable.txtr_compass, null));
			mImageAux = context.getResources().getDrawable(R.drawable.txtr_compass, null);
		} else {
			setImage(context.getResources().getDrawable(R.drawable.txtr_compass));
			mImageAux = context.getResources().getDrawable(R.drawable.txtr_compass);
		}
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