package com.pirateseas.view.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.controller.sensors.events.EventDayNightCycle;
import com.pirateseas.controller.sensors.events.EventEnemyTimer;
import com.pirateseas.controller.sensors.events.EventWeatherFog;
import com.pirateseas.controller.sensors.events.EventWeatherMaelstrom;
import com.pirateseas.controller.sensors.events.EventWeatherStorm;
import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.scene.Island;
import com.pirateseas.model.canvasmodel.ui.Throttle;
import com.pirateseas.model.canvasmodel.ui.UIDisplayElement;
import com.pirateseas.model.canvasmodel.ui.Wheel;
import com.pirateseas.view.graphics.canvasview.CanvasView;

/**
 * 
 * @author p7166421
 *
 * @see: http://android-developers.blogspot.com.es/2011/11/making-android-games-that-play-nice.html
 */
public class GameActivity extends Activity implements SensorEventListener {

	private static final String TAG = "GameActivity";

	private Context context;

	private CanvasView mCanvasView;

	private static final int SENSOR_UPDATE_SECONDS = 2;

	protected int[] sensorTypes = null;
	protected long sensorLastTimestamp;
	
	boolean loadGame = false;
	
	protected SensorManager mSensorManager;
	protected List<Sensor> triggeringSensors;

	public ImageButton btnPause;
	public UIDisplayElement mGold, mAmmo;
	public Throttle ctrlThrottle;
	public Wheel ctrlWheel;
	
	public EventEnemyTimer eventEnemy;

	protected Point centerPoint;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		mCanvasView = new CanvasView(this);
		
		Intent data = getIntent();
		
		loadGame = data.getBooleanExtra(Constants.TAG_LOAD_GAME, true);

		// Receive the device event triggering sensor list
		triggeringSensors = new ArrayList<Sensor>();
		sensorTypes = data.getIntArrayExtra(Constants.TAG_SENSOR_LIST);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		for (int i = 0; i < sensorTypes.length; i++) {
			if (sensorTypes[i] != 0)
				triggeringSensors.add(mSensorManager
						.getDefaultSensor(sensorTypes[i]));
		}

		// Launch the game!!
		setContentView(R.layout.activity_game);

