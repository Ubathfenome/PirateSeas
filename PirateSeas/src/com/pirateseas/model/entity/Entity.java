package com.pirateseas.model.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.pirateseas.utils.Geometry.Point;
import com.pirateseas.utils.Geometry.Vector;

public class Entity {
	
	protected Drawable mImage;
	
	protected static final int[] SPEEDS = {0, 2, 5, 10};
	
	// Position attribs
	protected Point mCoords;
	protected Vector mDirection;
	private float mWidth;
	private float mHeight;
	private float mDepth;
	
	protected final float[] mSize = {mWidth, mHeight, mDepth};
		
	// Common attribs
	protected ShipType sType;
	protected int mHealthPoints = 0;
	protected int mSpeed = 0;	
		
	public Entity(Context context, Point coordinates, Vector direction, float width, float height, float depth){
		this.mCoords = coordinates;
		this.mDirection = direction;
		this.mWidth = width;
		this.mHeight = height;
		this.mDepth = depth;
		mSize[0] = width;
		mSize[1] = height;
		mSize[2] = depth;	
	}
	
	public void draw(Canvas canvas) {
		mImage.draw(canvas);
    }
	
	public boolean intersectionWithEntity(Entity other){
		boolean intersection = false;
		
		// TODO
		
//		// Intersection with front panel
//		if (mCoords.z + mWidth / 2 <= other.Position.z - other.getSize()[2] / 2 && 
//				mCoords.z - mWidth / 2 <= other.Position.z + other.getSize()[2] / 2)
//			intersection = true;
//		// Intersection with right panel
//		if (mCoords.x + mWidth / 2 <= other.Position.x - other.getSize()[0] / 2 && 
//		/* TODO Verificar que 'other' no esté a la izquierda de la entidad*/)
//			intersection = true;
//		// Intersection with back panel
//		if (mCoords.z - mWidth / 2 <= other.Position.z + other.getSize()[2] / 2 && 
//		/* TODO Verificar que 'other' no esté delante de la entidad*/)
//			intersection = true;
//		// Intersection with left panel
//		if (mCoords.x - mWidth / 2 >= other.Position.x + other.getSize()[0] / 2 && 
//		/* TODO Verificar que 'other' no esté a la derecha de la entidad*/)
//			intersection = true;
		
		return intersection;
	}
	
	public void gainHealth(int points){
		if(points > 0)
			mHealthPoints += points;
		else
			throw new IllegalArgumentException("Encontrado valor de puntos negativo al modificar HelthPoints");
	}
	
	public void looseHealth(int points){
		if(points > 0)
			mHealthPoints -= points;
		else
			throw new IllegalArgumentException("Encontrado valor de puntos negativo al modificar HelthPoints");
	}
	
	public boolean isAlive(){
		return mHealthPoints > 0 ? true : false;
	}
	
	public void changeSpeed(int speedLevel){
		if(speedLevel >= 0 && speedLevel <= SPEEDS.length)
			mSpeed = SPEEDS[speedLevel];
	}
	
	public boolean[] compareTo(Entity other){
		boolean[] entityCompass = new boolean[4]; // North, East, South, West
		
		// Initialize
		for(boolean c : entityCompass)
			c = false;
		
		// TODO 
		
		return entityCompass;
	}
	
	public float[] getSize(){
		return mSize;
	}

	public Vector getDirection(){
		return mDirection;
	}
	
	public boolean isMoving(){
		return mSpeed > 0 ? true : false;
	}
}
