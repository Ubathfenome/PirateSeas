package com.pirateseas.controller.enemyIA;

import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.entity.Ship;

public class EnemyIA {
	
	private int mStatus;
	
	private Ship playerShip;
	private Ship enemyShip;
	
	private String playerIsAt = "";
	
	public EnemyIA(Ship pShip, Ship eShip){
		this.playerShip = pShip;
		this.enemyShip = eShip;
		
		setStatus(IAStatuses.IDLE.ordinal());
	}
	
	public Ship getNextMove(){
		Ship ship = null;
		if(imHealthier() && playerWithinReach() && playerIsAligned()){
			//SHOOT target
			if(!playerIsAt.equals(Constants.EMPTY_STRING) && !playerIsAt.equals(Constants.BACK)){
				switch(playerIsAt){
				case Constants.FRONT:
					setStatus(IAStatuses.ATTACKF.ordinal());
					ship = enemyShip;
					break;
				case Constants.RIGHT:
					setStatus(IAStatuses.ATTACKSR.ordinal());
					ship = enemyShip;
					break;
				case Constants.LEFT:
					setStatus(IAStatuses.ATTACKSL.ordinal());
					ship = enemyShip;
					break;
				}
			}
		} else if (imHealthier() && playerWithinReach() && !playerIsAligned()){
			//TURN to align with target
			if(playerIsAt.equals(Constants.EMPTY_STRING) || playerIsAt.equals(Constants.BACK)){
				setStatus(IAStatuses.TURN.ordinal());
				// TODO
				ship = new Ship();
			}
		} else if (imHealthier() && !playerWithinReach() && playerIsAligned()){
			//MOVE closer to target
			if(!playerIsAt.equals(Constants.EMPTY_STRING) && !playerIsAt.equals(Constants.BACK)){
				setStatus(IAStatuses.MOVE.ordinal());
				// TODO
				ship = new Ship();
			}
		} else if (imHealthier() && !playerWithinReach() && !playerIsAligned()){
			//IDLE let him come
			setStatus(IAStatuses.IDLE.ordinal());
			// TODO
			ship = new Ship();
		} else if (!imHealthier() && playerWithinReach()){
			//RETREAT asap (MOVE further from player)
			setStatus(IAStatuses.RETREAT.ordinal());
			// TODO
			ship = new Ship();
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
	
	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int mStatus) {
		this.mStatus = mStatus;
	}

	private enum IAStatuses{
		IDLE, MOVE, TURN, ATTACKF, ATTACKSR, ATTACKSL, RETREAT
	}

	@Override
	public String toString() {
		return "EnemyIA [mStatus=" + mStatus + "]";
	}
}