		btnPause = (ImageButton) findViewById(R.id.btnPause);
		btnPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent pauseIntent = new Intent(context, PauseActivity.class);
				context.startActivity(pauseIntent);
			}
		});

		ctrlThrottle = (Throttle) findViewById(R.id.controlThrottle);
		ctrlThrottle.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return ((Throttle) v).onTouchEvent(event);
			}
		});

		ctrlWheel = (Wheel) findViewById(R.id.controlWheel);
		ctrlWheel.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return ((Wheel) v).onTouchEvent(event);
			}
		});

		final LinearLayout gUI = (LinearLayout) findViewById(R.id.guiLayer);
		ViewTreeObserver vto = ctrlWheel.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					gUI.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				} else {
					gUI.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}

				ctrlWheel.setCenterPoint(new Point(ctrlWheel.getMeasuredWidth() / 2, ctrlWheel.getMeasuredHeight() / 2));
				centerPoint = ctrlWheel.getCenterPoint();
			}
		});

		mGold = (UIDisplayElement) findViewById(R.id.playerGold);
		mGold.setElementValue(0);
		mAmmo = (UIDisplayElement) findViewById(R.id.playerAmmunition);
		mAmmo.setElementValue(0);
	}
	
	public boolean hasToLoadGame(){
		return loadGame;
	}

	@Override
	protected void onPause() {	
		mSensorManager.unregisterListener(this);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		if(mCanvasView != null)
			mCanvasView.setStatus(Constants.GAME_STATE_PAUSE);

		super.onPause();
	}

	@Override
	protected void onResume() {		
		findViewById(R.id.rootLayoutGame).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		mCanvasView.setStatus(Constants.GAME_STATE_NORMAL);

		if (!CanvasView.nUpdateThread.isAlive()
				&& CanvasView.nUpdateThread.getState() != Thread.State.NEW) {
			Log.e(TAG, "MainLogic is DEAD. Re-starting...");
			mCanvasView.launchMainLogic();
			CanvasView.nUpdateThread.start();
		}

		for (int i = 0, size = triggeringSensors.size(); i < size; i++) {
			Sensor s = triggeringSensors.get(i);
			mSensorManager.registerListener(this, s,
					SensorManager.SENSOR_DELAY_GAME);
		}

		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// Pop up messageBox asking if the user is sure to leave
		LeaveGameDialogFragment exitDialog = new LeaveGameDialogFragment();
		exitDialog.show(getFragmentManager(), "LeaveGameDialog");
	}

	private class LeaveGameDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Activity dummyActivity = getActivity();
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(dummyActivity);
			builder.setTitle(
					getResources().getString(R.string.exit_dialog_title))
					.setMessage(R.string.exit_dialog_message)
					.setPositiveButton(R.string.exit_dialog_positive,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// Exit
									mCanvasView.setStatus(Constants.GAME_STATE_END);
									CanvasView.nUpdateThread.setRunning(false);
									dummyActivity.finish();
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

	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		long deltaTime = event.timestamp - sensorLastTimestamp;
		double deltaSeconds = deltaTime * Constants.NANOS_TO_SECONDS;
		if (arrayContainsValue(sensorTypes, sensor.getType())) {
			switch (sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				if (deltaSeconds >= SENSOR_UPDATE_SECONDS) { // Hold sensor updates
					// Parameters
					float axisSpeedX = event.values[0];
					float axisSpeedY = event.values[1];
					float axisSpeedZ = event.values[2];

					// Log.d(TAG, "Gravity (m/s^2): " + axisSpeedX + " / " + axisSpeedY + " / " + axisSpeedZ);
					
					// Event
					if(EventWeatherMaelstrom.generateMaelstrom(axisSpeedX, axisSpeedY, axisSpeedZ)){
						ctrlWheel.setDegrees(720);
						doWheelRotation();
						ctrlWheel.postInvalidate();
					}
					
					sensorLastTimestamp = event.timestamp;
				}
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				if (deltaSeconds >= SENSOR_UPDATE_SECONDS) {
					// Parameters
					float microTeslaX = event.values[0];
					float microTeslaY = event.values[1];
					float microTeslaZ = event.values[2];
					
					Log.d(TAG, "Magnetic field (uT): " + microTeslaX + " / " + microTeslaY + " / " + microTeslaZ);
					
					// Event
					
					
					sensorLastTimestamp = event.timestamp;
				}
				break;
			case Sensor.TYPE_GYROSCOPE:
				if (deltaSeconds >= SENSOR_UPDATE_SECONDS) { // Hold sensor updates
					// Parameters
					float axisSpeedX = event.values[0];
					float axisSpeedY = event.values[1];
					float axisSpeedZ = event.values[2];

					Log.d(TAG, "Gyroscope (rad/s): x = " + axisSpeedX + "; y = " + axisSpeedY + "; z = " + axisSpeedZ);
										
					// Event
					
					
					sensorLastTimestamp = event.timestamp;
				}
				break;
			case Sensor.TYPE_LIGHT:
				if (deltaSeconds >= SENSOR_UPDATE_SECONDS) { // Hold sensor updates
					// Parameters
					float lux = event.values[0];
					
					// Log.d(TAG, "Light (l): " + lux);

					// Event
					EventWeatherFog.adjustScreenBrightness(context, lux % 100);
					
					sensorLastTimestamp = event.timestamp;
				}
				break;
			case Sensor.TYPE_PRESSURE:
				if (deltaSeconds >= SENSOR_UPDATE_SECONDS) { // Hold sensor updates
					// Parameters
					float millibar = event.values[0];

					// Event
					EventDayNightCycle.pressure = millibar;
					
					sensorLastTimestamp = event.timestamp;
				}
				break;
			case Sensor.TYPE_PROXIMITY:
				if (deltaSeconds >= SENSOR_UPDATE_SECONDS) {
					// Parameters
					float centimeters = event.values[0];
					
					Log.d(TAG, "Proximity (cm): " + centimeters + " // " + sensor.getMaximumRange());
					
					// Event
					
					
					sensorLastTimestamp = event.timestamp;
				}
				break;
			case Sensor.TYPE_GRAVITY:
				break;
			case Sensor.TYPE_LINEAR_ACCELERATION:
				if (deltaSeconds >= SENSOR_UPDATE_SECONDS) { // Hold sensor updates
					// Parameters
					float linearAccelerationX = event.values[0];
					float linearAccelerationY = event.values[1];
					float linearAccelerationZ = event.values[2];

					// Event
					eventEnemy = new EventEnemyTimer(linearAccelerationX, linearAccelerationY, linearAccelerationZ);
					
					sensorLastTimestamp = event.timestamp;
				}
				break;
			case Sensor.TYPE_ROTATION_VECTOR:
				break;
			case Sensor.TYPE_RELATIVE_HUMIDITY:
				if (deltaSeconds >= SENSOR_UPDATE_SECONDS) { // Hold sensor updates
					// Parameters
					float airHumidityPercent = event.values[0];
					
					Log.d(TAG, "Humidity (%): " + airHumidityPercent + " // " + sensor.getMaximumRange());

					// Event
					mCanvasView.nClouds.setCloudy(EventWeatherStorm.setCloudyDay(airHumidityPercent));

					sensorLastTimestamp = event.timestamp;
				}
				break;
			case Sensor.TYPE_AMBIENT_TEMPERATURE:
				break;
			}
		}
	}

	private boolean arrayContainsValue(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value)
				return true;
		}
		return false;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Nothing
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.REQUEST_SHOP:
				if(resultCode == Activity.RESULT_OK){
					mCanvasView.destroyIsland();
				}
				break;
		}
	}

	public void gameOver(Player nPlayer) {
		Intent gameOverIntent = new Intent(this, GameOverActivity.class);
		// Parcelable Extra with Player object content
		gameOverIntent.putExtra(Constants.TAG_GAME_OVER, Player.clonePlayer(nPlayer));
		this.startActivity(gameOverIntent);
		shutdownGame();
	}

	public void shop(Island nIsland) {
		Intent shopIntent = new Intent(this, ShopActivity.class);
		shopIntent.putExtra(Constants.ITEMLIST_NATURE, nIsland.hasShop() ? Constants.NATURE_SHOP : Constants.NATURE_TREASURE);
		this.startActivityForResult(shopIntent, Constants.REQUEST_SHOP);
	}

	/**
	 * Free resources
	 */
	public void shutdownGame() {
		mCanvasView = null;
		finish();
	}
	
	public void resetUiWheel(){
		ctrlWheel.resetWheel();
	}

	public void doWheelRotation() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ctrlWheel.rotateWheel();
			}
		});
	}

}