package com.pirateseas.controller.enemyIA;

import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.entity.Ship;

public class EnemyIA {
	
	private IAStatus mStatus;
	
	private Ship playerShip;
	private Ship enemyShip;
	private double screenWidth;
	
	private String playerIsAt = Constants.EMPTY_STRING;
	
	public EnemyIA(Ship pShip, Ship eShip, double nScreenWidth){
		this.playerShip = pShip;
		this.enemyShip = eShip;
		this.screenWidth = nScreenWidth;
		
		setStatus(IAStatus.IDLE);
	}
	
	public Ship getNextMove(){
		Ship ship = enemyShip;
		boolean wasIdle = ship.wasIdle();
		boolean healthier = imHealthier();
		boolean inReach = playerWithinReach();
		boolean isAligned = playerIsAligned();
		
		if(healthier && inReach && isAligned){
			//SHOOT target
			if(!playerIsAt.equals(Constants.EMPTY_STRING) && !playerIsAt.equals(Constants.BACK)){
				switch(playerIsAt){
				case Constants.RIGHT:
				case Constants.LEFT:
					setStatus(IAStatus.ATTACK);
					break;
				}
			}
		} else if (healthier && inReach && !isAligned){
			//TURN to align with target
			if(playerIsAt.equals(Constants.EMPTY_STRING) || playerIsAt.equals(Constants.BACK)){
				if(Math.random() <= 0.5)
					setStatus(IAStatus.MOVE);
			}
		} else if (healthier && !inReach && isAligned){
			//MOVE closer to target
			if(!playerIsAt.equals(Constants.EMPTY_STRING) && !playerIsAt.equals(Constants.BACK)){
				setStatus(IAStatus.MOVE);
			}
		} else if (healthier && !inReach && !isAligned){
			//IDLE let him come
			setStatus(IAStatus.IDLE);
		} else if (!healthier && inReach){
			//RETREAT asap (MOVE further from player)
			setStatus(IAStatus.MOVE);
		} else if (getStatus() == IAStatus.IDLE && wasIdle) {
			setStatus(IAStatus.MOVE);
		} else if (getStatus() == IAStatus.IDLE && !wasIdle) {
			setStatus(IAStatus.IDLE);
			
		}
		
		// Check current status
		if (ship.getEntityDirection() == 270){
			ship.setEntityDirection(180);
		}
		if(ship.getEntityDirection() == 90) {
			ship.setEntityDirection(0);
		}
		
		switch(mStatus){
		case IDLE:
			// Hold still
			if(ship.isMoving()){
				ship.resetSpeedLevel();
				ship.setIdle(true);
			} else {
				ship.setIdle(false);
			}
			break;
		case MOVE:
			// Move Enemy ship forwards in the direction it is facing
			int direction = ship.getEntityDirection();
			int mod_dir = 0;
			int ammount = 0; // X pixels moved
			
			if(direction > 90 && direction < 270)
				mod_dir = -1;
			else if (direction < 90 || direction > 270)
				mod_dir= 1;
			else 
				mod_dir = 0;
			
			if(!ship.isMoving()){
				ship.setShipTypeDefaultSpeed();
			}
			ammount=ship.getShipType().getSpeed();
			int leftLimit = (int) (ship.getX()-ammount);
			int rightLimit = (int) (ship.getX() + ship.getWidth() + ammount); 
			
			if(leftLimit <= 0 && mod_dir == -1){
				// Turn right
				ship.setEntityDirection(0);
			} else if (rightLimit >= screenWidth && mod_dir == 1){
				// Turn left
				ship.setEntityDirection(180);
			}
			ship.move(mod_dir*ammount, 0, false);
			
			break;
		case ATTACK:
			break;
		}
		
		return ship;
	}

	private boolean imHealthier(){
		return enemyShip.getHealth() >= playerShip.getHealth() ? true : false;
	}
	
	private boolean playerWithinReach(){
		float enemyRange = Constants.SHIP_BASIC_RANGE * enemyShip.getRange();
		
		if (Math.abs(enemyShip.getCoordinates().y - playerShip.getCoordinates().y) <= enemyRange)
			return true;
		if (Math.abs(enemyShip.getCoordinates().x - playerShip.getCoordinates().x) <= enemyRange)
			return true;
		return false;
	}
	
	private boolean playerIsAligned(){
		int enemyDirection = enemyShip.getEntityDirection();

		if(enemyDirection == 0){
			playerIsAt = Constants.RIGHT;
			return true;
		} else if (enemyDirection == 90) {
			playerIsAt = Constants.BACK;
			return true;
		} else if(enemyDirection == 180){
			playerIsAt = Constants.LEFT;
			return true;
		} else if(enemyDirection == 270){
			playerIsAt = Constants.FRONT;
			return true;
		} 
					
		return false;
	}	
	
	public IAStatus getStatus() {
		return mStatus;
	}

	public void setStatus(IAStatus mStatus) {
		this.mStatus = mStatus;
	}

	@Override
	public String toString() {
		return "EnemyIA [mStatus=" + mStatus + "]";
	}
}