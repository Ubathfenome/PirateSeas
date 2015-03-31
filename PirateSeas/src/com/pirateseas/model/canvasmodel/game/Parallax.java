package com.pirateseas.model.canvasmodel.game;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Parallax {
	
	Drawable imageBase = null, imageTop = null;
	public static final float speedBase = 2f;
	public static final float speedTop = 8f;
	
	public Parallax(Context context, int resourceBase, int resourceTop){
		if(resourceBase != 0)
			imageBase = context.getResources().getDrawable(resourceBase);
		if(resourceTop != 0)
			imageTop = context.getResources().getDrawable(resourceTop);
	}
	
	public Drawable[] getLayers(){
		Drawable[] layers = new Drawable[2];
		layers[0] = imageBase;
		layers[1] = imageTop;
		return layers;
	}

}
