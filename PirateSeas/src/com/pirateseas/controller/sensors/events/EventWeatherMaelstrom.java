package com.pirateseas.controller.sensors.events;

import android.util.Log;

import com.pirateseas.controller.sensors.SensorType;

@SuppressWarnings("unused")
public class EventWeatherMaelstrom {
	private static final String TAG = "EventWeatherMaelstrom";
	private static final int AVERAGE = 10;
	private static final int THRESHOLD = 4;

	public static SensorType getSensorType() {
		return SensorType.TYPE_ACCELEROMETER;
	}
	
	// Establish event effects on sensor trigger
	public static boolean generateMaelstrom(float ySpeed, float zSpeed){
		double gHypot = Math.sqrt(ySpeed*ySpeed + zSpeed*zSpeed);
		
		// Log.d(TAG, "Gravity (m/s^2): " + gHypot);
		
		if(Math.abs(gHypot - AVERAGE) >= THRESHOLD)
			return true;
		
		return false;		
	}
}