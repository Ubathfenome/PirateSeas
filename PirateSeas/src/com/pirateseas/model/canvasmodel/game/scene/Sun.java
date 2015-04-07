package com.pirateseas.model.canvasmodel.game.scene;

import android.content.Context;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class Sun extends BasicModel{
	
	public Sun(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, mCanvasHeight, mCanvasHeight, mCanvasHeight, mCanvasHeight, null);
		
		setImage(context.getResources().getDrawable(R.drawable.txtr_orb_sun));
	}
	
	public void move(){
		
	}
}