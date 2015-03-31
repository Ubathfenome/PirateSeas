package com.pirateseas.model.canvasmodel.game.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.pirateseas.R;
import com.pirateseas.model.canvasmodel.game.BasicModel;
import com.pirateseas.model.canvasmodel.game.Parallax;

/**
* Extends from View?
* @see: http://developer.android.com/guide/topics/graphics/prop-animation.html#object-animator
*/
public class Clouds extends BasicModel{
	
	private boolean isCloudy;
	private double xTop;
	
	public Clouds(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight, boolean cloudy){
		super(context, x, y, mCanvasHeight, mCanvasHeight, new Parallax(context, R.drawable.txtr_clouds_light, R.drawable.txtr_clouds_almost_none));
		
		this.xTop = x;
		this.isCloudy = cloudy;
		setImage(context.getResources().getDrawable(R.drawable.txtr_clouds_almost_none));
	}
	
	public boolean isCloudy() {
		return isCloudy;
	}

	public void setCloudy(boolean isCloudy) {
		this.isCloudy = isCloudy;
	}
	
	public void move(){
		if(isCloudy)
			xTop += Parallax.speedTop;
		x += Parallax.speedBase;
	}

	/**
     * Draws on the screen the image of the model
     * 
     * @param canvas
     */
    public void drawOnScreen(Canvas canvas) {
        yUp = (int) y;
        xLeft = (int) x;
 
        if (!isCloudy){
        	mImage.setBounds(xLeft, yUp, xLeft + mWidth, yUp + mHeight);
        	mImage.draw(canvas);
        } else {
        	Drawable[] parallaxLayers = mParallax.getLayers();
        	parallaxLayers[0].setBounds(xLeft, yUp, xLeft + mWidth, yUp + mHeight);
        	parallaxLayers[1].setBounds((int) xTop, yUp, (int) xTop + mWidth, yUp + mHeight);
			parallaxLayers[0].draw(canvas);
			parallaxLayers[1].draw(canvas);
        }
    }
}
