package com.pirateseas.controller.enemyIA;

import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.entity.Ship;

public class EnemyIA {
	
	private int mStatus;
	
	private static Ship playerShip;
	private Ship enemyShip;
	
	public EnemyIA(Ship pShip, Ship eShip){
		this.playerShip = pShip;
		this.enemyShip = eShip;
		
		mStatus = IAStatuses.IDLE.ordinal();
	}
	
	public Ship getNextMove(){
		// TODO
		
		return null;
	}
	
	private boolean imHealthier(){
		return enemyShip.getHealth() >= playerShip.getHealth() ? true : false;
	}
	
	private boolean playerWithinReach(){
		int enemyRange = Constants.SHIP_BASIC_RANGE * enemyShip.getRange();
		
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
				if(enemyDirection == 270 || enemyDirection == 180 || enemyDirection == 90)
					return true;
				break;
			case 90:
				if(enemyDirection == 270 || enemyDirection == 180 || enemyDirection == 0)
					return true;
				break;
			case 180:
				if(enemyDirection == 270 || enemyDirection == 90 || enemyDirection == 0)
					return true;
				break;
			case 270:
				if(enemyDirection == 180 || enemyDirection == 90 || enemyDirection == 0)
					return true;
				break;
		}
		
		return false;
	}
	
	
	private enum IAStatuses{
		IDLE, MOVE, TURN, ATTACKF, ATTACKSR, ATTACKSL, RETREAT
	}
}