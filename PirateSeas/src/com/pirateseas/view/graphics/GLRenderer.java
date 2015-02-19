package com.pirateseas.view.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.pirateseas.R;
import com.pirateseas.model.basicfigures.Plane;
import com.pirateseas.controller.utils.programs.ColorShaderProgram;
import com.pirateseas.controller.utils.programs.TextureShaderProgram;
import com.pirateseas.controller.utils.MatrixHelper;
import com.pirateseas.controller.utils.TextureHelper;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 * </ul>
 */
public class GLRenderer implements Renderer {

    private static final String TAG = "MyGLRenderer";
    
    private final Context mActivityContext;
    
	// Object definition area
    private Plane mPlane;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
	private final float[] mViewProjectionMatrix = new float[16];
	private final float[] mModelMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];

    private float mAngle;
	
	private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;

    private int texture;
    
    public GLRenderer(final Context activityContext){
    	this.mActivityContext = activityContext;
		mAngle = 45f;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mPlane = new Plane();
        
        // Load textures
		textureProgram = new TextureShaderProgram(mActivityContext);
        colorProgram = new ColorShaderProgram(mActivityContext);

        texture = TextureHelper.loadTexture(mActivityContext, R.drawable.water_texture);
    }
	
	@Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
		
		float ratio = (float) width / height;
		
		final float eyeX = 0;
        final float eyeY = 1.2f;
        final float eyeZ = 2.2f;
        
        final float lookX = 0f;
        final  float lookY = 0f;
        final float lookZ = 0f;
        
        final float upX = 0f;
        final float upY = 1f;
        final float upZ = 0f;
		
		
        // Adjust the viewport based on geometry changes,
        // such as screen rotation, to fill the entire surface.
        GLES20.glViewport(0, 0, width, height);
		
		// This projection matrix is applied to object coordinates in the onDrawFrame() method
		MatrixHelper.perspectiveM(mProjectionMatrix, mAngle, ratio, 1f, 10f);
		
		// Set the camera position (View matrix)
        setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
		
        // Clear the rendering surface (Reset background color to 'Black').
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		// Multiply the view and projection matrices together.
        multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
		// Draw the water plane
		positionWaterPlane();
		textureProgram.useProgram();
        textureProgram.setUniforms(mMVPMatrix, texture);
        mPlane.bindData(textureProgram);
        mPlane.draw();
    }

	private void positionWaterPlane() {
        // The plane is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        setIdentityM(mModelMatrix, 0);
        rotateM(mModelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
    }
}