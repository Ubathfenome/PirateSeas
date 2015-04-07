package com.pirateseas.view.graphics.canvasview;

import java.util.ArrayList;
import java.util.List;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.controller.sensors.events.EventDayNightCycle;
import com.pirateseas.controller.timer.GameTimer;
import com.pirateseas.exceptions.CannonReloadingException;
import com.pirateseas.exceptions.NoAmmoException;
import com.pirateseas.exceptions.SaveGameException;
import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.entity.Ship;
import com.pirateseas.model.canvasmodel.game.entity.ShipType;
import com.pirateseas.model.canvasmodel.game.entity.Shot;
import com.pirateseas.model.canvasmodel.game.scene.Clouds;
import com.pirateseas.model.canvasmodel.game.scene.Compass;
import com.pirateseas.model.canvasmodel.game.scene.Sea;
import com.pirateseas.model.canvasmodel.game.scene.Sky;
import com.pirateseas.utils.approach2d.GameHelper;
import com.pirateseas.view.activities.GameActivity;
import com.pirateseas.view.activities.PauseActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "CanvasView";
	private static final float DEGREE_DECREMENT_RATIO = 2.15f;
	private static final double DEGREE_MIN_THRESHOLD = Math.pow(10, -3);

	private Context nContext;
	public static MainLogic nUpdateThread;

	private int nScreenWidth;
	private int nScreenHeight;
	public static int nStatus;

	private GameTimer nGameTimer;
	public Player nPlayer;

	private Sky nSky;
	private Compass nCompass;
	private Sea nSea;
	public Clouds nClouds;
	// private Sun nSun;

	public Ship nPlayerShip;
	private Ship nEnemyShip;
	private List<Shot> nShotList;

	private long nGameTimestamp;

	private boolean nInitialized = false;

	int downX = 0, downY = 0;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public CanvasView(Context context) {
		this(context, null);
	}

	public CanvasView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CanvasView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getHolder().addCallback(this);
		setFocusable(true);
		this.nContext = context;
		launchMainLogic();
	}

	/**
	 * Launches the main thread
	 */
	public void launchMainLogic() {
		nUpdateThread = null;
		nUpdateThread = new MainLogic(getHolder(), this);
	}

	public void initialize() {
		nStatus = Constants.GAME_STATE_NORMAL;

		nGameTimer = new GameTimer();
		nPlayer = new Player();

		// Initialize components
		// Scene
		nSky = new Sky(nContext, 0, 0, nScreenWidth, nScreenHeight);
		nClouds = new Clouds(nContext, 0, 0, nScreenWidth, nScreenHeight, false);
		nCompass = new Compass(nContext, nScreenWidth / 2, 125, nScreenWidth,
				nScreenHeight);
		nSea = new Sea(nContext, 0, 170, nScreenWidth, nScreenHeight);

		// Entities
		nPlayerShip = new Ship(nContext, ShipType.LIGHT,
				nScreenWidth / 2 - 100, nScreenHeight - 170, nScreenWidth,
				nScreenHeight, new Point(0, 0), 2, 3, 5, 0);

		// nEnemyShip = new Ship(nContext, ShipType.HEAVY, nScreenWidth / 2 +
		// 150, 170, nScreenWidth, nScreenHeight, new Point(15, 20), 3, 4, 7,
		// -1);

		nShotList = new ArrayList<Shot>();

		loadGame();

		nGameTimestamp = 0;

		nInitialized = true;
	}

	public boolean isInitialized() {
		return nInitialized;
	}

	public void loadGame() {
		GameHelper.loadGame(nContext, nPlayer, nPlayerShip);
		nPlayer = GameHelper.helperPlayer;
		nPlayerShip = GameHelper.helperShip;
	}

	public void saveGame() throws SaveGameException {
		if (GameHelper.saveGame(nContext, nPlayer, nPlayerShip))
			Log.v(TAG, "Game saved");
		else
			throw new SaveGameException(nContext.getResources().getString(
					R.string.exception_save));
	}

	/**
	 * Draws all objects on the screen
	 * 
	 * @param canvas
	 */
	protected void drawOnScreen(Canvas canvas) {
		nSky.drawOnScreen(canvas);
		nCompass.drawOnScreen(canvas);
		nSea.drawOnScreen(canvas);

		nPlayerShip.drawOnScreen(canvas);

		// Prueba con un solo enemigo
		// nEnemyShip.drawOnScreen(canvas);

		for (int i = 0, size = nShotList.size(); i < size; i++) {
			Shot s = nShotList.get(i);
			s.drawOnScreen(canvas);
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// int pointerCounter = event.getPointerCount(); // Number of pulsations

		int x = (int) event.getX();
		int y = (int) event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downX = x;
			downY = y;
		}

		if (nStatus == Constants.GAME_STATE_NORMAL) {
			if ((x > (getWidth() - (getWidth() / 6))) && (y < getHeight() / 6)) {
				nStatus = Constants.GAME_STATE_PAUSE;
				Intent pauseActivityIntent = new Intent(nContext,
						PauseActivity.class);
				nContext.startActivity(pauseActivityIntent);
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					if (nPlayerShip.isReloaded(SystemClock.uptimeMillis())) {
						String direction = pressedMotion(
								new Point(downX, downY), new Point(x, y));
						if (direction.equals(Constants.FRONT)) {
							try {
								nShotList.add(nPlayerShip.shootFront());
							} catch (NoAmmoException e) {
								Toast.makeText(nContext, e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}

						} else if (direction.equals(Constants.RIGHT)) {
							Shot[] shotArray = null;

							try {
								shotArray = nPlayerShip.shootSide(true);
								for (int i = 0, length = shotArray.length; i < length; i++) {
									nShotList.add(shotArray[i]);
								}
							} catch (NoAmmoException e) {
								Toast.makeText(nContext, e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}
						} else if (direction.equals(Constants.LEFT)) {
							Shot[] shotArray = null;

							try {
								shotArray = nPlayerShip.shootSide(false);
								for (int i = 0, length = shotArray.length; i < length; i++) {
									nShotList.add(shotArray[i]);
								}
							} catch (NoAmmoException e) {
								Toast.makeText(nContext, e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}
						}
					} else {
						try {
							throw new CannonReloadingException(nContext
									.getResources().getString(
											R.string.exception_reloading));
						} catch (CannonReloadingException e) {
							Toast.makeText(nContext, e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}
					break;
				}
			}
		}
		return true;
	}

	private String pressedMotion(Point start, Point end) {
		int deltaX = end.x - start.x;
		int deltaY = end.y - start.y;

		if (Math.abs(deltaX) > Math.abs(deltaY)) { // Lateral movement
			return deltaX > 0 ? Constants.RIGHT : Constants.LEFT;
		} else { // Vertical movement
			return deltaY > 0 ? Constants.BACK : Constants.FRONT;
		}
	}

	public static void pauseGame(boolean hasToBePaused) {
		if (hasToBePaused && nStatus == Constants.GAME_STATE_NORMAL) {
			nStatus = Constants.GAME_STATE_PAUSE;
		} else if (!hasToBePaused && nStatus == Constants.GAME_STATE_PAUSE) {
			nStatus = Constants.GAME_STATE_NORMAL;
		}
	}

	/**
	 * Updates the view mStatus
	 */
	public synchronized void updateLogic() {
		checkLogicThread();
		manageTime();
		switch (nStatus) {
		case Constants.GAME_STATE_NORMAL:
			manageEvents();
			manageEntities();
			manageUI();
			break;
		case Constants.GAME_STATE_PAUSE:
			break;
		case Constants.GAME_STATE_END:
			nUpdateThread.setRunning(false);
			((GameActivity) nContext).finish();
			break;
		}
	}

	private void checkLogicThread() {
		if (!nUpdateThread.isAlive()
				&& nUpdateThread.getState() != Thread.State.NEW) {
			launchMainLogic();
		}
	}

	private void manageTime() {
		long ts = SystemClock.uptimeMillis();
		if (nGameTimestamp == 0)
			nGameTimestamp = ts;
		else if (ts - nGameTimestamp >= 1000) {
			nGameTimer.updateHour();
			nGameTimestamp = ts;
		}
	}

	private void manageUI() {
		// TODO Manage StatBars
		// Manage UIDisplayElements
		((GameActivity) nContext).mGold.setElementValue(nPlayer.getGold());
		((GameActivity) nContext).mAmmo.setElementValue(nPlayerShip
				.getAmmunition());
	}

	private void manageEvents() {
		nSky.setFilterValue(EventDayNightCycle.getSkyFilter(nGameTimer
				.getHour()));
		// nSun.move();
		nClouds.move();
	}

	private void manageEntities() {
		managePlayer();
		manageEnemies();
		// manageShots();
	}

	private void manageShots() {
		int size = nShotList.size();
		boolean[] deadShots = new boolean[size];
		for (int i = 0; i < size; i++) {
			deadShots[i] = false;
			Shot s = nShotList.get(i);
			if (s.intersectionWithEntity(nEnemyShip)) {
				nEnemyShip.looseHealth(s.getDamage());
				// Mark shot to destroy object
				deadShots[i] = true;
			}
		}
		for (int i = 0; i < size; i++) {
			if (deadShots[i])
				nShotList.remove(i);
		}
	}

	private void manageEnemies() {
		// TODO
		/*
		 * if(!enemyShip.isAlive()) enemyShip.setStatus(Constants.STATE_DEAD);
		 * else{ EnemyIA eIA = new EnemyIA(playerShip, enemyShip); enemyShip =
		 * eIA.getNextMove(); }
		 */

	}

	/**
	 * Rotate the world around the player whenever he/she moves
	 */
	private void managePlayer() {
		double degrees = ((GameActivity) nContext).ctrlWheel.getDegrees();
		double sceneMoveValue = this.getHeight() * Math.tan(degrees);

		if (degrees == 0) {
			degrees = degrees > 90 && degrees < 270 ? -sceneMoveValue
					: sceneMoveValue;
		} else {
			if (degrees < DEGREE_MIN_THRESHOLD)
				degrees = 0;
			else
				degrees /= DEGREE_DECREMENT_RATIO;
			((GameActivity) nContext).ctrlWheel.setDegrees(degrees);
		}

		nSky.move(sceneMoveValue);
		nCompass.move(sceneMoveValue);
		nSea.move(sceneMoveValue);
		
		//nEnemyShip.move(sceneMoveValue);

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		String logTag = TAG + ": surfaceChanged";
		Log.d(logTag, "Ajuste de medidas: [" + width + ", " + height + "]");

		nScreenWidth = width;
		nScreenHeight = height;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		String logTag = TAG + ": surfaceCreated";
		Log.d(logTag, "Superficie creada");

		nUpdateThread.setRunning(true);
		nUpdateThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		String logTag = TAG + ": surfaceDestroyed";
		Log.d(logTag, "Superficie destruida");

		boolean tryAgain = true;
		while (tryAgain) {
			try {
				nUpdateThread.join();

				tryAgain = false;
			} catch (InterruptedException e) {
			}
		}
	}

}
