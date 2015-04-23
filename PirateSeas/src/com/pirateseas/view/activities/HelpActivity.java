package com.pirateseas.view.activities;

import com.pirateseas.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HelpActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		// TODO All
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		findViewById(R.id.rootLayoutHelp).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		super.onResume();
	}
	
	
}
