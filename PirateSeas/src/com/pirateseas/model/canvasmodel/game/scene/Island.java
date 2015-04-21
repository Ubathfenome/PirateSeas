package com.pirateseas.model.canvasmodel.game.scene;

import java.util.Random;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;
import com.pirateseas.model.canvasmodel.game.Parallax;

public class Island extends BasicModel{	
	private Random rand;
	private boolean hasShop;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public Island(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasHeight, mCanvasHeight, new Parallax(context, 0, 0));
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(context.getResources().getDrawable(R.drawable.txtr_island, null));
		} else {
			setImage(context.getResources().getDrawable(R.drawable.txtr_island));
		}

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

	@Override
	public String toString() {
		return "Island [rand=" + rand + ", hasShop=" + hasShop + "]";
	}
}
