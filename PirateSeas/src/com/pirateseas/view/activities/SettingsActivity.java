package com.pirateseas.view.activities;

import com.pirateseas.R;
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
	private SeekBar skbVolume;
	private Button btnSettingsAccept;
	private Button btnSettingsCancel;
	
	private int volumeValue;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		final SharedPreferences mPreferences = getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE);
		
		txtTitleLabel = (TextView) findViewById (R.id.txtSettingsLabel);
		Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/" + Constants.FONT_NAME + ".ttf");
		txtTitleLabel.setTypeface(customFont);
		
		skbVolume = (SeekBar) findViewById(R.id.sbVolume);
		skbVolume.setProgress(mPreferences.getInt(Constants.PREF_DEVICE_VOLUME, 0));
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
			}
		});
		
		btnSettingsAccept = (Button) findViewById(R.id.btnSettingsAccept);
		btnSettingsAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Save changes in preferences
				SharedPreferences.Editor editor = mPreferences.edit();
				editor.putInt(Constants.PREF_DEVICE_VOLUME, volumeValue);
				
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
