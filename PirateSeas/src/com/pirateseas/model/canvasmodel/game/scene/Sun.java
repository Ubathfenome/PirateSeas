package com.pirateseas.model.canvasmodel.game.scene;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.pirateseas.R;
import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.BasicModel;

@SuppressWarnings("unused")
public class Sun extends BasicModel{	
	private double sunTraverseRatio;
	private static final String TAG = "Sun";

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public Sun(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasWidth, mCanvasHeight, null);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(context.getResources().getDrawable(R.drawable.txtr_orb_sun, null));
		} else {
			setImage(context.getResources().getDrawable(R.drawable.txtr_orb_sun));
		}
		
		sunTraverseRatio = mCanvasWidth / Constants.GAME_FPS;
	}
	
	public void moveSun(float hour){
		if(hour <= 1)
			x = 0;
		x = hour * sunTraverseRatio;
		// Log.d(TAG, "Current hour is " + hour + " the new xCoord will be " + x);
	}
}