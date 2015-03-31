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

	private static boolean running;

	/**
	 * Constructor
	 * 
	 * @param surface
	 * @param mainView
	 */
	public MainLogic(SurfaceHolder surface, CanvasView mainView) {
		super();
		this.surface = surface;
		this.mCanvasView = mainView;
	}

	public void setRunning(boolean run) {
		MainLogic.running = run;
	}

	public boolean getRunning() {
		return running;
	}

	@Override
	public void run() {
		Canvas canvas;

		if(!this.mCanvasView.isInitialized()){
			this.mCanvasView.initialize();
		}

		long initTime; // Loop initial time
		long diffTime; // Loop duration time
		int waitTime; // Time between executions of the loop
		int jumpedFrames; // Number of jumped frames

		waitTime = 0;

		while (running) {
			canvas = null;
			try {
				canvas = this.surface.lockCanvas();
				canvas.getClipBounds();
				synchronized (surface) {
					initTime = System.currentTimeMillis();
					jumpedFrames = 0;

					this.mCanvasView.updateLogic();
					this.mCanvasView.drawOnScreen(canvas);

					diffTime = System.currentTimeMillis() - initTime;
					waitTime = (int) (FRAME_TIME - diffTime);

					if (waitTime > 0) {
						try { // Save battery
							Thread.sleep(waitTime);
						} catch (InterruptedException e) {
						}
					}

					while (waitTime < 0 && jumpedFrames < MAX_JUMPED_FRAMES) {
						this.mCanvasView.updateLogic();
						waitTime += FRAME_TIME;
						jumpedFrames++;
					}
				}
			} catch (Exception ex) {
				Log.e(TAG, "" + ex.getMessage());
			} finally {
				if (canvas != null) {
					surface.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
