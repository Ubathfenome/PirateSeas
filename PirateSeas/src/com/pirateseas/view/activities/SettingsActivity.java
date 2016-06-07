package com.pirateseas.view.activities;

import com.pirateseas.R;
import com.pirateseas.controller.audio.MusicManager;
import com.pirateseas.global.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	private TextView txtTitleLabel;
	private TextView txtVolumeLabel;
	private SeekBar skbVolume;
	private Button btnRestore;
	private Button btnSettingsAccept;
	private Button btnSettingsCancel;
	private Switch swControlMode;
	private CheckBox chkUseAmmoKeys;

	private float volumeValue = 0f;
	private boolean controlValue = false;
	private boolean ammoKeysEnabled = false;
	private String labelValue;
	
	private boolean mDebugMode;

	SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		mPreferences = getSharedPreferences(Constants.TAG_PREF_NAME,
				Context.MODE_PRIVATE);
		
		mDebugMode = mPreferences.getBoolean(Constants.TAG_EXE_MODE, false);

		txtTitleLabel = (TextView) findViewById(R.id.txtSettingsLabel);
		Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/"
				+ Constants.FONT_NAME + ".ttf");
		txtTitleLabel.setTypeface(customFont);
		
		volumeValue = (int) mPreferences.getFloat(
				Constants.PREF_DEVICE_VOLUME, MusicManager
				.getInstance(this).getDeviceVolume());
		controlValue = mPreferences.getBoolean(Constants.PREF_CONTROL_MODE, Constants.PREF_GAME_TOUCH);
		ammoKeysEnabled = mPreferences.getBoolean(Constants.PREF_USE_AMMO_KEYS, false);

		txtVolumeLabel = (TextView) findViewById(R.id.txtVolumeLabel);
		labelValue = (String) txtVolumeLabel.getText();
		txtVolumeLabel.setText(labelValue + " " + (int) volumeValue);

		skbVolume = (SeekBar) findViewById(R.id.sbVolume);
		skbVolume.setProgress((int) volumeValue);
		skbVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser)
					volumeValue = progress;
				MusicManager.getInstance().setDeviceVolume(progress);
				txtVolumeLabel.setText(labelValue + " " + progress);
			}
		});
		
		swControlMode = (Switch) findViewById(R.id.tbControlMode);
		swControlMode.setChecked(controlValue);
		swControlMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				v.setChecked(isChecked);
				controlValue = isChecked;
			}
		});
		
		chkUseAmmoKeys = (CheckBox) findViewById(R.id.chkChangeAmmo);
		chkUseAmmoKeys.setChecked(ammoKeysEnabled);
		chkUseAmmoKeys.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
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

		btnSettingsAccept = (Button) findViewById(R.id.btnSettingsAccept);
		btnSettingsAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Save changes in preferences
				SharedPreferences.Editor editor = mPreferences.edit();
				editor.putFloat(Constants.PREF_DEVICE_VOLUME, volumeValue);
				editor.putBoolean(Constants.PREF_CONTROL_MODE, controlValue);
				editor.putBoolean(Constants.PREF_USE_AMMO_KEYS, ammoKeysEnabled);
				editor.commit();

				finish();
			}
		});

		btnSettingsCancel = (Button) findViewById(R.id.btnSettingsCancel);
		btnSettingsCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		findViewById(R.id.rootLayoutSettings).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);		
		super.onResume();
	}

	private boolean resetPreferences() {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.clear();
		editor.putBoolean(Constants.TAG_EXE_MODE, mDebugMode);
		editor.putBoolean(Constants.PREF_CONTROL_MODE, Constants.PREF_GAME_TOUCH);
		editor.putBoolean(Constants.PREF_USE_AMMO_KEYS, false);
		return editor.commit();
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

}
