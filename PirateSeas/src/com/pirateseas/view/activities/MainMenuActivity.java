package com.pirateseas.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.pirateseas.R;
import com.pirateseas.controller.audio.MusicManager;
import com.pirateseas.global.Constants;

import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainMenuActivity extends Activity {
	
	private static final String TAG = "MainMenuActivity";
	private boolean newGame = false;
	private boolean mOverwriteWarning = false;
	private int mMode;

	protected Context context;
	protected SharedPreferences mPreferences;

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
		
		mMode = Constants.MODE;

		// Get Screen
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			Point size = new Point();
			getWindowManager().getDefaultDisplay().getSize(size);
			screenResolutionWidth = size.x;
			screenResolutionHeight = size.y;
		} else {
			Display display = ((WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();
			screenResolutionWidth = display.getWidth();
			screenResolutionHeight = display.getHeight();
		}

		mPreferences = context.getSharedPreferences(Constants.TAG_PREF_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putInt(Constants.PREF_DEVICE_WIDTH_RES, screenResolutionWidth);
		editor.putInt(Constants.PREF_DEVICE_HEIGHT_RES, screenResolutionHeight);
		editor.putBoolean(Constants.PREF_CONTROL_MODE, Constants.PREF_GAME_TOUCH);
		editor.putBoolean(Constants.TAG_EXE_MODE, Constants.isInDebugMode(mMode));
		editor.commit();

		btnNewGame = (Button) findViewById(R.id.btn_newgame);
		btnNewGame.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(mOverwriteWarning){
					OverwriteGameDialogFragment overwriteDialog = new OverwriteGameDialogFragment();
					overwriteDialog.show(getFragmentManager(), "OverwriteGameDialog");
				} else {
					newGame = true;
					launchSensorActivity();
				}
			}
		});

		btnLoadGame = (Button) findViewById(R.id.btn_loadgame);
		btnLoadGame.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				newGame = false;
				launchSensorActivity();
			}
		});

		btnSettings = (ImageButton) findViewById(R.id.btn_settings);
		btnSettings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent settingsIntent = new Intent(context,
						SettingsActivity.class);
				startActivity(settingsIntent);
			}
		});

		btnHelp = (ImageButton) findViewById(R.id.btn_help);
		btnHelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent helpIntent = new Intent(context, HelpActivity.class);
				startActivity(helpIntent);
			}
		});

		btnExit = (Button) findViewById(R.id.btn_exit);
		btnExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		AsyncTask<Void, Integer, Boolean> loadSoundsTask = new LoadSounds();
		
		if (!Constants.isInDebugMode(mMode)){
			loadSoundsTask.execute();
		}
	}

	private void launchGame(boolean displayTutorial, int[] sensorTypes) {
		if (displayTutorial == false) {
			Intent newGameIntent = new Intent(context, GameActivity.class);
			newGameIntent.putExtra(Constants.TAG_SENSOR_LIST, sensorTypes);
			newGameIntent.putExtra(Constants.TAG_LOAD_GAME, !displayTutorial);
			startActivity(newGameIntent);
		} else {
			Intent tutorialIntent = new Intent(context, TutorialActivity.class);
			tutorialIntent.putExtra(Constants.TAG_SENSOR_LIST, sensorTypes);
			tutorialIntent.putExtra(Constants.TAG_LOAD_GAME, !displayTutorial);
			startActivity(tutorialIntent);
		}
	}
	
	private void launchSensorActivity(){
		Intent checkSensorListIntent = new Intent(context, SensorActivity.class);
		startActivityForResult(checkSensorListIntent, Constants.REQUEST_SENSOR_LIST);
	}

	@Override
	protected void onPause() {
		if (!Constants.isInDebugMode(mMode))
			MusicManager.getInstance().pauseBackgroundMusic();
		super.onPause();
	}

	@Override
	protected void onResume() {
		findViewById(R.id.rootLayoutMainMenu).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		
		if (mPreferences.getLong(Constants.PREF_PLAYER_TIMESTAMP, 0) == 0) {
			btnLoadGame.setEnabled(false);
			mOverwriteWarning = false;
		} else {
			btnLoadGame.setEnabled(true);
			mOverwriteWarning = true;
		}
		
		super.onResume();
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case Constants.REQUEST_SENSOR_LIST:
				if (resultCode == RESULT_OK) {
					int[] sensorTypes = data
							.getIntArrayExtra(Constants.TAG_SENSOR_LIST);
					
					boolean emptyList = true;
					for(int i = 0, length = sensorTypes.length; i< length; i++){
						if(sensorTypes[i] != 0)
							emptyList = false;
					}
					if(emptyList){
						Toast.makeText(context, "No sensors have been detected. No events will be triggered.", Toast.LENGTH_LONG).show();
						SharedPreferences.Editor editor = mPreferences.edit();
						editor.putBoolean(Constants.PREF_DEVICE_NOSENSORS, true);
						editor.commit();
					}
	
					launchGame(newGame, sensorTypes);
				}
				break;
		}
	}
	
	private class OverwriteGameDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Activity dummyActivity = getActivity();
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(dummyActivity);
			builder.setTitle(
					getResources().getString(R.string.overwrite_dialog_title))
					.setMessage(R.string.overwrite_dialog_message)
					.setPositiveButton(R.string.overwrite_dialog_positive,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									boolean noSensors = mPreferences.getBoolean(Constants.PREF_DEVICE_NOSENSORS, false);
									
									SharedPreferences.Editor editor = mPreferences.edit();
									editor.clear();
									editor.putBoolean(Constants.TAG_EXE_MODE, Constants.isInDebugMode(mMode));
									editor.commit();
									
									if(!noSensors)
										launchSensorActivity();
									else{
										int[] emptySensorList = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
										launchGame(!mOverwriteWarning, emptySensorList);
									}
								}
							})
					.setNegativeButton(R.string.exit_dialog_negative,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User cancels the dialog
								}
							});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}
	
	/**
	 * http://stackoverflow.com/questions/7428448/android-soundpool-heapsize-overflow
	 * @author Miguel
	 *
	 */
	private class LoadSounds extends AsyncTask<Void, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(Void... arg0) {
			MusicManager.getInstance(context).registerSound(MusicManager.SOUND_ENEMY_APPEAR, R.raw.snd_ship_ahoy);
			MusicManager.getInstance(context).registerSound(MusicManager.MUSIC_GAME_PAUSED, R.raw.msc_game_paused);
			MusicManager.getInstance(context).registerSound(MusicManager.MUSIC_BATTLE, R.raw.msc_soundtrack_battle);
			MusicManager.getInstance(context).registerSound(MusicManager.MUSIC_ISLAND, R.raw.msc_soundtrack_island);
			MusicManager.getInstance(context).registerSound(MusicManager.MUSIC_GAME_MENU, R.raw.msc_soundtrack_menu);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_GOLD_GAINED, R.raw.snd_gold_gained);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_GOLD_SPENT, R.raw.snd_gold_spent);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_SHOT_FIRED, R.raw.snd_shot_fired);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_SHOT_HIT, R.raw.snd_shot_hit);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_SHOT_MISSED, R.raw.snd_shot_missed);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_SHOT_RELOADING, R.raw.snd_shot_reload);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_SHOT_EXPLOSION, R.raw.snd_shot_explosion);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_WEATHER_FOG, R.raw.snd_weather_fog);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_WEATHER_STORM, R.raw.snd_weather_storm);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_WEATHER_MAELSTROM, R.raw.snd_weather_maelstorm);
			MusicManager.getInstance().registerSound(MusicManager.SOUND_XP_GAINED, R.raw.snd_xp_gained);
			return true;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			Log.d(TAG,"AudioPool loaded");
			if (!Constants.isInDebugMode(mMode))
				MusicManager.getInstance(context, MusicManager.MUSIC_GAME_MENU).playBackgroundMusic();
		}		
	}
}
