package com.pirateseas.view.activities;

import java.util.ArrayList;
import java.util.List;

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
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.controller.sensors.events.EventDayNightCycle;
import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.entity.Ship;
import com.pirateseas.model.canvasmodel.ui.UIDisplayElement;
import com.pirateseas.model.canvasmodel.ui.Wheel;
import com.pirateseas.utils.approach2d.GameHelper;
import com.pirateseas.utils.approach2d.Geometry;
import com.pirateseas.view.graphics.canvasview.CanvasView;

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
	
	public ImageButton btnPause;
	public UIDisplayElement mGold, mAmmo;
	public Wheel ctrlWheel;

	Point startPoint;
	Point centerPoint;
	Point endPoint;
	float degrees;

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
		setContentView(R.layout.activity_game);
		
		btnPause = (ImageButton) findViewById(R.id.btnPause);
		btnPause.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent pauseIntent = new Intent(context, PauseActivity.class);
				context.startActivity(pauseIntent);
			}
		});
		
		ctrlWheel = (Wheel) findViewById(R.id.controlWheel);
		centerPoint = ctrlWheel.getCenterPoint();
		ctrlWheel.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						startPoint = new Point((int)event.getX(),(int)event.getY());
						// Toast.makeText(context, "DOWN touch registered (" + startPoint.x + ", " + startPoint.y + ")", Toast.LENGTH_SHORT).show();
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						endPoint = new Point((int)event.getX(),(int)event.getY());
						degrees = Geometry.getRotationAngle(startPoint, centerPoint, endPoint);
						((Wheel) v).setDegrees(degrees);
						v.postInvalidate();
						// Toast.makeText(context, "UP touch registered (" + endPoint.x + ", " + endPoint.y + ") [" + degrees + " degrees]", Toast.LENGTH_SHORT).show();
						break;
				}
				
				return true;
			}			
		});
		
		ctrlWheel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Wheel) v).setDegrees(degrees = 0);
				v.postInvalidate();
			}
		});
		
		mGold = (UIDisplayElement) findViewById(R.id.playerGold);
		GameHelper.loadGame(this, new Player(), new Ship());
		int gGold = GameHelper.helperPlayer.getGold();
		mGold.setElementValue(gGold);
		mAmmo = (UIDisplayElement) findViewById(R.id.playerAmmunition);
		mAmmo.setElementValue(0);
    }

    @Override
    protected void onPause() {
		//CanvasView.pauseGame(true);
		mSensorManager.unregisterListener(this);
		
        super.onPause();
    }

    @Override
    protected void onResume() {
    	
		if (!CanvasView.nUpdateThread.isAlive() && CanvasView.nUpdateThread.getState() != Thread.State.NEW){
			//Log.e(TAG, "MainLogic is DEAD. Re-starting...");
			mCanvasView.launchMainLogic();
			CanvasView.nUpdateThread.start();
		}		
		
		for(int i = 0, size = triggeringSensors.size(); i < size; i++){
			Sensor s = triggeringSensors.get(i);
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
	                	   /*
	                	   try {
	                		   mCanvasView = (CanvasView) findViewById(R.id.gameLayer);
	                		   mCanvasView.saveGame();
	                	   } catch (SaveGameException e) {
	                		   Log.e(TAG, e.getMessage());
	                	   }
	                	   */
	                	   
	                	   CanvasView.nStatus = Constants.GAME_STATE_END;
	                	   CanvasView.nUpdateThread.setRunning(false);
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

	@SuppressWarnings("unused")
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
	}
    
}