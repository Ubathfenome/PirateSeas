package com.pirateseas.view.activities;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.global.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class GameOverActivity extends Activity {
	
	Player  p = null;
	
	TextView txtDays, txtScore;

	// OnCreate
	//: Display results	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_gameover);
		
		// GetIntent Extras
		Intent intent = getIntent();
		p = intent.getParcelableExtra(Constants.TAG_GAME_OVER);
		
		txtDays = (TextView) findViewById(R.id.txtDays);
		txtScore = (TextView) findViewById(R.id.txtScore);
		
		int score = p.getLevel() * p.getExperience() + p.getGold() * p.getPassedDays();
		
		txtDays.setText("" + p.getPassedDays());
		txtScore.setText("" + score);
		
		// TODO Upload score to the cloud? | Save score in the preferences?
	}

	// OnTouch
	//: DESTROY EVERYTHING!! Muahahaha!! >:D
	//: Start Main Menu Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			Intent newGameTaskIntent = new Intent(this, MainMenuActivity.class);
			newGameTaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(newGameTaskIntent);
			finish();
		}
		
		return true;
	}
	
	
	
	
	
}
