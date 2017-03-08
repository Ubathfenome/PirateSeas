package com.pirateseas.view.activities;

import java.util.Random;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.global.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ScreenSelectionActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "ScreenSelectionActivity";

	private ImageView imgIslandLeft;
	private ImageView imgIslandFront;
	private ImageView imgIslandRight;

	private ImageButton btnLeft;
	private ImageButton btnFront;
	private ImageButton btnRight;

	private int islandSide;
	private Player p = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_screen_selection);

		imgIslandLeft = (ImageView) findViewById(R.id.imgIslandLeft);
		imgIslandFront = (ImageView) findViewById(R.id.imgIslandFront);
		imgIslandRight = (ImageView) findViewById(R.id.imgIslandRight);

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

		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (islandSide == 1) {
					enterRandomIsland();
				} else if (randomEncounter()) {
					// Devolver el valor a la tarea "padre"
					Intent data = new Intent();
					data.putExtra(Constants.TAG_RANDOM_ENCOUNTER, true);
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
				} else if (randomEncounter()) {
					// Devolver el valor a la tarea "padre"
					Intent data = new Intent();
					data.putExtra(Constants.TAG_RANDOM_ENCOUNTER, true);
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
				} else if (randomEncounter()) {
					// Devolver el valor a la tarea "padre"
					Intent data = new Intent();
					data.putExtra(Constants.TAG_RANDOM_ENCOUNTER, true);
					setResult(RESULT_OK, data);
					finish();
				} else {
					reloadSelection();
				}
			}
		});
	}

	protected void reloadSelection() {
		// Call activity LoadScreen with Result
		Intent loadScreenIntent = new Intent(this, LoadScreenActivity.class);
		loadScreenIntent.putExtra(Constants.TAG_LOAD_SCREEN, getString(R.string.message_nothinghere));
		startActivityForResult(loadScreenIntent, Constants.REQUEST_LOAD_SCREEN);
	}

	protected boolean randomEncounter() {
		// Generate random value with rate depending on player's level
		int playerLevel = p.getLevel();
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
		this.startActivityForResult(shopIntent, Constants.REQUEST_ISLAND);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constants.REQUEST_ISLAND:
			if (resultCode == Activity.RESULT_OK) {
				reloadSelection();
			}
			break;
		case Constants.REQUEST_LOAD_SCREEN:
			if (resultCode == Activity.RESULT_OK) {
				Intent resetIntent = new Intent(this, ScreenSelectionActivity.class);
				resetIntent.putExtra(Constants.TAG_SCREEN_SELECTION_ISLANDDATA, islandSide);
				resetIntent.putExtra(Constants.TAG_SCREEN_SELECTION_PLAYERDATA, p);
				this.startActivity(resetIntent);
				finish();
			}
			break;
		}
	}

}
