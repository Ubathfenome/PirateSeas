package com.pirateseas.controller.sensors.events;

import com.pirateseas.controller.sensors.SensorType;
import com.pirateseas.model.canvasmodel.game.entity.Ship;

public class EventEnemyTimer {
	float accX, accY, accZ;
	boolean spawn = false;

	public EventEnemyTimer(float x, float y, float z){
		this.accX = x;
		this.accY = y;
		this.accZ = z;
		double possibilities = Math.random() * 10000;
		spawn = possibilities % 100 > 75 ? true : false;
	}
	
	public static SensorType getSensorType() {
		return SensorType.TYPE_LINEAR_ACCELERATION;
	}

	public boolean challengerApproaches(Ship nEnemyShip) {
		if(nEnemyShip != null)
			return false;
		else if(spawn)
			return true;
		else
			return false;
	}	
	
	
}