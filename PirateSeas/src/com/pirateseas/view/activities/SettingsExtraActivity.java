package com.pirateseas.view.activities;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsExtraActivity extends Activity {
	
	private Button btnRestore;
	private Switch swControlMode;
	private ToggleButton tglChangeAmmo;
	
	private boolean controlValue = false;
	private boolean ammoKeysEnabled = false;
	
	private boolean mDebugMode;

	SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_settings_extra);

		mPreferences = getSharedPreferences(Constants.TAG_PREF_NAME,
				Context.MODE_PRIVATE);

		mDebugMode = mPreferences.getBoolean(Constants.TAG_EXE_MODE, false);
		
		swControlMode = (Switch) findViewById(R.id.tbControlMode);
		swControlMode.setChecked(controlValue);
		swControlMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				v.setChecked(isChecked);
				controlValue = isChecked;
			}
		});
		
		tglChangeAmmo = (ToggleButton) findViewById(R.id.tglChangeAmmo);
		tglChangeAmmo.setChecked(ammoKeysEnabled);
		tglChangeAmmo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				v.setChecked(isChecked);
				ammoKeysEnabled = isChecked;
			}
		});

		btnRestore = (Button) findViewById(R.id.btnSettingsRestore);
		btnRestore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ResetPreferencesDialogFragment restoreDialog = new ResetPreferencesDialogFragment();
				restoreDialog.show(getFragmentManager(),
						"RestorePreferencesDialog");
			}
		});
	}	
	
	@Override
	public void onBackPressed() {
		// Save changes in preferences
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putBoolean(Constants.PREF_CONTROL_MODE, controlValue);
		editor.putBoolean(Constants.PREF_USE_AMMO_KEYS, ammoKeysEnabled);
		editor.commit();

		finish();
	}

	private class ResetPreferencesDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Activity dummyActivity = getActivity();
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(dummyActivity);
			builder.setTitle(
					getResources().getString(
							R.string.settings_restore_dialog_title))
					.setMessage(R.string.settings_restore_dialog_message)
					.setPositiveButton(
							R.string.settings_restore_dialog_positive,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									String message = resetPreferences() ? "Preferences sucessfully restored"
											: "Error while restoring preferences";
									Toast.makeText(getActivity(), message,
											Toast.LENGTH_SHORT).show();
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
	
	private boolean resetPreferences() {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.clear();
		editor.putBoolean(Constants.TAG_EXE_MODE, mDebugMode);
		editor.putBoolean(Constants.PREF_CONTROL_MODE, Constants.PREF_GAME_TOUCH);
		editor.putBoolean(Constants.PREF_USE_AMMO_KEYS, false);
		return editor.commit();
	}

}
