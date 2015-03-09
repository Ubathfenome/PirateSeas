package com.pirateseas.model.canvasmodel;

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
    protected int height;
    protected int width;
    protected Drawable image;
	
    //protected ResolutionAdapter ra;
	
    private int yUp;
    private int xLeft;


	public BasicModel(Context context, double x, double y, double mCanvasWidth,
            double mCanvasHeight){
			
		//ra = new ResolutionAdapter((int) mCanvasWidth, (int) mCanvasHeight);
		
		this.context = context;
        //this.x = ra.x(x);
        //this.y = ra.y(y);
        this.mCanvasHeight = mCanvasHeight;
        this.mCanvasWidth = mCanvasWidth;
	}
	
	/**
     * Draws on the screen the image of the model
     * 
     * @param canvas
     */
    public void drawOnScreen(Canvas canvas) {
        yUp = (int) y - height / 2;
        xLeft = (int) x - width / 2;
 
        image.setBounds(xLeft, yUp, xLeft + width, yUp + height);
        image.draw(canvas);
    }
	
	public Drawable getImage() {
        return image;
    }
 
    public void setImage(Drawable image) {
        this.image = image;
    }
 
    public Rect getBounds() {
        return new Rect(xLeft, yUp, xLeft + width, yUp + height);
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
        return height;
    }
 
    public void setHeight(int height) {
        this.height = height;
    }
 
    public int getWidth() {
        return width;
    }
 
    public void setWidth(int width) {
        this.width = width;
    }

}