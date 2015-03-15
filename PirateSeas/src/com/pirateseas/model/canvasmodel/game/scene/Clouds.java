package com.pirateseas.model.canvasmodel.game.scene;

import android.content.Context;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;

public class Clouds extends BasicModel{
	
	public Clouds(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight, boolean normal){
		super(context, x, y, mCanvasHeight, mCanvasHeight);
		
		if(normal)
			setImage(context.getResources().getDrawable(R.drawable.txtr_clouds_light));
		else
			setImage(context.getResources().getDrawable(R.drawable.txtr_clouds_almost_none));
	}
}
