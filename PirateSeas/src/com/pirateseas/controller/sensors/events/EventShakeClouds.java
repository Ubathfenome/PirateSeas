package com.pirateseas.controller.sensors.events;

import com.pirateseas.controller.sensors.SensorType;

public class EventShakeClouds {
	
	public static final float threshold = 4;
	
	public static SensorType getSensorType() {
		return SensorType.TYPE_LINEAR_ACCELERATION;
	}
}
