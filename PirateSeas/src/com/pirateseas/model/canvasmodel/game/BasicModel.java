package com.pirateseas.model.canvasmodel.game;

import com.pirateseas.utils.ResolutionAdapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class BasicModel{
	protected Context context;
    protected double mCanvasHeight;
    protected double mCanvasWidth;

	// Model properties
    protected double x;
    protected double y;
    protected int mHeight;
    protected int mWidth;
    protected Drawable mImage;
	
    protected ResolutionAdapter ra;
	
    protected int yUp;
    protected int xLeft;


	public BasicModel(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
				
		ra = new ResolutionAdapter(context, (int) mCanvasWidth, (int) mCanvasHeight);
		
		this.context = context;
        this.x = ra.x(x); 
        this.y = ra.y(y);
        this.mWidth = ra.width((int) mCanvasWidth);
        this.mHeight = ra.height((int) mCanvasHeight);
        this.mCanvasHeight = mCanvasHeight;
        this.mCanvasWidth = mCanvasWidth;
	}
	
	/**
     * Draws on the screen the image of the model
     * 
     * @param canvas
     */
    public void drawOnScreen(Canvas canvas) {
        yUp = (int) y - mHeight / 2;
        xLeft = (int) x - mWidth / 2;
 
        mImage.setBounds(xLeft, yUp, xLeft + mWidth, yUp + mHeight);
        mImage.draw(canvas);
    }
	
	public Drawable getImage() {
        return mImage;
    }
 
    public void setImage(Drawable image) {
        this.mImage = image;
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