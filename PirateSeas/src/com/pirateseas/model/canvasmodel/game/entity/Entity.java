package com.pirateseas.model.canvasmodel.game.entity;

import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.BasicModel;
import com.pirateseas.utils.approach2d.Geometry;

import android.content.Context;
import android.graphics.Point;

public class Entity extends BasicModel{
	
	private static final int MOVEMENT_DELTA = 12;
		
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
		
	public Entity(Context context, double x, double y, double canvasWidth, double canvasHeight, Point eCoords, int eDirection, int eWidth, int eHeight, int eLength){
		super(context, x, y, canvasWidth, canvasHeight, null);
		
		this.entityWidth = eWidth;
		this.entityHeight = eHeight;
		this.entityLength = eLength;
		
		this.entityCoordinates = eCoords;
		
		if(eDirection < 0)
			entityDirection = 360 - eDirection;
		else if(eDirection > 360)
			entityDirection = eDirection - 360;
		else
			entityDirection = eDirection;
		
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
	
	public void moveEntity(Point destiny){
		int xDiff = 0;
		int yDiff = 0;
		int nextX = 0;
		int nextY = 0;
		
		// Get current Point
		Point curr = new Point(entityCoordinates.x, entityCoordinates.y);
		
		// Set difference with destiny Point
		if(destiny.x > curr.x){			// Destiny to the right
			xDiff = destiny.x - curr.x;	// Get the positive needed amount to reach the destiny
		} else if(destiny.x < curr.x) {	// Destiny to the left
			xDiff = curr.x - destiny.x;	// Get the positive needed amount to reach the destiny
		}		
		if(destiny.y > curr.y){			// Destiny to the front
			yDiff = destiny.y - curr.y;	// Get the positive needed amount to reach the destiny
		} else if(destiny.y < curr.y) { // Destiny to the back
			yDiff = curr.y - destiny.y;	// Get the positive needed amount to reach the destiny
		}
		
		// Calculate next Point coordinates
		if(xDiff > 0){
			if(destiny.x > curr.x){			// Destiny to the right
				nextX = curr.x + 1;			// Next point moved 1 position to the side
				x += MOVEMENT_DELTA;		// Move Bitmap coordinates relative to next Point movement
			} else if(destiny.x < curr.x) {	// Destiny to the left
				nextX = curr.x - 1;			// Next point moved 1 position to the side
				x -= MOVEMENT_DELTA;		// Move Bitmap coordinates relative to next Point movement
			}
		} 
		if(yDiff > 0){
			if(destiny.y > curr.y){			// Destiny to the front
				nextY = curr.y + 1;			// Next point moved 1 position to the front
				y -= MOVEMENT_DELTA;		// Move Bitmap coordinates relative to next Point movement
			} else if(destiny.y < curr.y) {	// Destiny to the back
				nextY = curr.y - 1;			// Next point moved 1 position to the back
				y += MOVEMENT_DELTA;		// Move Bitmap coordinates relative to next Point movement
			}
		}
		
		// Set next Point coordinates
		Point next = new Point(xDiff > 0 ? nextX : curr.x, yDiff > 0 ? nextY : curr.y);
		entityCoordinates = new Point(next.x, next.y);
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
