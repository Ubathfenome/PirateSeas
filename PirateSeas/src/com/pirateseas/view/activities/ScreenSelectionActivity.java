package com.pirateseas.view.activities;

import java.util.Random;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.global.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ScreenSelectionActivity extends Activity {

	private static final String TAG = "ScreenSelectionActivity";

	private ImageView imgIslandLeft;
	private ImageView imgIslandFront;
	private ImageView imgIslandRight;

	private ImageButton btnLeft;
	private ImageButton btnFront;
	private ImageButton btnRight;

	private int islandSide;
	private Player p = null;
	
	@SuppressWarnings("unused")
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_screen_selection);

		this.context = this;
		
		imgIslandLeft = (ImageView) findViewById(R.id.imgIslandLeft);
		imgIslandFront = (ImageView) findViewById(R.id.imgIslandFront);
		imgIslandRight = (ImageView) findViewById(R.id.imgIslandRight);
		
		btnLeft = (ImageButton) findViewById(R.id.btnLeft);
		btnFront = (ImageButton) findViewById(R.id.btnFront);
		btnRight = (ImageButton) findViewById(R.id.btnRight);

		Intent intent = getIntent();

		islandSide = intent.getIntExtra(Constants.TAG_SCREEN_SELECTION_ISLANDDATA, 0);
		p = intent.getParcelableExtra(Constants.TAG_SCREEN_SELECTION_PLAYERDATA);

		switch (islandSide) {
		case 1:
			imgIslandLeft.setVisibility(View.VISIBLE);
			imgIslandFront.setVisibility(View.INVISIBLE);
			imgIslandRight.setVisibility(View.INVISIBLE);
			break;
		case 2:
			imgIslandLeft.setVisibility(View.INVISIBLE);
			imgIslandFront.setVisibility(View.VISIBLE);
			imgIslandRight.setVisibility(View.INVISIBLE);
			break;
		case 3:
			imgIslandLeft.setVisibility(View.INVISIBLE);
			imgIslandFront.setVisibility(View.INVISIBLE);
			imgIslandRight.setVisibility(View.VISIBLE);
			break;
		default:
			imgIslandLeft.setVisibility(View.INVISIBLE);
			imgIslandFront.setVisibility(View.INVISIBLE);
			imgIslandRight.setVisibility(View.INVISIBLE);
			break;
		}
		
		final boolean encounter = randomEncounter();

		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (islandSide == 1) {
					enterRandomIsland();
				} else if (encounter) {
					// Devolver el valor a la tarea "padre"
					Intent data = new Intent();
					data.putExtra(Constants.TAG_RANDOM_ENCOUNTER, true);
					Log.d(TAG,"Finish ScreenSelection Activty");
					setResult(RESULT_OK, data);
					finish();
				} else {
					reloadSelection();
				}
			}
		});

		btnFront.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (islandSide == 2) {
					enterRandomIsland();
				} else if (encounter) {
					// Devolver el valor a la tarea "padre"
					Intent data = new Intent();
					data.putExtra(Constants.TAG_RANDOM_ENCOUNTER, true);
					Log.d(TAG,"Finish ScreenSelection Activty");
					setResult(RESULT_OK, data);
					finish();
				} else {
					reloadSelection();
				}
			}
		});

		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (islandSide == 3) {
					enterRandomIsland();
				} else if (encounter) {
					// Devolver el valor a la tarea "padre"
					Intent data = new Intent();
					data.putExtra(Constants.TAG_RANDOM_ENCOUNTER, true);
					Log.d(TAG,"Finish ScreenSelection Activty");
					setResult(RESULT_OK, data);
					finish();
				} else {
					reloadSelection();
				}
			}
		});
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected boolean randomEncounter() {
		// Generate random value with rate depending on player's level
		int playerLevel = 1;
		if (p != null)
			playerLevel = p.getLevel();
		
		if(playerLevel == 0)
			playerLevel = 1;
		
		double logarythm = Math.log(playerLevel);
		if (logarythm % 2 == 0)
			return true;
		else
			return false;
	}

	private void enterRandomIsland() {
		Random rand = new Random();
		boolean yesNo = rand.nextBoolean();
		Intent shopIntent = new Intent(this, ShopActivity.class);
		shopIntent.putExtra(Constants.ITEMLIST_NATURE, yesNo ? Constants.NATURE_SHOP : Constants.NATURE_TREASURE);
		Log.d(TAG,"Start Shop ForResult Intent");
		this.startActivityForResult(shopIntent, Constants.REQUEST_ISLAND);
	}
	
	private void reloadSelection(){
		Toast.makeText(this, getResources().getString(R.string.message_nothinghere), Toast.LENGTH_SHORT).show();
		
		Intent resetIntent = new Intent(this, ScreenSelectionActivity.class);
		resetIntent.putExtra(Constants.TAG_SCREEN_SELECTION_ISLANDDATA, islandSide);
		resetIntent.putExtra(Constants.TAG_SCREEN_SELECTION_PLAYERDATA, p);
		Log.d(TAG,"Reset ScreenSelection Intent");
		this.startActivity(resetIntent);
		Log.d(TAG,"Finish ScreenSelection Activty");
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Log.d(TAG, "ScreenSelectionActivity called by " + this.getCallingActivity().getClassName());
		if (data != null && requestCode == Constants.REQUEST_ISLAND && resultCode == Activity.RESULT_OK)
			reloadSelection();
	}

}
