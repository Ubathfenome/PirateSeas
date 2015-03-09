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
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

public class SensorActivity extends Activity{
	
	private static final long serialVersionUID = 1L;
	public static final String TAG = "com.pirateseas.view.activities.SensorActivity";
	
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
		
		// Crear layout que muestre una animacion de carga
		setContentView(R.layout.activity_sensors);
		
		tv = (TextView) findViewById(R.id.lbl_load_status);
	
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mDeviceSensorTypes = new ArrayList<Integer>(); // Performance of Java's Lists @source: http://www.onjava.com/pub/a/onjava/2001/05/30/optimization.html
		
		// Llamar AsyncTask
		AsyncTask<Void,Integer,Boolean> listValidSensors = new ListSensors();
		listValidSensors.execute();
	}
	
	private void exitActivity(boolean result){
		Intent sensorListIntent = new Intent();		
		sensorListIntent.putExtra(Constants.SENSOR_LIST, mDeviceSensorTypes.toArray());
		
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
			// Iniciar lista de sensores generadores de eventos
			triggeredSensors.add(EventDayNightCycle.getSensorType());
			triggeredSensors.add(EventEnemyTimer.getSensorType());
			triggeredSensors.add(EventWeatherFog.getSensorType());
			triggeredSensors.add(EventWeatherMaelstrom.getSensorType());
			triggeredSensors.add(EventWeatherStorm.getSensorType());
			
			// Iniciar animacion
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
					Log.e(TAG, "Sorry, there is no " + type + " sensor on your device. Its event will be disabled.");
				}
				publishProgress((int) ((i / (float) count) * 100));
				SystemClock.sleep(1500);
			}
			
			return mDeviceSensorTypes.isEmpty() ? false : true;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			// Actualizar animacion
			tv.setText(getResources().getStringArray(R.array.loading_messages)[progress[0]/10]);
		}
		
		protected void onPostExecute(Boolean result){
			exitActivity(result);
		}
	}
}