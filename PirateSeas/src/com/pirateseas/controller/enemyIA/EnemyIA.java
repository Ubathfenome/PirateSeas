package com.pirateseas.controller.enemyIA;

import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.entity.Ship;

public class EnemyIA {
	
	private IAStatus mStatus;
	
	private Ship playerShip;
	private Ship enemyShip;
	
	private String playerIsAt = "";
	
	private static final double DEGREES_TO_AUTOTURN = 30.0;
	
	public EnemyIA(Ship pShip, Ship eShip){
		this.playerShip = pShip;
		this.enemyShip = eShip;
		
		setStatus(IAStatus.IDLE);
	}
	
	public Ship getNextMove(){
		Ship ship = null;
		
		if(imHealthier() && playerWithinReach() && playerIsAligned()){
			//SHOOT target
			if(!playerIsAt.equals(Constants.EMPTY_STRING) && !playerIsAt.equals(Constants.BACK)){
				switch(playerIsAt){
				case Constants.FRONT:
					setStatus(IAStatus.ATTACKF);
					break;
				case Constants.RIGHT:
					setStatus(IAStatus.ATTACKSR);
					break;
				case Constants.LEFT:
					setStatus(IAStatus.ATTACKSL);
					break;
				}
			}
		} else if (imHealthier() && playerWithinReach() && !playerIsAligned()){
			//TURN to align with target
			if(playerIsAt.equals(Constants.EMPTY_STRING) || playerIsAt.equals(Constants.BACK)){
				if(Math.random() <= 0.5)
					setStatus(IAStatus.TURNR);
				else
					setStatus(IAStatus.TURNL);
			}
		} else if (imHealthier() && !playerWithinReach() && playerIsAligned()){
			//MOVE closer to target
			if(!playerIsAt.equals(Constants.EMPTY_STRING) && !playerIsAt.equals(Constants.BACK)){
				setStatus(IAStatus.MOVE);
			}
		} else if (imHealthier() && !playerWithinReach() && !playerIsAligned()){
			//IDLE let him come
			setStatus(IAStatus.IDLE);
		} else if (!imHealthier() && playerWithinReach()){
			//RETREAT asap (MOVE further from player)
			setStatus(IAStatus.RETREAT);
		}
		
		// Check current status
		switch(mStatus){
		case IDLE:
			// TODO Hold still
			ship = new Ship();
			break;
		case MOVE:
			// TODO Move Enemy ship forwards
			ship = new Ship();
			break;
		case TURNR:
			// TODO Turn Enemy ship to the right
			ship = new Ship();
			break;
		case TURNL:
			// TODO Turn Enemy ship to the left
			ship = new Ship();
			break;
		case ATTACKF:
			ship = enemyShip;
			break;
		case ATTACKSR:
			ship = enemyShip;
			break;
		case ATTACKSL:
			ship = enemyShip;
			break;
		case RETREAT:
			// TODO Establish retreat behaviour for enemy ships
			ship = new Ship();
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
		int direction = playerShip.compareTo(enemyShip);
		int enemyDirection = enemyShip.getEntityDirection();
		
		switch(direction){
			case 0:
				if(enemyDirection == 0){
					playerIsAt = Constants.BACK;
					return true;
				} else if (enemyDirection == 90) {
					playerIsAt = Constants.LEFT;
					return true;
				} else if(enemyDirection == 180){
					playerIsAt = Constants.FRONT;
					return true;
				} else if(enemyDirection == 270){
					playerIsAt = Constants.RIGHT;
					return true;
				} 
				break;
			case 90:
				if(enemyDirection == 0){
					playerIsAt = Constants.RIGHT;
					return true;
				} else if(enemyDirection == 90){
					playerIsAt = Constants.BACK;
					return true;
				} else if(enemyDirection == 180){
					playerIsAt = Constants.LEFT;
					return true;
				} else if(enemyDirection == 270){
					playerIsAt = Constants.FRONT;
					return true;
				}
				break;
			case 180:
				if(enemyDirection == 0){
					playerIsAt = Constants.FRONT;
					return true;
				} else if(enemyDirection == 90){
					playerIsAt = Constants.RIGHT;
					return true;
				} else if(enemyDirection == 180){
					playerIsAt = Constants.BACK;
					return true;
				} else if(enemyDirection == 270){
					playerIsAt = Constants.LEFT;
					return true;
				}
				break;
			case 270:
				if(enemyDirection == 0){
					playerIsAt = Constants.LEFT;
					return true;
				} else if(enemyDirection == 90){
					playerIsAt = Constants.FRONT;
					return true;
				} else if(enemyDirection == 180){
					playerIsAt = Constants.RIGHT;
					return true;
				} else if(enemyDirection == 270){
					playerIsAt = Constants.BACK;
					return true;
				}
				break;
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