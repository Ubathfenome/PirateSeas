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
		Ship ship = null;
		if(getStatus() == IAStatus.IDLE){
			setStatus(IAStatus.MOVE);
		}
		
		if(imHealthier() && playerWithinReach() && playerIsAligned()){
			//SHOOT target
			if(!playerIsAt.equals(Constants.EMPTY_STRING) && !playerIsAt.equals(Constants.BACK)){
				switch(playerIsAt){
				case Constants.RIGHT:
				case Constants.LEFT:
					setStatus(IAStatus.ATTACK);
					break;
				}
			}
		} else if (imHealthier() && playerWithinReach() && !playerIsAligned()){
			//TURN to align with target
			if(playerIsAt.equals(Constants.EMPTY_STRING) || playerIsAt.equals(Constants.BACK)){
				if(Math.random() <= 0.5)
					setStatus(IAStatus.MOVE);
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
			setStatus(IAStatus.MOVE);
		} else {
			setStatus(IAStatus.MOVE);
		}
		
		// Check current status
		ship = enemyShip;
		if (ship.getEntityDirection() == 270){
			ship.setEntityDirection(180);
		}
		switch(mStatus){
		case IDLE:
			// Hold still
			
			break;
		case MOVE:
			// Move Enemy ship forwards in the direction it is facing
			int direction = ship.getEntityDirection();
			int mod_dir = 0;
			final int ammount = 10; // X pixels moved
			
			if(direction > 90 && direction < 270)
				mod_dir = -1;
			else if (direction < 90 || direction > 270)
				mod_dir= 1;
			
			if(!ship.isMoving()){
				ship.increaseSpeedX();
			}
			ship.move(ship.getX() + mod_dir*ammount, ship.getY());
			
			if(ship.getX() == 0 && mod_dir == -1){
				// Turn right
				ship.setEntityDirection(0);
			} else if ((ship.getX() + ship.getWidth()) >= (screenWidth - ship.getWidth()) && mod_dir == 1){
				// Turn left
				ship.setEntityDirection(180);
			}
			
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