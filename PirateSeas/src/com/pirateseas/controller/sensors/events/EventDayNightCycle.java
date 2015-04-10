package com.pirateseas.controller.sensors.events;

import android.hardware.SensorManager;

import com.pirateseas.controller.sensors.SensorType;

public class EventDayNightCycle {
	private static final float PSA = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;
	private static final float HOUR_VALUE_RATIO = 255 / (24 * PSA); // Ratio = 255 values / 24 hours; -> X values per hour
	public static float pressure = PSA;

	public static SensorType getSensorType() {
		return SensorType.TYPE_PRESSURE;
	}
	
	public static int getSkyFilter(float hour){
		return (int) (hour * pressure * HOUR_VALUE_RATIO);
	}
}