package com.pirateseas.controller.sensors.events;

import com.pirateseas.controller.sensors.SensorType;

public class EventDayNightCycle {
	private static final float SEA_LEVEL_PRESSURE = 1013.25f; // 1 atm = 1013.25 hPA = 1013.25 mbar
	private static final int HOUR_VALUE_RATIO = 255 / 24; // Ratio = 255 values / 24 hours; -> X values per hour

	public static SensorType getSensorType() {
		return SensorType.TYPE_PRESSURE;
	}
	
	public static int getSkyFilter(float hour){		
		return (int) (hour * HOUR_VALUE_RATIO);
	}
}