package com.pirateseas.model.canvasmodel.game.scene;

import android.content.Context;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class Compass extends BasicModel {
	
	public Compass (Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasWidth, mCanvasHeight);
		setImage(context.getResources().getDrawable(R.drawable.txtr_compass));
	}
	
	
}