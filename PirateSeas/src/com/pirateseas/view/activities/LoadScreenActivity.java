package com.pirateseas.view.activities;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class LoadScreenActivity extends Activity {
	
	private static final String TAG = "LoadScreenActivity";
	
	private TextView txtMessage;
	
	private Animation enterAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		Intent intent = getIntent();
		String message = intent.getStringExtra(Constants.TAG_LOAD_SCREEN);
		
		Log.d(TAG, "Message: " + message);
		
		setContentView(R.layout.activity_load_screen);
		txtMessage = (TextView) findViewById(R.id.txtMessage);
		Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/" + Constants.FONT_NAME + ".ttf");
		txtMessage.setTypeface(customFont);
		
		if(message != null){
			txtMessage.setText(message);
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
		
		
		enterAnimation = AnimationUtils.loadAnimation(this, R.anim.xml_difusse_animation);
		// @source: http://stackoverflow.com/questions/7606498/end-animation-event-android
		enterAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				txtMessage.setVisibility(View.INVISIBLE);
				setResult(RESULT_OK);
				finish();				
			}
		});
		
		txtMessage.startAnimation(enterAnimation);		
	}

}
