package com.pirateseas.controller.sensors.events;

import android.content.Context;
import android.content.Intent;

import com.pirateseas.controller.sensors.SensorType;
import com.pirateseas.view.activities.BrightnessUpdateActivity;

public class EventDayNightCycle {
	
	private static final float HOUR_DEGREE_RATIO = 360 / 24;

	public static SensorType getSensorType() {
		return SensorType.TYPE_LIGHT;
	}
	
	public static float getAngleOfDay(float hour){
		// 1 Dia = 24 horas
		// 1 Revolicion = 360 grados
		// Ratio = 360 grados / 24 horas; -> X grados por hora
		
		// Recibe hora -> Devuelve angulo
		return hour * HOUR_DEGREE_RATIO;
	}
	
	public static void adjustScreenBrightness(Context context, float brightnessLevel){
		if (brightnessLevel < 8)
			brightnessLevel = 8;
		else if (brightnessLevel > 100)
			brightnessLevel = 100;
		
		Intent screenBrightnessIntent = new Intent(context, BrightnessUpdateActivity.class);
		context.startActivity(screenBrightnessIntent);
	}
	
}