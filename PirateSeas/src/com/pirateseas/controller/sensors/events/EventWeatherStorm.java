package com.pirateseas.controller.sensors.events;

import com.pirateseas.controller.sensors.SensorType;

public class EventWeatherStorm {

	public static SensorType getSensorType() {
		return SensorType.TYPE_RELATIVE_HUMIDITY;
	}
	
	public static boolean setCloudyDay(float humidityPercent){
		return humidityPercent > 0.75;
	}
	
}