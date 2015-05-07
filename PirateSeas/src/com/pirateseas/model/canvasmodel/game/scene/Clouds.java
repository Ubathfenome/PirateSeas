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
	
	private static final double OUTWINDOW_RATIO = 1.5;
	
	private boolean isCloudy;
	private double xTop;
	
	private float speedBase, speedTop;
	
	private static Parallax mParallaxAux;
	private Drawable mImageAux;
	
	public Clouds(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight, boolean cloudy){
		super(context, x, y, mCanvasHeight, mCanvasHeight, mParallaxAux = new Parallax(context, R.drawable.txtr_clouds_light, R.drawable.txtr_clouds_almost_none));
		
		this.xTop = x;
		this.isCloudy = cloudy;
		
		speedBase = Parallax.SPEED_BASE;
		speedTop = Parallax.SPEED_TOP;
		
		setImage(mImageAux = mParallaxAux.getLayers()[1]);
	}
	
	public void heightReposition(int bottomPadding){
		y = -(mHeight - bottomPadding);
	}
	
	public boolean isCloudy() {
		return isCloudy;
	}

	public void setCloudy(boolean isCloudy) {
		this.isCloudy = isCloudy;
	}
	
	public void move(){
		if(x >= (mCanvasWidth * OUTWINDOW_RATIO))
			x = -(mWidth * OUTWINDOW_RATIO);
		if(xTop >= (mCanvasWidth * OUTWINDOW_RATIO))
			xTop = -(mWidth * OUTWINDOW_RATIO);
		
		if(isCloudy)
			xTop += speedTop;
		x += speedBase;
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
        	
        	if(xLeft < 0){
        		mImageAux.setBounds((int) (xLeft + mCanvasWidth), yUp, (int) (xLeft + mCanvasWidth) + mWidth, yUp + mHeight);
    			mImageAux.draw(canvas);
        	} else if(xLeft > 0){
        		mImageAux.setBounds((int) (xLeft - mCanvasWidth), yUp, (int) (xLeft - mCanvasWidth) + mWidth, yUp + mHeight);
    			mImageAux.draw(canvas);
        	}
        } else {
        	Drawable[] parallaxLayers = mParallax.getLayers();
        	parallaxLayers[0].setBounds(xLeft, yUp, xLeft + mWidth, yUp + mHeight);
        	parallaxLayers[1].setBounds((int) xTop, yUp, (int) xTop + mWidth, yUp + mHeight);
			parallaxLayers[0].draw(canvas);
			parallaxLayers[1].draw(canvas);
			
			if(xLeft < 0){
				Drawable[] auxParallaxLayers = mParallaxAux.getLayers();
				auxParallaxLayers[0].setBounds(xLeft + mWidth, yUp, xLeft + mWidth, yUp + mHeight);
				auxParallaxLayers[1].setBounds((int) xTop + mWidth, yUp, (int) xTop + mWidth, yUp + mHeight);
				auxParallaxLayers[0].draw(canvas);
				auxParallaxLayers[1].draw(canvas);
        	} else if(xLeft > 0){
        		Drawable[] auxParallaxLayers = mParallaxAux.getLayers();
				auxParallaxLayers[0].setBounds(xLeft - mWidth, yUp, xLeft - mWidth, yUp + mHeight);
				auxParallaxLayers[1].setBounds((int) xTop - mWidth, yUp, (int) xTop - mWidth, yUp + mHeight);
				auxParallaxLayers[0].draw(canvas);
				auxParallaxLayers[1].draw(canvas);
        	}
        }
    }

	@Override
	public String toString() {
		return "Clouds [isCloudy=" + isCloudy + "]";
	}
    
}
