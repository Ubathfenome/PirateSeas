package com.pirateseas.view.activities;

import com.pirateseas.R;
import com.pirateseas.global.Constants;
import com.pirateseas.view.graphics.canvasview.CanvasView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class GameActivity extends Activity implements SensorEventListener{
	
	private static final String TAG = "com.pirateseas.GAME_ACTIVITY";

	private GLSurfaceView mGLView;
	private CanvasView mCanvasView;
	
	protected int[] sensorTypes = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        // mGLView = new GLSView(this);
		
		mCanvasView = new CanvasView(this);
		
		// Receive the device event triggering sensor list
		sensorTypes = getIntent().getIntArrayExtra(Constants.TAG_SENSOR_LIST);
		
		// Launch the game!!
		setContentView(mCanvasView);
    }

    @Override
    protected void onPause() {
		CanvasView.pauseGame(true);
		
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
						   // TODO Save
	                	   
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
    
}