package com.pirateseas.model.canvasmodel.game.entity;

import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.BasicModel;
import com.pirateseas.utils.approach2d.Geometry;

import android.content.Context;
import android.graphics.Point;

public class Entity extends BasicModel{
		
	// Entity Attribs
	protected int entityWidth;
	protected int entityHeight;
	protected int entityLength;
	
	protected int entityDirection; // 0..359 degrees
	
	protected Point entityCoordinates;
	
	private int mStatus = Constants.STATE_DEAD;
		
	// Common attribs
	protected int mHealthPoints = 0;
	protected int mMaxHealth = 0;
	protected int mSpeed = 0;	
		
	public Entity(Context context, double x, double y, double canvasWidth, double canvasHeight, Point eCoords, int eWidth, int eHeight, int eLength){
		super(context, x, y, canvasWidth, canvasHeight, null);
		
		this.entityWidth = eWidth;
		this.entityHeight = eHeight;
		this.entityLength = eLength;
		
		this.entityCoordinates = eCoords;
		
		entityDirection = 90;
		
		mSpeed = Constants.ENTITY_SPEED[0];
	}
	
	public boolean intersectionWithEntity(Entity other){
		boolean intersection = false;
		boolean horizontalInt = false, verticalInt = false;
		
		if (intersectionToRight(other) || intersectionToLeft(other))
			horizontalInt = true;
		
		if (intersectionToFront(other) || intersectionToBack(other))
			verticalInt = true;
		
		if(horizontalInt && verticalInt)
			intersection = true;
		
		return intersection;
	}
	
	private boolean intersectionToBack(Entity other) {
		return ((entityCoordinates.y + entityHeight / 2) >= (other.entityCoordinates.y - other.entityHeight / 2))
				&& ((entityCoordinates.y - entityWidth / 2) < (other.entityCoordinates.y - other.entityHeight / 2)) ? true
				: false;
	}

	private boolean intersectionToFront(Entity other) {
		return ((entityCoordinates.y - entityHeight / 2) <= (other.entityCoordinates.y + other.entityHeight / 2))
				&& ((entityCoordinates.y + entityWidth / 2) > (other.entityCoordinates.y + other.entityHeight / 2)) ? true
				: false;
	}

	private boolean intersectionToRight(Entity other) {
		return ((entityCoordinates.x + entityWidth / 2) >= (other.entityCoordinates.x - other.entityWidth / 2))
				&& ((entityCoordinates.x - entityWidth / 2) < (other.entityCoordinates.x - other.entityWidth / 2)) ? true
				: false;
	}

	private boolean intersectionToLeft(Entity other) {
		return ((entityCoordinates.x - entityWidth / 2) <= (other.entityCoordinates.x + other.entityWidth / 2))
				&& ((entityCoordinates.x + entityWidth / 2) > (other.entityCoordinates.x + other.entityWidth / 2)) ? true
				: false;
	}
	
	public void moveEntity(){
		Point copy = new Point(entityCoordinates.x, entityCoordinates.y);
		// Avanza Plano x
		if(Math.abs(Math.cos(entityDirection))>=Math.abs(Math.sin(entityDirection)))
			if(entityDirection < 90 && entityDirection >= 0 || entityDirection > 270 && entityDirection <= 360) {
				// Derecha
				copy = movePoint("Positive", "X", copy);
				x++;
			} else {
				// Izquierda
				copy = movePoint("Negative", "X", copy);
				x--;
			}
		// Avanza Plano y
		if(Math.abs(Math.cos(entityDirection))<=Math.abs(Math.sin(entityDirection)))
			if(entityDirection < 180 && entityDirection >= 0) {
				// Alante
				copy = movePoint("Positive", "Y", copy);
				y--;
			} else if(entityDirection >= 180 && entityDirection < 360) {
				// Atras
				copy = movePoint("Negative", "Y", copy);
				y++;
			}
		entityCoordinates = new Point(copy.x, copy.y);
	}
	
	private Point movePoint(String relation, String plane, Point point) {
		switch(relation){
			case "Positive":
				if(plane.equalsIgnoreCase("X")){
					point.x += 1;
				} else if (plane.equalsIgnoreCase("Y")){
					point.y += 1;
				}
				break;
			case "Negative":
				if(plane.equalsIgnoreCase("X")){
					point.x -= 1;
				} else if (plane.equalsIgnoreCase("Y")){
					point.y -= 1;
				}
				break;
		}
		
		return new Point(point.x, point.y);
	}

	public void gainHealth(int points){
		if(points >= 0){
			if(mHealthPoints + points <= mMaxHealth)
				mHealthPoints += points;
			else if (mHealthPoints + points > mMaxHealth)
				mHealthPoints = mMaxHealth;
		}else
			throw new IllegalArgumentException("Encontrado valor de puntos invalido al modificar HealthPoints");
	}
	
	public void looseHealth(int points){
		if(points > 0)
			mHealthPoints -= points;
		else
			throw new IllegalArgumentException("Encontrado valor de puntos negativo al modificar HealthPoints");
	}
	
	public int getHealth(){
		return mHealthPoints;
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
		Point origin = new Point(0, 0);
		
		entityCompass = (int) Geometry.getRotationAngle(entityCoordinates, origin, other.getCoordinates());
		
		return entityCompass;
	}
	
	public Point getCoordinates(){
		return entityCoordinates;
	}
	
	public void setCoordinates(Point point){
		this.entityCoordinates = point;
	}
	
	public boolean isMoving(){
		return mSpeed > 0 ? true : false;
	}
	
	/**
	 * @return the mStatus
	 */
	public int getStatus() {
		return mStatus;
	}

	/**
	 * @param mStatus the mStatus to set
	 */
	public void setStatus(int mStatus) {
		this.mStatus = mStatus;
	}

	/**
	 * @return the entityDirection
	 */
	public int getEntityDirection() {
		return entityDirection;
	}

	/**
	 * @param entityDirection the entityDirection to set
	 */
	public void setEntityDirection(int entityDirection) {
		this.entityDirection = entityDirection;
	}
}
