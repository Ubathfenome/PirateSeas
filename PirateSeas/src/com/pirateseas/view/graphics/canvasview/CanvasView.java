package com.pirateseas.view.graphics.canvasview;

import java.util.ArrayList;
import java.util.List;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.controller.enemyIA.EnemyIA;
import com.pirateseas.controller.sensors.events.EventDayNightCycle;
import com.pirateseas.controller.sensors.events.EventEnemyTimer;
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
import com.pirateseas.model.canvasmodel.game.scene.Island;
import com.pirateseas.model.canvasmodel.game.scene.Sea;
import com.pirateseas.model.canvasmodel.game.scene.Sky;
import com.pirateseas.model.canvasmodel.game.scene.Sun;
import com.pirateseas.model.canvasmodel.ui.StatBar;
import com.pirateseas.utils.approach2d.GameHelper;
import com.pirateseas.view.activities.GameActivity;
import com.pirateseas.view.activities.PauseActivity;
import com.pirateseas.view.activities.ShopActivity;

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
	private static final float DEGREE_DECREMENT_RATIO = 3.15f;
	private static final double DEGREE_MIN_THRESHOLD = Math.pow(10, -3);

	private static final int HORIZON_Y_VALUE = 170;
	private static final int BAR_INITIAL_X_VALUE = 150;

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
	private Sun nSun;

	private Island nIsland;

	public Ship nPlayerShip;
	private Ship nEnemyShip;
	private List<Shot> nShotList;

	private StatBar nPlayerHBar, nPlayerXPBar;
	private StatBar nEnemyHBar;

	private long nGameTimestamp;

	private boolean nInitialized = false;
	
	private int nCheatCounter;

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
		nSun = new Sun(nContext, nScreenWidth / 2, 0, nScreenWidth,
				nScreenHeight);
		nClouds = new Clouds(nContext, 0, 0, nScreenWidth, nScreenHeight, false);
		nCompass = new Compass(nContext, nScreenWidth / 2,
				HORIZON_Y_VALUE - 45, nScreenWidth, nScreenHeight);
		nSea = new Sea(nContext, 0, HORIZON_Y_VALUE, nScreenWidth,
				nScreenHeight);

		// Entities
		nPlayerShip = new Ship(nContext, ShipType.LIGHT,
				nScreenWidth / 2 - 100, nScreenHeight - HORIZON_Y_VALUE,
				nScreenWidth, nScreenHeight, new Point(0, 0), 2, 3, 5, 0);

		nShotList = new ArrayList<Shot>();

		loadGame();

		// Game User Interface
		nPlayerHBar = new StatBar(nContext, BAR_INITIAL_X_VALUE,
				nScreenHeight - 45, nScreenWidth, nScreenHeight,
				nPlayerShip.getHealth(), nPlayerShip.getType()
						.defaultHealthPoints(), Constants.BAR_HEALTH);
		nPlayerXPBar = new StatBar(nContext, BAR_INITIAL_X_VALUE,
				nScreenHeight - 25, nScreenWidth, nScreenHeight,
				nPlayer.getExperience(), 100, Constants.BAR_EXPERIENCE);

		nGameTimestamp = 0;
		nCheatCounter = 0;

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
		nSun.drawOnScreen(canvas);
		nClouds.drawOnScreen(canvas);
		nCompass.drawOnScreen(canvas);
		nSea.drawOnScreen(canvas);

		nPlayerShip.drawOnScreen(canvas);

		// Prueba con un solo enemigo
		if (nEnemyShip != null) {
			nEnemyShip.drawOnScreen(canvas);
			nEnemyHBar.drawOnScreen(canvas);
		}

		for (int i = 0, size = nShotList.size(); i < size; i++) {
			Shot s = nShotList.get(i);
			s.drawOnScreen(canvas);
		}

		nPlayerHBar.drawOnScreen(canvas);
		nPlayerXPBar.drawOnScreen(canvas);
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
					if (nPlayerShip.isReloaded(nGameTimestamp)) {
						String direction = pressedMotion(
								new Point(downX, downY), new Point(x, y));
						if (direction.equals(Constants.FRONT)) {
							try {
								nShotList.add(nPlayerShip.shootFront());
							} catch (NoAmmoException e) {
								Log.e(TAG, e.getMessage());
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
								Log.e(TAG, e.getMessage());
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
								Log.e(TAG, e.getMessage());
								Toast.makeText(nContext, e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}
						} else if (direction.equals(Constants.BACK)){
							nCheatCounter++;
							if(nCheatCounter == 10){
								grantCheat2Player();
							}
						}
					} else {
						try {
							throw new CannonReloadingException(nContext
									.getResources().getString(
											R.string.exception_reloading));
						} catch (CannonReloadingException e) {
							Log.e(TAG, e.getMessage());
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

	private void grantCheat2Player() {
		nPlayer.addExperience(3500);
		nPlayer.addGold(5000);
		nPlayer.setMapPieces(5);
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
		switch (nStatus) {
		case Constants.GAME_STATE_NORMAL:
			manageTime();
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
		long ts = SystemClock.elapsedRealtime();
		if (nGameTimestamp == 0)
			nGameTimestamp = ts;
		else if (ts - nGameTimestamp >= 1000) {
			nGameTimer.updateHour();
			nGameTimestamp = ts;
		}
	}

	private void manageUI() {
		// Manage StatBars
		if(nEnemyShip != null)
			nEnemyHBar.setCurrentValue(nEnemyShip.getHealth());
		nPlayerHBar.setCurrentValue(nPlayerShip.getHealth());
		nPlayerXPBar.setCurrentValue(nPlayer.getExperience());
		
		// Manage UIDisplayElements
		((GameActivity) nContext).mGold.setElementValue(nPlayer.getGold());
		((GameActivity) nContext).mAmmo.setElementValue(nPlayerShip
				.getAmmunition());
	}

	private void manageEvents() {
		nSky.setFilterValue(EventDayNightCycle.getSkyFilter(nGameTimer
				.getHour()));

		EventEnemyTimer timer = ((GameActivity) nContext).eventEnemy;
		if (timer != null)
			if (timer.challengerApproaches(nEnemyShip)) {
				nEnemyShip = new Ship(nContext, ShipType.HEAVY,
						nScreenWidth / 2 + 150, 70, nScreenWidth,
						nScreenHeight, new Point(15, 20), 3, 4, 7, -1);
				nEnemyHBar = new StatBar(nContext, BAR_INITIAL_X_VALUE, 0,
						nScreenWidth, nScreenHeight, nEnemyShip.getHealth(),
						nEnemyShip.getType().defaultHealthPoints(),
						Constants.BAR_HEALTH);
			}

		if (nEnemyShip == null && nIsland == null) {
			nIsland = new Island(nContext, nScreenWidth - 150, HORIZON_Y_VALUE,
					nScreenWidth, nScreenHeight);
		}

		nSun.move();
		nClouds.move();
	}

	private void manageEntities() {
		managePlayer();
		manageEnemies();
		manageShots();
	}

	private void manageShots() {
		int size = nShotList.size();
		boolean[] deadShots = new boolean[size];
		for (int i = 0; i < size; i++) {
			deadShots[i] = false;
			Shot s = nShotList.get(i);
			if (nEnemyShip != null) {
				if (s.intersectionWithEntity(nEnemyShip)) {
					nEnemyShip.looseHealth(s.getDamage());
					s.looseHealth(s.getDamage());
					
					// Mark shot to destroy object
					deadShots[i] = true;
				}
			}
			if (s.intersectionWithEntity(nPlayerShip)) {
				if(nPlayerShip.getHealth() >= s.getDamage())
					nPlayerShip.looseHealth(s.getDamage());
				s.looseHealth(s.getDamage());
				deadShots[i] = true;
			}
		}
		for (int i = 0; i < size; i++) {
			if (deadShots[i])
				nShotList.remove(i);
		}
	}

	private void manageEnemies() {
		if (nEnemyShip != null) {
			if (!nEnemyShip.isAlive()) {
				nEnemyShip.setStatus(Constants.STATE_DEAD);
				nPlayer.addGold(nEnemyShip.getType().defaultHealthPoints() / 5);
				nPlayer.addExperience(nEnemyShip.getType()
						.defaultHealthPoints() / 2);
				try {
					saveGame();
				} catch (SaveGameException e) {
					Log.e(TAG, e.getMessage());
					Toast.makeText(nContext, e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				EnemyIA eIA = new EnemyIA(nPlayerShip, nEnemyShip);
				nEnemyShip = eIA.getNextMove();
			}
		}
	}

	/**
	 * Rotate the world around the player whenever he/she moves
	 */
	private void managePlayer() {
		if(nPlayerShip.isAlive()){
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
	
			if (nEnemyShip != null)
				nEnemyShip.move(sceneMoveValue);
	
			if (nIsland != null) {
				nIsland.move(sceneMoveValue);
				if (nPlayerShip.arriveIsland(nIsland)){
					try {
						saveGame();
					} catch (SaveGameException e) {
						Log.e(TAG, e.getMessage());
						Toast.makeText(nContext, e.getMessage(), Toast.LENGTH_SHORT).show();
					}
					
					// Display "Shop" Screen
					((GameActivity) nContext).shop(nIsland);
				}
			}
		} else {
			// Display "Game Over" Screen with calculated score
			((GameActivity) nContext).gameOver(nPlayer);
			nStatus = Constants.GAME_STATE_END;
		}
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

	public void destroyIsland() {
		nIsland = null;
	}

}
