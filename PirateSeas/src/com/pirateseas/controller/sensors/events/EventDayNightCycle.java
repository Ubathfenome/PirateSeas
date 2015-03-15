package com.pirateseas.controller.sensors.events;

import com.pirateseas.controller.sensors.SensorType;

public class EventDayNightCycle {
	
	private static final float HOUR_DEGREE_RATIO = 360 / 24;

	public static SensorType getSensorType() {
		return SensorType.TYPE_LIGHT;
	}
	
	public float getAngleOfDay(float hour){
		// 1 Dia = 24 horas
		// 1 Revolicion = 360 grados
		// Ratio = 360 grados / 24 horas; -> X grados por hora
		
		// Recibe hora -> Devuelve angulo
		return hour * HOUR_DEGREE_RATIO;
	}
	
}