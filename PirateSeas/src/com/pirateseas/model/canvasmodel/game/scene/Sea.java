package com.pirateseas.model.canvasmodel.game.scene;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class Sea extends BasicModel{
	
	private Drawable mImageAux;
	private static int startingHeight;
	private int degrees;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public Sea(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasWidth, mCanvasHeight, null);
		
		startingHeight = (int) y;
		degrees = 0;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(context.getResources().getDrawable(R.drawable.txtr_water, null));
			mImageAux = context.getResources().getDrawable(R.drawable.txtr_water, null);
		} else {
			setImage(context.getResources().getDrawable(R.drawable.txtr_water));
			mImageAux = context.getResources().getDrawable(R.drawable.txtr_water);
		}
	}
	
	@Override
	public void drawOnScreen(Canvas canvas){
		yUp = (int) y;
		xLeft = (int) x;
 
        mImage.setBounds(xLeft, yUp, (int)(xLeft + mCanvasWidth), (int)(yUp + mCanvasHeight));
        mImage.draw(canvas);
		
		// Si la xLeft no es cero
		// Es necesario pintar un fondo auxiliar por la derecha o la izquierda. 
		if (xLeft < 0) { 
			mImageAux.setBounds((int) (xLeft + mCanvasWidth), yUp, (int) (xLeft + mCanvasWidth) + mWidth, yUp + mHeight);
			mImageAux.draw(canvas);
		} else if (xLeft > 0) { 
			mImageAux.setBounds((int) (xLeft - mCanvasWidth), yUp, (int) (xLeft - mCanvasWidth) + mWidth, yUp + mHeight);
			mImageAux.draw(canvas);
		}

		// Si la yUp no es startingHeight
		// Es necesario pintar un fondo auxiliar por arriba o por abajo.
		if (yUp < startingHeight) { 
			mImageAux.setBounds(xLeft, yUp, (int) mCanvasWidth, (int) mCanvasHeight);
			mImageAux.draw(canvas);
		} else if (yUp > startingHeight) { 
			mImageAux.setBounds(xLeft, startingHeight, (int) mCanvasWidth, (int) mCanvasHeight);
			mImageAux.draw(canvas);
		}
	}
	
	public void turn360degrees(){
		if(degrees == 360){
			degrees = 0;
			return;
		} else {
			degrees += 30;
			turn360degrees();
		}		
	}
	
	@Override
	public void move(double xLength, double yLength, boolean bounce){
		x = x - xLength;
		y = y + Math.abs(yLength);
		
		// Horizontal behavior
		if ( x > mCanvasWidth){
			x = 0;
		} 
		if ( x < 0){
			x = mCanvasWidth;
		}
		
		// Vertical behavior
		if( y > mCanvasHeight || y < startingHeight){
			y = startingHeight;
		}
	}
	
	public void setFilterValue(int value){
		this.filterValue = value;
	}
}
