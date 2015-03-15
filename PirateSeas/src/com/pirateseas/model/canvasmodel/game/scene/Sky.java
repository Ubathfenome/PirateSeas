package com.pirateseas.model.canvasmodel.game.scene;

import android.content.Context;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class Sky extends BasicModel{
	
	public Sky(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasHeight, mCanvasHeight);
		
		setImage(context.getResources().getDrawable(R.drawable.txtr_sky_clear));
	}
	
	
	
}
