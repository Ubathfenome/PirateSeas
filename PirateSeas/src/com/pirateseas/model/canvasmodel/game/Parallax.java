package com.pirateseas.model.canvasmodel.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class Parallax {
	
	Drawable imageBase = null, imageTop = null;
	public static final float SPEED_BASE = 1.2f;
	public static final float SPEED_TOP = 8f;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public Parallax(Context context, int resourceBase, int resourceTop){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			if(resourceBase != 0)
				imageBase = context.getResources().getDrawable(resourceBase, null);
			if(resourceTop != 0)
				imageTop = context.getResources().getDrawable(resourceTop, null);
		} else {
			if(resourceBase != 0)
				imageBase = context.getResources().getDrawable(resourceBase);
			if(resourceTop != 0)
				imageTop = context.getResources().getDrawable(resourceTop);
		}
	}
	
	public Drawable[] getLayers(){
		Drawable[] layers = new Drawable[2];
		layers[0] = imageBase;
		layers[1] = imageTop;
		return layers;
	}

}
