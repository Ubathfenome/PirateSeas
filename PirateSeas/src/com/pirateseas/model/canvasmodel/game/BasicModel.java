package com.pirateseas.model.canvasmodel.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class BasicModel{
	protected static final int FILTER_MASK = (int) Math.pow(2, (2 * 8 + 8));
	
	protected Context context;
    protected double mCanvasHeight;
    protected double mCanvasWidth;

	// Model properties
    protected double x;
    protected double y;
    protected int mHeight;
    protected int mWidth;
    protected Drawable mImage;
    protected Parallax mParallax;
	
    protected int yUp;
    protected int xLeft;
    
    protected int filterValue = 1;


	public BasicModel(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight, Parallax parallax){
		
		this.context = context;
        this.x = x; 
        this.y = y;
        this.mCanvasHeight = mCanvasHeight;
        this.mCanvasWidth = mCanvasWidth;
        
        setParallax(parallax);
	}
	
	public void move(double xLength, double yLength, boolean bounce){
		x = x - xLength;
		y = y - yLength;
		
		if (bounce) {
			if (x > mCanvasWidth) {
				x = mCanvasWidth - xLength;
			}
			if (x < 0) {
				x = 0;
			}

			if (y > mCanvasHeight) {
				y = 0;
			}
			if (y < 0) {
				y = mCanvasHeight;
			}
		} else {
			if (x > mCanvasWidth) {
				x = 0;
			}
			if (x < 0) {
				x = mCanvasWidth;
			}

			if (y > mCanvasHeight) {
				y = 0;
			}
			if (y < 0) {
				y = mCanvasHeight;
			}
		}
	}
	
	/**
     * Draws on the screen the image of the model
     * 
     * @param canvas
     */
    public void drawOnScreen(Canvas canvas) {
        yUp = (int) y;
        xLeft = (int) x;
 
        if (mParallax == null){
        	mImage.setBounds(xLeft, yUp, xLeft + mWidth, yUp + mHeight);
        	mImage.draw(canvas);
        } else {
        	Drawable[] parallaxLayers = mParallax.getLayers();
        	for(int i = 0; i < 2; i++){
        		if(parallaxLayers[i] != null){
        			parallaxLayers[i].setBounds(xLeft, yUp, xLeft + mWidth, yUp + mHeight);
        			parallaxLayers[i].draw(canvas);
        		}
        	}
        }
    }
	
	public Drawable getImage() {
        return mImage;
    }
 
    public void setImage(Drawable image) {
        this.mImage = image;
        this.mWidth = image.getIntrinsicWidth();
        this.mHeight = image.getIntrinsicHeight();
    }
    
    public Parallax getParallax(){
    	return this.mParallax;
    }
    
    public void setParallax(Parallax parallax){
    	this.mParallax = parallax;
    	if(parallax != null){
	    	this.mWidth = parallax.getMaxWidth();
	    	this.mHeight = parallax.getMaxHeight();
    	}
    }
 
    public Rect getBounds() {
        return new Rect(xLeft, yUp, xLeft + mWidth, yUp + mHeight);
    }
	
	public double getX() {
        return x;
    }
 
    public double getY() {
        return y;
    }
 
    public void setX(double x) {
        this.x = x;
    }
 
    public void setY(double y) {
        this.y = y;
    }
 
    public int getHeight() {
        return mHeight;
    }
 
    public void setHeight(int height) {
        this.mHeight = height;
    }
 
    public int getWidth() {
        return mWidth;
    }
 
    public void setWidth(int width) {
        this.mWidth = width;
    }   
    
}