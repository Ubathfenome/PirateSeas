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
import com.pirateseas.utils.approach2d.DrawableHelper;

/**
 *
 * 
 * @author p7166421
 * 
 * @see: https://softwyer.wordpress.com/2012/01/21/1009/
 * @see: http://en.wikipedia.org/wiki/Alpha_compositing
 *
 */
public class Sky extends BasicModel{
	
	private static final String TAG = "com.pirateseas.SKY";
	
	private static final int FILTER_MASK = 2^(2 * 8 + 8);
	
	private Drawable mImageAux;
	private int filterValue = 1;
	
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
        mImage.setColorFilter(filterValue * FILTER_MASK, PorterDuff.Mode.SRC_OVER);
        mImageAux.setColorFilter(filterValue * FILTER_MASK, PorterDuff.Mode.SRC_OVER);
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
	
	public void rotate(float angle){
		Log.d(TAG, "Rotating sky by " + angle + " degrees");
		this.mImage = DrawableHelper.rotateDrawable(mImage, context, angle);
	}

	@Override
	public String toString() {
		return "Sky [filterValue=" + filterValue + "]";
	}
	
}
