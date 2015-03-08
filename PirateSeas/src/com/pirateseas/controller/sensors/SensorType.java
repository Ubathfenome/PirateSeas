package com.pirateseas.controller.sensors;

public enum SensorType {
	TYPE_ALL(-1),
	TYPE_ACCELEROMETER(1),
	TYPE_MAGNETIC_FIELD(2),
	//TYPE_ORIENTATION(3), // Deprecated since API 8
	TYPE_GYROSCOPE(4),
	TYPE_LIGHT(5),
	TYPE_PRESSURE(6),
	//TYPE_TEMPERATURE(7),  // Deprecated since API 8
	TYPE_PROXIMITY(8),
	TYPE_GRAVITY(9),
	TYPE_LINEAR_ACCELERATION(10),
	TYPE_ROTATION_VECTOR(11),
	TYPE_RELATIVE_HUMIDITY(12),	
	TYPE_AMBIENT_TEMPERATURE(13);
	
	private final int code;

	SensorType(int code){
		this.code = code;
	}
	
	public int getCode(){
		return code;
	}
}