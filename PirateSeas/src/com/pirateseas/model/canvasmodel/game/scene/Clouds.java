package com.pirateseas.model.canvasmodel.game.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.pirateseas.R;
import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.BasicModel;
import com.pirateseas.model.canvasmodel.game.Parallax;

/**
* Extends from View?
* @see: http://developer.android.com/guide/topics/graphics/prop-animation.html#object-animator
*/
public class Clouds extends BasicModel{
	
	private static final double OUTWINDOW_RATIO = 1.5;
	private static final int ALPHA_LIMIT = 255;
	
	private double xTop;
	
	private int shakeMoveCount;
		
	public Clouds(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
		super(context, x, y, mCanvasHeight, mCanvasHeight, new Parallax(context, R.drawable.txtr_clouds_light, R.drawable.txtr_clouds_almost_none));
		
		this.xTop = x;
		this.shakeMoveCount = 0;
	}
	
	public void heightReposition(int bottomPadding){
		y = -(mHeight - bottomPadding);
	}
	
	public void move(){
		if(x >= (mCanvasWidth * OUTWINDOW_RATIO))
			x = -(mWidth * OUTWINDOW_RATIO);
		if(xTop >= (mCanvasWidth * OUTWINDOW_RATIO))
			xTop = -(mWidth * OUTWINDOW_RATIO);
		
		xTop += Parallax.SPEED_TOP;
		x += Parallax.SPEED_BASE;
	}

	/**
     * Draws on the screen the image of the model
     * 
     * @param canvas
     */
    public void drawOnScreen(Canvas canvas) {
        yUp = (int) y;
        xLeft = (int) x;
        
        setShakeMoveCount(getShakeMoveCount());
        
    	Drawable[] parallaxLayers = mParallax.getLayers();
    	parallaxLayers[0].setBounds(xLeft, yUp, xLeft + mWidth, yUp + mHeight);
    	parallaxLayers[1].setBounds((int) xTop, yUp, (int) xTop + mWidth, yUp + mHeight);
		parallaxLayers[0].draw(canvas);
		parallaxLayers[1].draw(canvas);
		
		if(xLeft < 0){
			Drawable[] auxParallaxLayers = this.getParallax().getLayers();
			auxParallaxLayers[0].setBounds(xLeft + mWidth, yUp, xLeft + mWidth, yUp + mHeight);
			auxParallaxLayers[1].setBounds((int) xTop + mWidth, yUp, (int) xTop + mWidth, yUp + mHeight);
			auxParallaxLayers[0].draw(canvas);
			auxParallaxLayers[1].draw(canvas);
    	} else if(xLeft > 0){
    		Drawable[] auxParallaxLayers = this.getParallax().getLayers();
			auxParallaxLayers[0].setBounds(xLeft - mWidth, yUp, xLeft - mWidth, yUp + mHeight);
			auxParallaxLayers[1].setBounds((int) xTop - mWidth, yUp, (int) xTop - mWidth, yUp + mHeight);
			auxParallaxLayers[0].draw(canvas);
			auxParallaxLayers[1].draw(canvas);
    	}
        
    }
    
	public int getShakeMoveCount() {
		return shakeMoveCount;
	}

	public void setShakeMoveCount(int shakeMoveCount) {
		this.shakeMoveCount = shakeMoveCount;
		float alphaIndex = (shakeMoveCount / Constants.SHAKE_LIMIT);
		int alpha = (int) ((1 - alphaIndex) * ALPHA_LIMIT);
		this.getParallax().setAlpha(alpha);
	}

	@Override
	public String toString() {
		return "Clouds [shakeMoveCount=" + shakeMoveCount + " / " + Constants.SHAKE_LIMIT + "]";
	}

	public void resetShakes() {
		this.shakeMoveCount = 0;
	}
    
}
