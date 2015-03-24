package com.pirateseas.view.activities;

import com.pirateseas.global.Constants;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;

public class BrightnessUpdateActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		// [8..100]
		int brightnessLevel = getIntent().getIntExtra(Constants.TAG_BRIGHTNESS_LEVEL, 100);
		
		Settings.System.putInt(this.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		Settings.System.putInt(this.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS, brightnessLevel);
		
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = brightnessLevel/100.0f; 
		getWindow().setAttributes(lp);
		
		finish();
	}
	
}