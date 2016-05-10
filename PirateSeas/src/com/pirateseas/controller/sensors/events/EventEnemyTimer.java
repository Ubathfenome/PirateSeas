package com.pirateseas.controller.sensors.events;

import android.util.Log;

import com.pirateseas.controller.sensors.SensorType;
import com.pirateseas.model.canvasmodel.game.entity.Ship;

public class EventEnemyTimer {
	boolean spawn = false;
	private static final String TAG = "EventEnemyTimer";

	public EventEnemyTimer(float x, float y, float z){		
		// Establish relation between acceleration and game timer
		Log.d(TAG, "X = " + x + "; Y = " + y + "; Z = " + z);
		float possibilities = x + y + z;
		Log.d(TAG, "Actual possibilities of enemy spawn: " + possibilities + "%");
		spawn = Math.abs(possibilities) > 75 ? true : false;
	}
	
	public static SensorType getSensorType() {
		return SensorType.TYPE_MAGNETIC_FIELD;
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