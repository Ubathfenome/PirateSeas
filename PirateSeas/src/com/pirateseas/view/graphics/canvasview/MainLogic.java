package com.pirateseas.view.graphics.canvasview;

import com.pirateseas.global.Constants;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Main thread class
 * 
 * @author Miguel
 *
 */
public class MainLogic extends Thread {
	private final static int MAX_JUMPED_FRAMES = 5;
	private final static int FRAME_TIME = 1000 / Constants.GAME_FPS ;
	private final static String TAG = "MainLogic";

	private SurfaceHolder surface;
	private CanvasView mCanvasView;
	
	private Canvas canvas;

	private static boolean running;
	private static boolean initialized;

	/**
	 * Constructor
	 * 
	 * @param surface
	 * @param mainView
	 */
	public MainLogic(SurfaceHolder surface, CanvasView mainView) {
		super();
		this.surface = surface;
		this.setCanvasViewInstance(mainView);
	}

	public void setRunning(boolean run) {
		MainLogic.running = run;
	}

	public boolean getRunning() {
		return running;
	}

	@Override
	public void run() {

		if(!(initialized = this.getCanvasViewInstance().isInitialized())){
			this.getCanvasViewInstance().initialize();
			initialized = this.getCanvasViewInstance().isInitialized();
			Log.d(TAG, "Starting logic thread");
		}

		long initTime; // Loop initial time
		long diffTime; // Loop duration time
		int waitTime; // Time between executions of the loop
		int jumpedFrames; // Number of jumped frames

		waitTime = 0;
		canvas = null;

		while (running && initialized) {
			canvas = null;
			try {
				canvas = this.surface.lockCanvas();
				if (canvas != null){
					canvas.getClipBounds();
					synchronized (surface) {
						initTime = System.currentTimeMillis();
						jumpedFrames = 0;
	
						this.getCanvasViewInstance().updateLogic();
						this.getCanvasViewInstance().drawOnScreen(canvas);
	
						diffTime = System.currentTimeMillis() - initTime;
						waitTime = (int) (FRAME_TIME - diffTime);
	
						if (waitTime > 0) {
							try { // Save battery
								Thread.sleep(waitTime);
							} catch (InterruptedException e) {
							}
						}
	
						while (waitTime < 0 && jumpedFrames < MAX_JUMPED_FRAMES) {
							this.getCanvasViewInstance().updateLogic();
							waitTime += FRAME_TIME;
							jumpedFrames++;
						}
					}
				} else {
					this.getCanvasViewInstance().updateLogic();
					setRunning(false);
				}
			} catch (ArithmeticException arex){
				Log.e(TAG, arex.getMessage());
			} catch (Exception ex) {
				if(canvas == null){
					Log.e(TAG, "Canvas is not created or cannot be edited");
				} else 
					Log.e(TAG, "Canvas not available");
			} finally {
				if (canvas != null) {
					surface.unlockCanvasAndPost(canvas);
				}
			}
		}
		Log.d(TAG, "Stopping logic thread. De-initializing...");
		// mCanvasView instance gets destroyed
		if(getCanvasViewInstance().isInitialized())
			initialized = false;	
	}

	public CanvasView getCanvasViewInstance() {
		return mCanvasView;
	}

	public void setCanvasViewInstance(CanvasView mCanvasView) {
		this.mCanvasView = mCanvasView;
	}
}
