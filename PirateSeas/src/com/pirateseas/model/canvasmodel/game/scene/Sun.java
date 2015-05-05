package com.pirateseas.model.canvasmodel.game.scene;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import com.pirateseas.R;
import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class Sun extends BasicModel{	
	private double sunTraverseRatio;

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
		
		sunTraverseRatio = mCanvasWidth / Constants.GAME_MPIGD;
	}
	
	public void moveSun(float hour){
		if(hour <= 1)
			x = 0;
		x += ((2 * hour) * sunTraverseRatio);
	}
}