package com.pirateseas.controller.sensors.events;

import android.graphics.Point;
import android.util.Log;

import com.pirateseas.controller.sensors.SensorType;

public class EventWeatherMaelstrom {
	private static final String TAG = "EventWeatherMaelstrom";

	public static SensorType getSensorType() {
		return SensorType.TYPE_GYROSCOPE;
	}
	
	// Establish event effects on sensor trigger
	public Point generateMaelstrom(float xSpeed, float ySpeed, float zSpeed){
		float omegaMovement = (float) Math.sqrt(xSpeed*xSpeed + ySpeed*ySpeed + zSpeed*zSpeed);
		Log.d(TAG,"Angular speed (rad/s)" + omegaMovement);
		return null;		
	}
}