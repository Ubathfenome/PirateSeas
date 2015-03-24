package com.pirateseas.view.graphics.openglview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility")
public class GLSView extends GLSurfaceView {

	private final GLRenderer mRenderer;

	private static final String TAG = "com.pirateseas.view.graphics.SurfaceView";

	public GLSView(Context context) {
		super(context);

		// Create an OpenGL ES 2.0 context.
		setEGLContextClientVersion(2);

		// Set the Renderer for drawing on the GLSurfaceView
		mRenderer = new GLRenderer(context);
		setRenderer(mRenderer);

		// Render the view only when there is a change in the drawing data
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float mPreviousX;
	private float mPreviousY;

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		int pointerCounter = e.getPointerCount(); // Number of pulsations
		
		
		// MotionEvent reports input details from the touch screen
		// and other input controls. In this case, you are only
		// interested in events where the touch position changed.

		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (x < getWidth() / 4 && y < getHeight() / 4) {
					// Reset eyeX & Y
					mRenderer.resetEye();
					Toast.makeText(getContext(), "Resetting default eye location",
							Toast.LENGTH_SHORT).show();
				} else {
			
				}
				break;
			case MotionEvent.ACTION_MOVE:
				// Circular movement of the wheel to the right
				// Circular movement of the wheel to the left
				// Linear movement of the bumper to the front
				// Linear movement of the bumper to the right
				// Linear movement of the bumper to the left
			
				// Free camera movement
				float dx = x - mPreviousX;
				float dy = y - mPreviousY;
			
				// reverse direction of rotation above the mid-line
				if (y > getHeight() / 2) {
					dx = dx * -1;
				}
			
				// reverse direction of rotation to left of the mid-line
				if (x < getWidth() / 2) {
					dy = dy * -1;
				}
			
				// Move eyeX & Y
				Log.v(TAG, "rendererEyeX: " + mRenderer.getEyeX() + " | x: " + x
						+ " | dx: " + dx);
				Log.v(TAG, "rendererEyeY: " + mRenderer.getEyeY() + " | y: " + y
						+ " | dy: " + dy);
				mRenderer.setEye(mRenderer.getEyeX() + (dx * TOUCH_SCALE_FACTOR)
						/ 500, mRenderer.getEyeY(), mRenderer.getEyeZ());
				requestRender();
				break;
			
			case MotionEvent.ACTION_UP:
				Toast.makeText(
						getContext(),
						"x: " + mRenderer.getEyeX() + " | y: "
								+ mRenderer.getEyeY() + " | z: "
								+ mRenderer.getEyeZ(), Toast.LENGTH_SHORT).show();
				break;
		}

		mPreviousX = x;
		mPreviousY = y;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#performClick()
	 */
	@Override
	public boolean performClick() {
		
		// TODO Al hacer Click...
		
		return super.performClick();
	}

}