package com.pirateseas.view.graphics;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;

import android.content.Context;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.pirateseas.R;
import com.pirateseas.model.basicfigures.Plane;
//import com.pirateseas.model.scene.Sea;
import com.pirateseas.utils.programs.ColorShaderProgram;
import com.pirateseas.utils.programs.TextureShaderProgram;
import com.pirateseas.utils.MatrixHelper;
import com.pirateseas.utils.TextureHelper;

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

    private static final String TAG = "GLRenderer";
    
    private final Context mActivityContext;
    
    private float eyeX = 0;
    private float eyeY = 1.2f;
    private float eyeZ = 4.2f;
    
    private final float lookX = 0f;
    private final float lookY = 0f;
    private final float lookZ = 0f;
    
    private final float upX = 0f;
    private final float upY = 1f;
    private final float upZ = 0f;
    
	// Object definition area
	// private Sea sea;
    private Plane mWaterPlane;
    private Plane mSkyPlane;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
	private final float[] mViewProjectionMatrix = new float[16];
	private final float[] mModelMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
	
	private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;

    private int waterTexture;
    private int skyTexture;
    
    public GLRenderer(final Context activityContext){
    	this.mActivityContext = activityContext;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		//sea = new Sea();
        mWaterPlane = new Plane();
        mSkyPlane = new Plane();
        
        // Load textures
		textureProgram = new TextureShaderProgram(mActivityContext);
        colorProgram = new ColorShaderProgram(mActivityContext);

        waterTexture = TextureHelper.loadTexture(mActivityContext, R.drawable.blue_water_texture);
        skyTexture = TextureHelper.loadTexture(mActivityContext, R.drawable.horizon_sky_texture);
    }
	
	@Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
		
		float ratio = (float) width / height;	
		
        // Adjust the viewport based on geometry changes,
        // such as screen rotation, to fill the entire surface.
        GLES20.glViewport(0, 0, width, height);
		
		// This projection matrix is applied to object coordinates in the onDrawFrame() method
		MatrixHelper.perspectiveM(mProjectionMatrix, 45f, ratio, 1f, 10f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
		
        // Clear the rendering surface (Reset background color to 'Black').
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        // Set the camera position (View matrix)
        setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
		
		// Multiply the view and projection matrices together.
        multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
		// Draw the water plane
		positionWaterPlane(eyeZ * 10);
		textureProgram.useProgram();
        textureProgram.setUniforms(mMVPMatrix, waterTexture);
		//sea.bindData(textureProgram);
		//sea.draw();
        mWaterPlane.bindData(textureProgram);
        mWaterPlane.draw();
        
        // Draw the sky plane
     	positionSkyPlane();
     	textureProgram.useProgram();
        textureProgram.setUniforms(mMVPMatrix, skyTexture);
        mSkyPlane.bindData(textureProgram);
        mSkyPlane.draw();
    }

	private void positionWaterPlane(float scaleFactor) {
        // The plane is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        setIdentityM(mModelMatrix, 0);
        scaleM(mModelMatrix, 0, scaleFactor, 1.0f, scaleFactor);
		rotateM(mModelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
    }
	
	private void positionSkyPlane() {
        // The plane is defined in terms of X & Y coordinates, so it lie flat on the XY plane.
        setIdentityM(mModelMatrix, 0);
        scaleM(mModelMatrix, 0, 1f, 1f, 1f);
		// Positive Z-axis translation goes OUT of the screen (Negative goes INTO the screen)
        translateM(mModelMatrix, 0, 0, 0, -2f);
        multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
    }

	public void resetEye() {
		eyeX = 0;
	    eyeY = 1.2f;		
		eyeZ = 4.2f;
	}
	
	public float getEyeX(){
		return eyeX;
	}
	
	public float getEyeY(){
		return eyeY;
	}
	
	public float getEyeZ(){
		return eyeZ;
	}

	public void setEye(float x, float y, float z) {
		eyeX = x;
		eyeY = y;
		eyeZ = z;
	}
}