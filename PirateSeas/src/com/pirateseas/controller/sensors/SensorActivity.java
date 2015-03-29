package com.pirateseas.controller.sensors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pirateseas.R;
import com.pirateseas.global.Constants;
import com.pirateseas.controller.sensors.events.EventDayNightCycle;
import com.pirateseas.controller.sensors.events.EventEnemyTimer;
import com.pirateseas.controller.sensors.events.EventWeatherFog;
import com.pirateseas.controller.sensors.events.EventWeatherMaelstrom;
import com.pirateseas.controller.sensors.events.EventWeatherStorm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

public class SensorActivity extends Activity{
	
	private static final long serialVersionUID = 1L;
	public static final String TAG = "com.pirateseas.view.activities.SensorActivity";
	
	private SharedPreferences mPreferences = null;
	private int[] preferenceSensorList = null;
	
	private SensorManager mSensorManager;
	private List<Integer> mDeviceSensorTypes;
	private Sensor mSensor;
	
	private TextView tv;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set animation layout while loading
		setContentView(R.layout.activity_sensors);
		
		tv = (TextView) findViewById(R.id.lbl_load_status);
	
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mDeviceSensorTypes = new ArrayList<Integer>(); // Performance of Java's Lists @source: http://www.onjava.com/pub/a/onjava/2001/05/30/optimization.html
		
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    	mPreferences = getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE); 
		
		// Check preferences if its already called 
		preferenceSensorList = getPreferenceSensorList();
		if(hasValidValues(preferenceSensorList)){
			for(int i = 0; i < preferenceSensorList.length; i++){
				mDeviceSensorTypes.add(preferenceSensorList[i]); 
			}
			exitActivity(true);
		} else {
			// Call AsyncTask
			AsyncTask<Void,Integer,Boolean> listValidSensors = new ListSensors();
			listValidSensors.execute();
		}
	}
	
	private boolean hasValidValues(int[] sensorList){
		for(int i = 0; i < sensorList.length; i++){
			if(sensorList[i] != 0)
				return true;
		}
		return false;
	}
	
	private int[] getPreferenceSensorList(){
		String prefStringSensorList = "";
		String[] stuff = null;
		int prefLength = 0;
		prefStringSensorList = mPreferences.getString(Constants.PREF_SENSOR_LIST,Constants.EMPTY_STRING);
		if(prefStringSensorList.length() > 0){
			stuff = prefStringSensorList.split(";");
			prefLength = stuff.length;
		}
		
		int[] preferenceIntArray = new int[SensorType.values().length];
		for(int i = 0; ((i < preferenceIntArray.length) && (i < prefLength)); i++){
			int value = Integer.valueOf(stuff[i]);
			preferenceIntArray[i] = value;
		}
		
		return preferenceIntArray;
	}
	
	private String putPreferenceSensorList(){
		String modifiedString = "";
		Object[] valuesArray =  mDeviceSensorTypes.toArray();
		for(int i = 0; i < valuesArray.length; i++){
			modifiedString += String.valueOf(valuesArray[i]) + ";";
		}
		
		return modifiedString;
	}
	
	private void exitActivity(boolean result){
		Intent sensorListIntent = new Intent();
		
		Object[] tmpObjectArray = mDeviceSensorTypes.toArray();
		int[] extraIntArray = new int[tmpObjectArray.length];
		
		for(int i = 0; i < tmpObjectArray.length; i++){
			extraIntArray[i] = (int) tmpObjectArray[i];
		}
		
		sensorListIntent.putExtra(Constants.TAG_SENSOR_LIST, extraIntArray);
		
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString(Constants.PREF_SENSOR_LIST, putPreferenceSensorList());
		editor.commit();
		
		if(result)
			setResult(RESULT_OK, sensorListIntent);
		else
			setResult(RESULT_CANCELED, sensorListIntent);
		finish();
	}
	
	private class ListSensors extends AsyncTask<Void, Integer, Boolean>{
		List<SensorType> triggeredSensors = new ArrayList<SensorType>();
		
		@Override
		protected void onPreExecute(){
			// Set event generator' sensor list
			triggeredSensors.add(EventDayNightCycle.getSensorType());
			triggeredSensors.add(EventEnemyTimer.getSensorType());
			triggeredSensors.add(EventWeatherFog.getSensorType());
			triggeredSensors.add(EventWeatherMaelstrom.getSensorType());
			triggeredSensors.add(EventWeatherStorm.getSensorType());
			
			// Start animation
			tv.setText(getResources().getStringArray(R.array.loading_messages)[0]);
		}
		
		@Override
		protected Boolean doInBackground(Void... params){
			int count = SensorType.values().length;
			for(int i = 0; i < count; i++){
				SensorType type = SensorType.values()[i];
				if((mSensor = mSensorManager.getDefaultSensor(type.getCode())) != null && triggeredSensors.contains(type)){
					mDeviceSensorTypes.add(type.getCode());
				} else {
					// Sorry, there is no 'type' sensor on your device.
					// The 'event' event will be disabled.
					if (triggeredSensors.contains(type))
						Log.d(TAG, "No " + type + " sensor detected on your device. Its event will be disabled.");
					else
						Log.d(TAG, "No " + type + " sensor detected on your device.");
				}
				publishProgress((int) ((i / (float) count) * 100));
				SystemClock.sleep(1500);
			}
			
			return mDeviceSensorTypes.isEmpty() ? false : true;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			// Update animation
			String[] messagesArray = getResources().getStringArray(R.array.loading_messages);
			tv.setText(messagesArray[progress[0]/messagesArray.length]);
		}
		
		protected void onPostExecute(Boolean result){
			exitActivity(result);
		}
	}
}