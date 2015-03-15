package com.pirateseas.model.canvasmodel.game.entity;

import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.BasicModel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class Entity extends BasicModel{
		
	// Entity Attribs
	protected int entityWidth;
	protected int entityHeight;
	protected int entityLength;
	
	protected Point entityCoordinates;
		
	// Common attribs
	protected ShipType sType;
	protected int mHealthPoints = 0;
	protected int mSpeed = 0;	
		
	public Entity(Context context, double x, double y, double canvasWidth, double canvasHeight, Point eCoords, int eWidth, int eHeight, int eLength){
		super(context, x, y, canvasWidth, canvasHeight);
		
		this.entityWidth = eWidth;
		this.entityHeight = eHeight;
		this.entityLength = eLength;
		
		this.entityCoordinates = eCoords;
		
		mSpeed = Constants.ENTITY_SPEED[0];
	}
	
	public boolean intersectionWithEntity(Entity other){
		boolean intersection = false;
		
		if (intersectionToRight(other) || intersectionToLeft(other))
			intersection = true;
		
		// TODO Posible problema con coordenadas y y z
		
		return intersection;
	}
	
	private boolean intersectionToRight(Entity other){
		return (entityCoordinates.x + entityWidth / 2) >= (other.entityCoordinates.x - other.entityWidth / 2) ? true : false;
	}
	
	private boolean intersectionToLeft(Entity other){
		return (entityCoordinates.x - entityWidth / 2) <= (other.entityCoordinates.x + other.entityWidth / 2) ? true : false;
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
		if(speedLevel >= 0 && speedLevel <= Constants.ENTITY_SPEED.length)
			mSpeed =  Constants.ENTITY_SPEED[speedLevel];
	}
	
	public int compareTo(Entity other){
		int entityCompass = -1; // 0..359 degrees
		
		// TODO 
		
		return entityCompass;
	}
	
	public boolean isMoving(){
		return mSpeed > 0 ? true : false;
	}
}
