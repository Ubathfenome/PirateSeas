package com.pirateseas.view.graphics.canvasview;

import java.util.ArrayList;
import java.util.List;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.controller.audio.MusicManager;
import com.pirateseas.controller.enemyIA.EnemyIA;
import com.pirateseas.controller.enemyIA.IAStatus;
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
import com.pirateseas.model.canvasmodel.ui.StatBar;
import com.pirateseas.utils.persistence.GameHelper;
import com.pirateseas.view.activities.GameActivity;
import com.pirateseas.view.activities.PauseActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "CanvasView";
	private static final String EXCEPTION_TAG = "CustomException";
	private static final float DEGREE_DECREMENT_RATIO = 2.65f;
	private static final double DEGREE_MIN_THRESHOLD = 0.2f;
	private static final float FORWARD_BASE_VALUE = 1.05f;
	private static final int ISLAND_SPAWN_SECONDS_TO_CHECK = 30;

	private static final int CHT_VALUE = 20;
	
	private static final int SHOT_CHK_DELAY = 75;
	
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

	private Island nIsland;

	public Ship nPlayerShip;
	private Ship nEnemyShip;
	private List<Shot> nShotList;

	private StatBar nPlayerHBar, nPlayerXPBar;
	private StatBar nEnemyHBar;

	private SharedPreferences nPreferences;
	private long nBaseTimestamp;
	private long nGameTimestamp;
	private long nLastIslandTimestamp;
	private long[] nShotLastTimeChecked;

	private boolean nInitialized = false;
	private boolean nDebug = false;
	
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
		if(nUpdateThread != null){
			nUpdateThread.setRunning(false);
			nUpdateThread.interrupt();
		}
		nUpdateThread = null;
		nUpdateThread = new MainLogic(getHolder(), this);
	}

	public void initialize() {
		nStatus = Constants.GAME_STATE_NORMAL;

		nPreferences = nContext.getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE);
		nBaseTimestamp = nPreferences.getLong(Constants.PREF_PLAYER_TIMESTAMP, 0);
		nDebug = nPreferences.getBoolean(Constants.TAG_EXE_MODE, false);
		
		nGameTimer = new GameTimer(nBaseTimestamp);
		nPlayer = new Player();

		// Initialize components
		// Scene
		nSky = new Sky(nContext, 0, 0, nScreenWidth, nScreenHeight);
		nClouds = new Clouds(nContext, 0, 0, nScreenWidth, nScreenHeight, false);
		nClouds.heightReposition(HORIZON_Y_VALUE - 55);
		nCompass = new Compass(nContext, nScreenWidth / 2,
				HORIZON_Y_VALUE - 45, nScreenWidth, nScreenHeight);
		nSea = new Sea(nContext, 0, HORIZON_Y_VALUE, nScreenWidth,
				nScreenHeight);

		// Entities
		nPlayerShip = new Ship(nContext, ShipType.LIGHT,
				nScreenWidth / 2 - 100, nScreenHeight - HORIZON_Y_VALUE,
				nScreenWidth, nScreenHeight, new Point(0, 0), 90, 2, 3, 5, 20);

		nShotList = new ArrayList<Shot>();

		if(((GameActivity)nContext).hasToLoadGame())
			loadGame();

		// Game User Interface
		nPlayerHBar = new StatBar(nContext, BAR_INITIAL_X_VALUE,
				nScreenHeight - 45, nScreenWidth, nScreenHeight,
				nPlayerShip.getHealth(), nPlayerShip.getType()
						.defaultHealthPoints(), Constants.BAR_HEALTH);
		nPlayerXPBar = new StatBar(nContext, BAR_INITIAL_X_VALUE,
				nScreenHeight - 25, nScreenWidth, nScreenHeight,
				(int) nPlayer.getNextLevelThreshold(), 0, Constants.BAR_EXPERIENCE);

		nGameTimestamp = 0;
		nLastIslandTimestamp = 0;
		nShotLastTimeChecked = null;
		nCheatCounter = 0;

		nInitialized = true;
	}

	public boolean isInitialized() {
		return nInitialized;
	}

	public void loadGame() {
		GameHelper.loadGameAtPreferences(nContext, nPlayer, nPlayerShip);
		nPlayer = GameHelper.helperPlayer;
		nPlayerShip = GameHelper.helperShip;
	}

	public void saveGame() throws SaveGameException {
		if (GameHelper.saveGameAtPreferences(nContext, nPlayer, nPlayerShip))
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
			if(s.isAlive() && s.isInBounds(HORIZON_Y_VALUE))
				s.drawOnScreen(canvas);
		}

		nPlayerHBar.drawOnScreen(canvas);
		nPlayerXPBar.drawOnScreen(canvas);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
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
					boolean reloaded = nPlayerShip.isReloaded(nGameTimestamp);
					if (reloaded) {
						String direction = pressedMotion(
								new Point(downX, downY), new Point(x, y));
						if (direction.equals(Constants.FRONT)) {
							
							nCheatCounter = nCheatCounter != 0 ? 0 : nCheatCounter;
							
							try {
								nShotList.add(nPlayerShip.shootFront());
							} catch (NoAmmoException e) {
								Log.e(EXCEPTION_TAG, e.getMessage());
								Toast.makeText(nContext, e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}

						} else if (direction.equals(Constants.RIGHT)) {
							Shot[] shotArray = null;
							nCheatCounter = nCheatCounter != 0 ? 0 : nCheatCounter;

							try {
								shotArray = nPlayerShip.shootSide(true);
								for (int i = 0, length = shotArray.length; i < length; i++) {
									nShotList.add(shotArray[i]);
								}
							} catch (NoAmmoException e) {
								Log.e(EXCEPTION_TAG, e.getMessage());
								Toast.makeText(nContext, e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}
						} else if (direction.equals(Constants.LEFT)) {
							Shot[] shotArray = null;
							nCheatCounter = nCheatCounter != 0 ? 0 : nCheatCounter;

							try {
								shotArray = nPlayerShip.shootSide(false);
								for (int i = 0, length = shotArray.length; i < length; i++) {
									nShotList.add(shotArray[i]);
								}
							} catch (NoAmmoException e) {
								Log.e(EXCEPTION_TAG, e.getMessage());
								Toast.makeText(nContext, e.getMessage(),
										Toast.LENGTH_SHORT).show();
							}
						} else if (direction.equals(Constants.BACK)){
							nCheatCounter++;
							if(nCheatCounter % CHT_VALUE > 0)
								Log.v("Cheat", CHT_VALUE - (nCheatCounter % CHT_VALUE) + " more touches to go!");
							if(nCheatCounter % CHT_VALUE == 0)
								grantCheat2Player();
						}
					} else {
						try {
							throw new CannonReloadingException(nContext
									.getResources().getString(
											R.string.exception_reloading));
						} catch (CannonReloadingException e) {
							Log.e(EXCEPTION_TAG, e.getMessage());
							if(!nDebug)
								MusicManager.getInstance().playSound(MusicManager.SOUND_SHOT_RELOADING);
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
		nPlayer.addExperience(200);
		nPlayer.addGold(50);
		nPlayer.setMapPieces(1);
		nPlayerShip.gainAmmo(10);
		nPlayerShip.gainHealth(30);
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
	 * Updates the view depending on mStatus value
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
			nUpdateThread.interrupt();
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
		nGameTimestamp = nGameTimer.getLastTimestamp();
		
		nGameTimer.updateHour();
		
		long baseTs2Save = nGameTimer.getBaseTimestamp();
		if(baseTs2Save != 0 && nBaseTimestamp == 0){
			SharedPreferences.Editor editor = nPreferences.edit();
			editor.putLong(Constants.PREF_PLAYER_TIMESTAMP, baseTs2Save);
			editor.commit();
		}
		int passedDays = nGameTimer.getDay();
		if(passedDays > 0){
			nPlayer.setPassedDays(passedDays);
		}
	}

	private void manageUI() {
		// Manage StatBars
		if(nEnemyShip != null)
			nEnemyHBar.setCurrentValue(nEnemyShip.getHealth());
		nPlayerHBar.setCurrentValue(nPlayerShip.getHealth());
		nPlayerXPBar.setCurrentValue(nPlayer.getExperience());
		nPlayerXPBar.setMaxValue(nPlayer.getNextLevelThreshold());
		
		// Manage UIDisplayElements
		((GameActivity) nContext).mGold.setElementValue(nPlayer.getGold());
		((GameActivity) nContext).mGold.postInvalidate();
		
		((GameActivity) nContext).mAmmo.setElementValue(nPlayerShip
				.getAmmunition());
		
		if(nPlayerShip.isReloaded(nGameTimestamp))
			((GameActivity) nContext).mAmmo.setReloading(false);
		else
			((GameActivity) nContext).mAmmo.setReloading(true);
		
		((GameActivity) nContext).mAmmo.postInvalidate();
	}

	private void manageEvents() {
		nSky.setFilterValue(EventDayNightCycle.getSkyFilter(nGameTimer
				.getHour()));
		
		nSea.setFilterValue(EventDayNightCycle.getSkyFilter(nGameTimer
				.getHour()));

		EventEnemyTimer timer = ((GameActivity) nContext).eventEnemy;
		if (timer != null)
			if (timer.challengerApproaches(nEnemyShip)) {
				nEnemyShip = new Ship(nContext, ShipType.HEAVY,
						nScreenWidth / 2 + 150, 70, nScreenWidth,
						nScreenHeight, new Point(15, 20), 270, 3, 4, 7, Constants.SHOT_AMMO_UNLIMITED);
				nEnemyHBar = new StatBar(nContext, BAR_INITIAL_X_VALUE, 0,
						nScreenWidth, nScreenHeight, nEnemyShip.getHealth(),
						nEnemyShip.getType().defaultHealthPoints(),
						Constants.BAR_HEALTH);
			}

		if (nEnemyShip == null && 
				nIsland == null && 
				nLastIslandTimestamp != 0 && 
				(nGameTimestamp - nLastIslandTimestamp) >= ISLAND_SPAWN_SECONDS_TO_CHECK * Constants.MILLIS_TO_SECONDS) {
			nIsland = new Island(nContext, nScreenWidth - 150, HORIZON_Y_VALUE,
					nScreenWidth, nScreenHeight);
			nLastIslandTimestamp = nGameTimestamp;
			Log.v(TAG, "Island detected!");
			Toast.makeText(nContext, "Capt'n! Land in sight!", Toast.LENGTH_SHORT).show();
		}

		nClouds.move();
	}

	private void manageEntities() {
		managePlayer();
		manageEnemies();
		manageShots();
	}

	private void manageShots() {
		int size = nShotList.size();
		
		if(size > 0){
			boolean[] deadShots = new boolean[size];
			nShotLastTimeChecked = new long[size];
			for (int i = 0; i < size; i++) {
				deadShots[i] = false;
				Shot s = nShotList.get(i);
				if(s.isAlive() && s.isInBounds(HORIZON_Y_VALUE)){
					switch(s.getShotStatus()){
						case Constants.SHOT_FIRED:
							if (nGameTimestamp - s.getTimestamp() >= SHOT_CHK_DELAY){
								if(!nDebug)
									MusicManager.getInstance().playSound(MusicManager.SOUND_SHOT_FIRED);
								s.setShotStatus(Constants.SHOT_FLYING);
								nShotLastTimeChecked[i] = nGameTimestamp;
							}
							break;
						case Constants.SHOT_FLYING:
							if (nGameTimestamp - nShotLastTimeChecked[i] >= SHOT_CHK_DELAY){
								if(!nDebug)
									MusicManager.getInstance().playSound(MusicManager.SOUND_SHOT_FLYING);
								if (nEnemyShip != null) {
									if (s.intersectionWithEntity(nEnemyShip)) {
										nEnemyShip.looseHealth(s.getDamage());
										s.setShotStatus(Constants.SHOT_HIT);
									}
								}
								if (s.intersectionWithEntity(nPlayerShip)) {
									if(nPlayerShip.getHealth() >= s.getDamage())
										nPlayerShip.looseHealth(s.getDamage());
									s.setShotStatus(Constants.SHOT_HIT);
								}
								
								s.moveEntity(s.getEndPoint());
								nShotLastTimeChecked[i] = nGameTimestamp;
								
								if (s.getCoordinates().x == s.getEndPoint().x && s.getCoordinates().y == s.getEndPoint().y)
									s.setShotStatus(Constants.SHOT_MISSED);
							}
							break;
						case Constants.SHOT_HIT:
							// Play hit sound
							if (nGameTimestamp - nShotLastTimeChecked[i] >= SHOT_CHK_DELAY){
								if(!nDebug)
									MusicManager.getInstance().playSound(MusicManager.SOUND_SHOT_HIT);
								s.looseHealth(s.getDamage());
								deadShots[i] = true;
							}
							break;
						case Constants.SHOT_MISSED:
							// Play missed sound
							if (nGameTimestamp - nShotLastTimeChecked[i] >= SHOT_CHK_DELAY){
								if(!nDebug)
									MusicManager.getInstance().playSound(MusicManager.SOUND_SHOT_MISSED);
								s.looseHealth(s.getDamage());
								deadShots[i] = true;
							}
							break;
					}			
				}
			}
			
			for (int i = size - 1; i >= 0; i--) {
				if (deadShots[i]){
					nShotList.remove(i);
					nShotLastTimeChecked[i] = 0;
				}
			}
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
					Log.e(EXCEPTION_TAG, e.getMessage());
					Toast.makeText(nContext, e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				EnemyIA eIA = new EnemyIA(nPlayerShip, nEnemyShip);
				nEnemyShip = eIA.getNextMove();
				
				try {
					if(eIA.getStatus() == IAStatus.ATTACKF)  
						nShotList.add(nEnemyShip.shootFront());
					if(eIA.getStatus() == IAStatus.ATTACKSR){
						Shot[] shotArray = nEnemyShip.shootSide(true);
						for(Shot s : shotArray){
							nShotList.add(s);
						}
					}
					if(eIA.getStatus() == IAStatus.ATTACKSL){
						Shot[] shotArray = nEnemyShip.shootSide(false);
						for(Shot s : shotArray){
							nShotList.add(s);
						}
					}
				} catch (NoAmmoException e) {
					Log.e(EXCEPTION_TAG, e.getMessage());
				}
			}
		}
	}

	/**
	 * Rotate the world around the player whenever he/she moves
	 */
	private void managePlayer() {
		if(nPlayerShip.isAlive()){
			double arcPixels = ((GameActivity) nContext).ctrlWheel.getMovedPixels();
			float degrees = ((GameActivity) nContext).ctrlWheel.getDegrees();
	
			if(!((GameActivity) nContext).ctrlWheel.isBeingTouched()){
				if (degrees >= DEGREE_MIN_THRESHOLD) {
					degrees /= DEGREE_DECREMENT_RATIO;
					((GameActivity) nContext).ctrlWheel.setDegrees(degrees);
					((GameActivity) nContext).doWheelRotation();
					((GameActivity) nContext).ctrlWheel.postInvalidate();
				} else {
					if(degrees != 0){
						((GameActivity) nContext).resetUiWheel();
						((GameActivity) nContext).ctrlWheel.postInvalidate();
					}
				}
			}
			
			int verticalSpeed = (int) (FORWARD_BASE_VALUE * ((GameActivity) nContext).ctrlThrottle.getLevelSpeed());
	
			if(arcPixels != 0)
				Log.v(TAG, "Moving scene " + (-arcPixels) + " pixels");
			nSky.move(-arcPixels, -verticalSpeed);
			nCompass.move(-arcPixels, 0);
			nSea.move(-arcPixels, verticalSpeed);
			
			nPlayerShip.setEntityDirection((int) nCompass.getValue());
	
			if (nEnemyShip != null)
				nEnemyShip.move(-arcPixels, verticalSpeed);
	
			if (nIsland != null) {
				nIsland.move(-arcPixels, verticalSpeed);
				if (nPlayerShip.arriveIsland(nIsland)){
					try {
						saveGame();
					} catch (SaveGameException e) {
						Log.e(EXCEPTION_TAG, e.getMessage());
						Toast.makeText(nContext, e.getMessage(), Toast.LENGTH_SHORT).show();
					}
					
					// Display "Shop" Screen
					((GameActivity) nContext).shop(nIsland);
					nIsland = null;
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
		nScreenWidth = width;
		nScreenHeight = height;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Surface Created");
		
		if(!nUpdateThread.isAlive()){
			launchMainLogic();
			
			nUpdateThread.setRunning(true);
			nUpdateThread.start();
		} else {
			nUpdateThread.setRunning(true);
		}
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
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
	
	public void setStatus (int status){
		nStatus = status;
	}

}
