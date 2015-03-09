package com.pirateseas.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pirateseas.R;
import com.pirateseas.global.Constants;
import com.pirateseas.controller.sensors.SensorActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity {
	
	private boolean newGame = false;
	
	protected Context context;
	
	private Button btnNewGame;
	private Button btnLoadGame;
	private Button btnConfig;
	private Button btnHelp;
	private Button btnExit;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		context = this;
		
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
		
		btnConfig = (Button) findViewById(R.id.btn_config);
		btnConfig.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				// Intent configIntent = new Intent(context, ConfigActivity.class);
				// startActivity(configIntent);
			}
		});
		
		btnHelp = (Button) findViewById(R.id.btn_help);
		btnHelp.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				// Intent helpIntent = new Intent(context, HelpActivity.class);
				// startActivity(helpIntent);
			}
		});
		
		btnExit = (Button) findViewById(R.id.btn_exit);
		btnExit.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				finish();
			}
		});
	}
	
	private void launchGame(boolean display_tutorial){
		if(display_tutorial == false){
			Intent newGameIntent = new Intent(context, GameActivity.class);
			startActivity(newGameIntent);
		} else {
			Intent tutorialIntent = new Intent(context, TutorialActivity.class);
			startActivity(tutorialIntent);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.REQUEST_SENSOR_LIST:
				if (resultCode == RESULT_OK){
					
					// TODO
					int[] sensorTypes = data.getIntArrayExtra(Constants.SENSOR_LIST);
					
					launchGame(newGame);
				}
				break;
		}
	}
}
