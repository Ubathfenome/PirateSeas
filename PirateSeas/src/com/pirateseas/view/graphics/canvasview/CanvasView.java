package com.pirateseas.view.graphics.canvasview;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.controller.sensors.events.EventDayNightCycle;
import com.pirateseas.controller.timer.GameTimer;
import com.pirateseas.exceptions.SaveGameException;
import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.entity.Ship;
import com.pirateseas.model.canvasmodel.game.entity.ShipType;
import com.pirateseas.model.canvasmodel.game.scene.Sea;
import com.pirateseas.model.canvasmodel.game.scene.Sky;
import com.pirateseas.model.canvasmodel.ui.UILayer;
import com.pirateseas.utils.GameHelper;
import com.pirateseas.view.activities.GameActivity;
import com.pirateseas.view.activities.PauseActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "CanvasView";
	private Context context;
	public static MainLogic mainLogic;
	
	private int screenWidth;
	private int screenHeight;
	public static int mStatus;
	
	private GameTimer gameTimer;
	private Player player;
	
	private Sky sky;
	private Sea sea;
	// private Clouds clouds;
	// private Sun sun;
	private Ship playerShip;
	// private Ship enemyShip;
	// private List<Shot> shotList;
	// private UILayer ui;
	
	private boolean mInitialized = false;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public CanvasView(Context context) {
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
		this.context = context;
		launchMainLogic();
	}

	/**
	 * Launches the main thread
	 */
	public void launchMainLogic() {
		mainLogic = null;
		mainLogic = new MainLogic(getHolder(), this);
	}
	
	public void initialize() {
		mStatus = Constants.GAME_STATE_NORMAL;
		
		gameTimer = new GameTimer();
		
		// Initialize components
		// Scene
		sky = new Sky(context, 0, 0, screenWidth, screenHeight);
		sea = new Sea(context, 0, 100, screenWidth, screenHeight);
		
		// Entities
		playerShip = new Ship(context, ShipType.LIGHT, screenWidth / 2,
			screenHeight / 8, screenWidth, screenHeight, new Point(0, 0), 2,
			3, 5, 20);
		
		// UI Layer
		//ui = new UILayer(context);
		
		mInitialized = true;
	}
	
	public boolean isInitialized() {
		return mInitialized;
	}
	
	public void loadGame(){
		GameHelper.loadGame(context, player, playerShip);
		player = GameHelper.helperPlayer;
		playerShip = GameHelper.helperShip;
	}
	
	public void saveGame() throws SaveGameException{
		if(GameHelper.saveGame(context, player, playerShip))
			Log.v(TAG, "Game saved");
		else
			throw new SaveGameException();
	}

	/**
	 * Draws all objects on the screen
	 * 
	 * @param canvas
	 */
	protected void drawOnScreen(Canvas canvas) {
		sky.drawOnScreen(canvas);
		sea.drawOnScreen(canvas);
		
		playerShip.drawOnScreen(canvas);
		
		/*
		for(Ship enemy : enemyShips){
			enemy.drawOnScreen(canvas);
		}
		*/
		//ui.drawOnScreen(canvas);
	}

	
	/**
	 * Checks if a button have been pressed
	 * 
	 * @param event
	 * @param pressed
	 * @return true if pressed, false otherwise
	 */
	/*
	private boolean inBounds(MotionEvent event, View pressed) {
		if (pressed.isEnabled()) {
			if (event.getX(0) > pressed.getX()
					&& event.getX(0) < pressed.getX() + pressed.getWidth() - 1
					&& event.getY(0) > pressed.getY()
					&& event.getY(0) < pressed.getY() + pressed.getHeight() - 1)
				return true;
			return false;
		} else
			return false;
	}
	*/

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int pointerCounter = event.getPointerCount(); // Number of pulsations
		
		float x = event.getX();
		float y = event.getY();
		
		if (mStatus == Constants.GAME_STATE_NORMAL) {
			if((x > (getWidth() - (getWidth() / 6))) && (y < getHeight() / 6)){
				mStatus = Constants.GAME_STATE_PAUSE;
				Intent pauseActivityIntent = new Intent(context, PauseActivity.class);
				context.startActivity(pauseActivityIntent);
			} else {
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						break;
					case MotionEvent.ACTION_UP:
						/*
						if(isTouching(Throttle)){
						} else if(isTouching(Wheel)){
						} else {
							if (motionUp(event)){
								shotList.add(ship.shootFront());
							} else if(motionSide(event).equals(Constants.RIGHT)){
								Shot[] shotArray = ship.shootSide(true);
								for(Shot shot : shotArray){
									shotList.add(shot);
								}
							} else if(motionSide(event).equals(Constants.LEFT)){
								Shot[] shotArray = ship.shootSide(false);
								for(Shot shot : shotArray){
									shotList.add(shot);
								}
							}
						}
						*/
						break;
				}
			}
		}
		return true;
	}
	
	public static void pauseGame(boolean hasToBePaused){
		if(hasToBePaused && mStatus == Constants.GAME_STATE_NORMAL){
			mStatus = Constants.GAME_STATE_PAUSE;
			mainLogic.setRunning(false);
		} else if (!hasToBePaused && mStatus == Constants.GAME_STATE_PAUSE){
			mStatus = Constants.GAME_STATE_NORMAL;
			mainLogic.setRunning(true);
		}
	}

	/**
	 * Ask the user if he/she really wants to exit the activity
	 */
	public void exitView() {
		mStatus = Constants.GAME_STATE_PAUSE;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(getResources().getString(R.string.exit_dialog_title))
				.setMessage(getResources().getString(R.string.exit_dialog_message))
				.setPositiveButton(getResources().getString(R.string.exit_dialog_positive),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Yes button clicked, do something
								mStatus = Constants.GAME_STATE_END;
							}
						})
				.setNegativeButton(getResources().getString(R.string.exit_dialog_negative),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// No button clicked, go back to normal mode
								mStatus = Constants.GAME_STATE_NORMAL;
							}
						});
		AlertDialog aD = builder.create();
		aD.setCanceledOnTouchOutside(false);
		aD.show();
	}

	/**
	 * Updates the view mStatus
	 */
	public synchronized void updateLogic() {
		manageTime();
		switch (mStatus) {
			case Constants.GAME_STATE_NORMAL:
				manageEvents();
				manageEntities();
				manageUI();
				break;
			case Constants.GAME_STATE_PAUSE:
				break;
			case Constants.GAME_STATE_END:
				mainLogic.setRunning(false);
				((GameActivity)context).finish();
				break;
		}
	}
	
	private void manageTime(){
		gameTimer.setTime(1/((System.currentTimeMillis() % Constants.MILLIS_PER_HOUR)+1));
	}
	
	private void manageUI() {
		// TODO Auto-generated method stub
		
	}

	private void manageEvents() {
		sky.rotate(EventDayNightCycle.getAngleOfDay(gameTimer.getHour()));
		// TODO Add more events
	}

	private void manageEntities(){
		managePlayer();
		manageEnemies();
		manageShots();
	}

	private void manageShots() {
		// TODO 
		/*
		for(Shot shot : shotList){
			if(shot.intersectionWithEntity(enemyShip))
				enemyShip.looseHealth(shot.getDamage());
		}
		*/
	}

	private void manageEnemies() {
		// TODO
		/*
		if(!enemyShip.isAlive())
			enemyShip.setStatus(Constants.STATE_DEAD);
		else{
			EnemyIA eIA = new EnemyIA(playerShip, enemyShip);
			enemyShip = eIA.getNextMove();
		}
		*/
		
	}

	/**
	*	Rotate the world around the player whenever he/she moves
	*/
	private void managePlayer() {
		// TODO
		// sky.move();
		// sea.move();
		// clouds.move();
		// sun.move();
		// compass.move();
		// enemy.move();
		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		String logTag = TAG + ": surfaceChanged";
		Log.d(logTag, "Ajuste de medidas: [" + width + ", " + height + "]");

		screenWidth = width;
		screenHeight = height;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		String logTag = TAG + ": surfaceCreated";
		Log.d(logTag, "Superficie creada");

		mainLogic.setRunning(true);
		mainLogic.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		String logTag = TAG + ": surfaceDestroyed";
		Log.d(logTag, "Superficie destruida");

		boolean tryAgain = true;
		while (tryAgain) {
			try {
				mainLogic.join();

				tryAgain = false;
			} catch (InterruptedException e) {
			}
		}
	}
	
	

}
