package com.pirateseas.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import com.pirateseas.R;
import com.pirateseas.controller.audio.MusicManager;
import com.pirateseas.global.Constants;

import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class MainMenuActivity extends Activity {

	private static final int DEBUG_MODE = 0x0;
	private static final int RELEASE_MODE = 0x1;
	
	private boolean newGame = false;
	private int mMode;
	
	protected Context context;
	
	protected static int screenResolutionWidth;
	protected static int screenResolutionHeight;
	
	private Button btnNewGame;
	private Button btnLoadGame;
	private ImageButton btnSettings;
	private ImageButton btnHelp;
	private Button btnExit;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		context = this;
		
		// Get Screen
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			Point size = new Point();
			getWindowManager().getDefaultDisplay().getSize(size);
			screenResolutionWidth = size.x;
			screenResolutionHeight = size.y;
		} else {
			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			screenResolutionWidth = display.getWidth();
			screenResolutionHeight = display.getHeight();
		}
		
		SharedPreferences mPreferences = context.getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt(Constants.PREF_DEVICE_WIDTH_RES, screenResolutionWidth);
		editor.putInt(Constants.PREF_DEVICE_HEIGHT_RES, screenResolutionHeight);
		editor.commit();
		
		btnNewGame = (Button) findViewById(R.id.btn_newgame);
		btnNewGame.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				newGame = true;
				Intent checkSensorListIntent = new Intent(context, SensorActivity.class);
				startActivityForResult(checkSensorListIntent, Constants.REQUEST_SENSOR_LIST);
			}
		});
		
		btnLoadGame = (Button) findViewById(R.id.btn_loadgame);
		btnLoadGame.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				newGame = false;
				Intent checkSensorListIntent = new Intent(context, SensorActivity.class);
				startActivityForResult(checkSensorListIntent, Constants.REQUEST_SENSOR_LIST);
			}
		});
		
		if(mPreferences.getInt(Constants.PREF_PLAYER_DAYS, 0) == 0){
			btnLoadGame.setEnabled(false);
		}
		
		btnSettings = (ImageButton) findViewById(R.id.btn_settings);
		btnSettings.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent settingsIntent = new Intent(context, SettingsActivity.class);
				startActivity(settingsIntent);
			}
		});
		
		btnHelp = (ImageButton) findViewById(R.id.btn_help);
		btnHelp.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				/*
				Intent helpIntent = new Intent(context, HelpActivity.class);
				startActivity(helpIntent);
				*/
				
				Intent shopIntent = new Intent(context, ShopActivity.class);
				shopIntent.putExtra(Constants.ITEMLIST_NATURE, Constants.NATURE_SHOP);
				startActivity(shopIntent);
			}
		});
		
		btnExit = (Button) findViewById(R.id.btn_exit);
		btnExit.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				finish();
			}
		});
		
		mMode = DEBUG_MODE;
		
		MusicManager.getInstance(this, R.raw.msc_casimps1_zoo_music);
		if(!isInDebugMode())
			MusicManager.getInstance().playBackgroundMusic();
	}
	
	private boolean isInDebugMode() {
		return mMode == DEBUG_MODE ? true : false;
	}

	private void launchGame(boolean display_tutorial, int[] sensorTypes){
		if(display_tutorial == false){
			Intent newGameIntent = new Intent(context, GameActivity.class);
			newGameIntent.putExtra(Constants.TAG_SENSOR_LIST, sensorTypes);
			startActivity(newGameIntent);
		} else {
			Intent tutorialIntent = new Intent(context, TutorialActivity.class);
			tutorialIntent.putExtra(Constants.TAG_SENSOR_LIST, sensorTypes);
			startActivity(tutorialIntent);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.REQUEST_SENSOR_LIST:
				if (resultCode == RESULT_OK){					
					int[] sensorTypes = data.getIntArrayExtra(Constants.TAG_SENSOR_LIST);
					
					launchGame(newGame, sensorTypes);
				}
				break;
		}
	}
}
