package com.pirateseas.view.activities;

import com.pirateseas.R;
import com.pirateseas.controller.audio.MusicManager;
import com.pirateseas.global.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	private TextView txtTitleLabel;
	private TextView txtVolumeLabel;
	private SeekBar skbVolume;
	private Button btnSettingsAccept;
	private Button btnSettingsCancel;
	
	private float volumeValue = 0f;
	private String labelValue;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		final SharedPreferences mPreferences = getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE);
		
		txtTitleLabel = (TextView) findViewById (R.id.txtSettingsLabel);
		Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/" + Constants.FONT_NAME + ".ttf");
		txtTitleLabel.setTypeface(customFont);
		
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putFloat(Constants.PREF_DEVICE_VOLUME, MusicManager.getInstance().getDeviceVolume());
		editor.commit();
		
		txtVolumeLabel = (TextView) findViewById (R.id.txtVolumeLabel);
		labelValue = (String) txtVolumeLabel.getText();
		
		skbVolume = (SeekBar) findViewById(R.id.sbVolume);
		skbVolume.setProgress((int) mPreferences.getFloat(Constants.PREF_DEVICE_VOLUME, 0));
		skbVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser)
					volumeValue = progress;
				MusicManager.getInstance().setDeviceVolume(progress);
				txtVolumeLabel.setText(labelValue + " " + progress);
			}
		});
		
		btnSettingsAccept = (Button) findViewById(R.id.btnSettingsAccept);
		btnSettingsAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Save changes in preferences
				SharedPreferences.Editor editor = mPreferences.edit();
				editor.putFloat(Constants.PREF_DEVICE_VOLUME, volumeValue);
				
				editor.commit();
				
				finish();
			}
		});
		
		btnSettingsCancel = (Button) findViewById(R.id.btnSettingsCancel);
		btnSettingsCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
