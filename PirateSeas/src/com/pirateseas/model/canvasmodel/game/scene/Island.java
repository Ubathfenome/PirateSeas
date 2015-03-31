package com.pirateseas.model.canvasmodel.game.scene;

import java.util.Random;

import android.content.Context;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;
import com.pirateseas.model.canvasmodel.game.Parallax;

public class Island extends BasicModel{
	
	private Context context; 
	
	private Random rand;
	private boolean hasShop;
	
	public Island(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasHeight, mCanvasHeight, new Parallax(context, 0, 0));
		
		setImage(context.getResources().getDrawable(R.drawable.txtr_island));

		rand = new Random();
		
		if(rand.nextBoolean())
			hasShop = true;
		else{
			hasShop = false;
		}
	}
	
	public boolean hasShop(){
		return hasShop;
	}
}
