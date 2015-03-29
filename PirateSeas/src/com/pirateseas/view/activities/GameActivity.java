package com.pirateseas.view.activities;

import java.util.ArrayList;
import java.util.List;

import com.pirateseas.R;
import com.pirateseas.controller.sensors.events.EventDayNightCycle;
import com.pirateseas.exceptions.SaveGameException;
import com.pirateseas.global.Constants;
import com.pirateseas.view.graphics.canvasview.CanvasView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class GameActivity extends Activity implements SensorEventListener{
	
	private static final String TAG = "com.pirateseas.GAME_ACTIVITY";
	
	private Context context;

	// private GLSurfaceView mGLView;
	private CanvasView mCanvasView;
	
	private static final long SENSOR_UPDATE_PERIOD = 15 * Constants.SECONDS_TO_NANOS; // 1 segundo = 1 Millardo (1 000 000 000) nanosegundos
	
	protected int[] sensorTypes = null;
	protected long sensorLastTimestamp;
	protected SensorManager mSensorManager;
	protected List<Sensor> triggeringSensors;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		context = this;

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        // mGLView = new GLSView(this);
		
		mCanvasView = new CanvasView(this);
		
		// Receive the device event triggering sensor list
		triggeringSensors = new ArrayList<Sensor>();
		sensorTypes = getIntent().getIntArrayExtra(Constants.TAG_SENSOR_LIST);
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		for(int i = 0; i < sensorTypes.length; i++){
			if(sensorTypes[i] != 0)
				triggeringSensors.add(mSensorManager.getDefaultSensor(sensorTypes[i]));
		}
		
		
		// Launch the game!!
		setContentView(mCanvasView);
    }

    @Override
    protected void onPause() {
		CanvasView.pauseGame(true);
		mSensorManager.unregisterListener(this);
		
        super.onPause();
    }

    @Override
    protected void onResume() {
		if (!mCanvasView.mainLogic.isAlive() && mCanvasView.mainLogic.getState() != Thread.State.NEW){
			//Log.e(TAG, "MainLogic is DEAD. Re-starting...");
			mCanvasView.launchMainLogic();
			mCanvasView.mainLogic.start();
		}
		CanvasView.pauseGame(false);
		
		for(Sensor s : triggeringSensors){
			mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_GAME);
		}
		
        super.onResume();
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {		
		// Pop up messageBox asking if the user is sure to leave
		LeaveGameDialogFragment exitDialog = new LeaveGameDialogFragment();
		exitDialog.show(getFragmentManager(),"LeaveGameDialog");
	}
    
	private class LeaveGameDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	final Activity dummyActivity = getActivity();
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(dummyActivity);
	        builder.setTitle(getResources().getString(R.string.exit_dialog_title))
				   .setMessage(R.string.exit_dialog_message)
	               .setPositiveButton(R.string.exit_dialog_positive, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // Exit
	                	   try {
							mCanvasView.saveGame();
						} catch (SaveGameException e) {
							Log.e(TAG, e.getMessage());
						}
						   mCanvasView.mStatus = Constants.GAME_STATE_END;
						   mCanvasView.mainLogic.setRunning(false);
	                	   dummyActivity.finish();
	                   }
	               })
	               .setNegativeButton(R.string.exit_dialog_negative, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
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
		if (arrayContainsValue(sensorTypes, sensor.getType())){
			switch(sensor.getType()){
				case Sensor.TYPE_ACCELEROMETER: break;
				case Sensor.TYPE_MAGNETIC_FIELD: break;
				case Sensor.TYPE_GYROSCOPE: 
					float axisSpeedX = event.values[0];
					float axisSpeedY = event.values[1];
					float axisSpeedZ = event.values[2];
				
					break;
				case Sensor.TYPE_LIGHT: 
					if(event.timestamp - sensorLastTimestamp >= SENSOR_UPDATE_PERIOD){ // Hold sensor updates
						float lux = event.values[0];
						EventDayNightCycle.adjustScreenBrightness(context, lux % 100);
						sensorLastTimestamp = event.timestamp;
					}
					break;
				case Sensor.TYPE_PRESSURE: 
					float millibar = event.values[0];
					
					break;
				case Sensor.TYPE_PROXIMITY: break;
				case Sensor.TYPE_GRAVITY: break;
				case Sensor.TYPE_LINEAR_ACCELERATION: 
					
					break;
				case Sensor.TYPE_ROTATION_VECTOR: break;
				case Sensor.TYPE_RELATIVE_HUMIDITY: 
					float airHumidityPercent = event.values[0];
					break;
				case Sensor.TYPE_AMBIENT_TEMPERATURE: break;
			}
		}
	}
	
	private boolean arrayContainsValue(int[] array, int value){
		for(int i = 0; i < array.length; i++){
			if(array[i] == value)
				return true;
		}
		return false;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
    
}