package com.pirateseas.controller.sensors.events;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.WindowManager;

import com.pirateseas.controller.sensors.SensorType;

public class EventWeatherLight {

	public static SensorType getSensorType() {
		return SensorType.TYPE_LIGHT;
	}
	
	public static void adjustScreenBrightness(Context context, float brightnessLevel){
		if (brightnessLevel < 8)
			brightnessLevel = 8;
		else if (brightnessLevel > 100)
			brightnessLevel = 100;
		
		Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS, (int) brightnessLevel);
		
		WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
		lp.screenBrightness = brightnessLevel/100.0f; 
		((Activity) context).getWindow().setAttributes(lp);
	}
	
}