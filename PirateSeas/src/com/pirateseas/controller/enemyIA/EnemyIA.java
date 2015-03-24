package com.pirateseas.controller.enemyIA;

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
		
		
		return null;
	}
	
	private boolean imHealthier(){
		return enemyShip.getHealth() >= playerShip.getHealth() ? true : false;
	}
	
	private boolean playerWithinReach(){
		return false;
		
	}
	
	private boolean playerIsAligned(){
		return false;		
	}
	
	
	private enum IAStatuses{
		IDLE, MOVE, TURN, ATTACKF, ATTACKSR, ATTACKSL, RETREAT
	}
}