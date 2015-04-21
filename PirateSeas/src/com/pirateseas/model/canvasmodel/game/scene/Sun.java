package com.pirateseas.model.canvasmodel.game.scene;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class Sun extends BasicModel{
	
	// TODO Change sun speed value
	private static final double SUN_SPEED = 0;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public Sun(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, mCanvasHeight, mCanvasHeight, mCanvasHeight, mCanvasHeight, null);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(context.getResources().getDrawable(R.drawable.txtr_orb_sun, null));
		} else {
			setImage(context.getResources().getDrawable(R.drawable.txtr_orb_sun));
		}
	}
	
	public void move(){
		x += SUN_SPEED;
	}
}