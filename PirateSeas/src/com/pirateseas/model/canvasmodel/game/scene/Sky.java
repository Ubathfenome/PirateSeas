package com.pirateseas.model.canvasmodel.game.scene;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;

/**
 *
 * 
 * @author p7166421
 * 
 * @see: https://softwyer.wordpress.com/2012/01/21/1009/
 * @see: http://en.wikipedia.org/wiki/Alpha_compositing
 *
 */
@SuppressWarnings("unused")
public class Sky extends BasicModel{
	private static final String TAG = "Sky";
	
	private Drawable mImageAux;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public Sky(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasWidth, mCanvasHeight, null);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(context.getResources().getDrawable(R.drawable.txtr_sky_clear, null));
			mImageAux = context.getResources().getDrawable(R.drawable.txtr_sky_clear, null);
		} else {
			setImage(context.getResources().getDrawable(R.drawable.txtr_sky_clear));
			mImageAux = context.getResources().getDrawable(R.drawable.txtr_sky_clear);
		}		
	}
	
	@Override
	public void drawOnScreen(Canvas canvas){
		yUp = (int) y;
		xLeft = (int) x;
 
        mImage.setBounds(xLeft, yUp, (int) (xLeft + mCanvasWidth), (int) (yUp + mCanvasHeight));
        mImage.setColorFilter(filterValue * FILTER_MASK, PorterDuff.Mode.SRC_ATOP);
        mImageAux.setColorFilter(filterValue * FILTER_MASK, PorterDuff.Mode.SRC_ATOP);
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
	
	public void setFilterValue(int value){
		this.filterValue = value;
	}

	@Override
	public String toString() {
		return "Sky [filterValue=" + filterValue + "]";
	}
	
}
