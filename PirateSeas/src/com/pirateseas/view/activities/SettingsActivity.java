package com.pirateseas.view.activities;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	private TextView txtTitleLabel;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		txtTitleLabel = (TextView) findViewById (R.id.txtSettingsLabel);
		Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/" + Constants.FONT_NAME + ".ttf");
		txtTitleLabel.setTypeface(customFont);
	}

}
