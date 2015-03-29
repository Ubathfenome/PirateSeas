package com.pirateseas.view.activities;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PauseActivity extends Activity {
	
	private Context context;
	
	private TextView txtTitleLabel;
	private ImageButton btnSettings;
	private ImageButton btnHelp;
	private Button btnResume;
	private Button btnExit;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pause);
		// TODO
		// UNDO
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		txtTitleLabel = (TextView) findViewById (R.id.txtPauseLabel);
		Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/" + Constants.FONT_NAME + ".ttf");
		txtTitleLabel.setTypeface(customFont);
		
		btnSettings = (ImageButton) findViewById(R.id.btnPauseSettings);
		btnSettings.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent settingsIntent = new Intent(context, SettingsActivity.class);
				startActivity(settingsIntent);
			}
		});
		
		btnHelp = (ImageButton) findViewById(R.id.btnPauseHelp);
		btnHelp.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent helpIntent = new Intent(context, HelpActivity.class);
				startActivity(helpIntent);
			}
		});
		
		btnResume = (Button) findViewById(R.id.btnPauseResume);
		btnResume.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				finish();
			}
		});
		
		btnExit = (Button) findViewById(R.id.btnPauseExit);
		btnExit.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				// TODO Check this...
				((GameActivity) context).onBackPressed();
				finish();
			}
		});
	}
}
