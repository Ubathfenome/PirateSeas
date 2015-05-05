package com.pirateseas.view.activities;

import com.pirateseas.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpActivity extends Activity {
	
	private static final int HELP_PAGES = 1;
	
	Button btnNext, btnPrev, btnAbout;
	
	int currentPage = 1;	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		btnNext = (Button) findViewById(R.id.btnBarNext);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentPage == HELP_PAGES)
					finish();
				else {
					currentPage++;
					btnPrev.setVisibility(View.VISIBLE);
				}
			}
		});
		
		btnPrev = (Button) findViewById(R.id.btnBarPrev);
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentPage == 2) 
					v.setVisibility(View.INVISIBLE);
				currentPage--;				
			}
		});
		
		btnAbout = (Button) findViewById(R.id.btnBarAbout);
		btnAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DisplayInfoDialogFragment displayDialog = new DisplayInfoDialogFragment();
				displayDialog.show(getFragmentManager(), "DisplayInfoDialogFragment");
			}
		});
		
	}
	
	private class DisplayInfoDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Activity dummyActivity = getActivity();
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(dummyActivity);
			builder.setTitle(
					getResources().getString(R.string.about_dialog_title))
					.setMessage(R.string.about_dialog_message)
					.setPositiveButton(R.string.about_dialog_positive,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// Show info
									dismiss();
								}
							});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}

	@Override
	protected void onResume() {
		findViewById(R.id.rootLayoutHelp).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		super.onResume();
	}
	
	
}
