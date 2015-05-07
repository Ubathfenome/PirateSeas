package com.pirateseas.controller.sensors.events;

import android.hardware.SensorManager;
import android.util.Log;

import com.pirateseas.controller.sensors.SensorType;
import com.pirateseas.global.Constants;

@SuppressWarnings("unused")
public class EventDayNightCycle {
	private static final String TAG = "EventDayNightCycle";
	private static final int HOURS_PER_DAY = 60;
	private static final int MAX_SHADOW_VALUE = 255;
	private static final float PSA = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;
	private static final float HOUR_VALUE_RATIO = (MAX_SHADOW_VALUE * 2) / (HOURS_PER_DAY * PSA); // Ratio = 510 values / 60 hours per day; -> X values per hour
	public static float pressure = PSA;

	public static SensorType getSensorType() {
		return SensorType.TYPE_PRESSURE;
	}
	
	public static int getSkyFilter(float hour){
		// Variar de dia a noche y de noche a dia
		int filterValue = 0;
		if(hour <= (HOURS_PER_DAY / 2))
			filterValue = (int) (hour * pressure * HOUR_VALUE_RATIO);
		else
			filterValue = (int) ((HOURS_PER_DAY - hour) * pressure * HOUR_VALUE_RATIO);
		// Log.d(TAG, "Sky mask is " + filterValue + " while hour is " + hour);
		return filterValue;
	}
}