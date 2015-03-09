package com.pirateseas.view.graphics;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.pirateseas.R;
import com.pirateseas.model.openglmodel.entity.Ship;
import com.pirateseas.model.openglmodel.entity.ShipType;
import com.pirateseas.model.openglmodel.scene.Sea;
import com.pirateseas.model.openglmodel.scene.Sky;
import com.pirateseas.model.openglmodel.scene.Sun;
import com.pirateseas.utils.programs.ColorShaderProgram;
import com.pirateseas.utils.programs.TextureShaderProgram;
import com.pirateseas.utils.programs.LightPointShaderProgram;
import com.pirateseas.utils.Geometry.Point;
import com.pirateseas.utils.Geometry.Vector;
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
	private static final int SECONDS_PER_REVOLUTION = 120;
    
    private final Context mActivityContext;
    
    private float eyeX = 0;
    private float eyeY = 1.2f;
    private float eyeZ = 1.2f;
    
    private final float lookX = 0f;
    private final float lookY = 0f;
    private final float lookZ = 0f;
    
    private final float upX = 0f;
    private final float upY = 1f;
    private final float upZ = 0f;
    
	// Object definition area
	// Scene objects
	private Sea sea;
	private Sky sky;
	private Sun sun;
	// Entities
	private Ship playerShip;

	// World matrices definition area
    private final float[] mMVPMatrix = new float[16];
	private final float[] mViewProjectionMatrix = new float[16];
	private final float[] mModelMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
	
	// Light matrices definition area
	private float[] mLightModelMatrix = new float[16];
	private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
	private final float[] mLightPosInWorldSpace = new float[4];
	private final float[] mLightPosInEyeSpace = new float[4];
	
	// Shader programs definition area
	private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
	private LightPointShaderProgram lightPointProgram;

	// Textures to be used
    private int waterTexture;
    private int rippleTexture;
    private int skyTexture;
	private int shipTexture;
    
    public GLRenderer(final Context activityContext){
    	this.mActivityContext = activityContext;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		// Initialize objects
		sea = new Sea();
		sky = new Sky();
		sun = new Sun();
		
		playerShip = new Ship(mActivityContext, ShipType.LIGHT, new Point(0,0,0), new Vector(0,0,1), 2f, 3f, 5f, 10, com.pirateseas.global.Constants.UNLIMITED);
        
        // Load textures
		lightPointProgram = new LightPointShaderProgram(mActivityContext);
		textureProgram = new TextureShaderProgram(mActivityContext);
        colorProgram = new ColorShaderProgram(mActivityContext);

        waterTexture = TextureHelper.loadTexture(mActivityContext, R.drawable.blue_water_texture);
        skyTexture = TextureHelper.loadTexture(mActivityContext, R.drawable.sky_clear);
		
		shipTexture = TextureHelper.loadTexture(mActivityContext, R.drawable.ship_base);
		rippleTexture = TextureHelper.loadTexture(mActivityContext, R.drawable.sea_ripples);
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
        long time = SystemClock.uptimeMillis() % (SECONDS_PER_REVOLUTION * 1000L);
        float angleInDegrees = (360.0f / (SECONDS_PER_REVOLUTION * 1000.0f)) * ((int) time);
		
        // Clear the rendering surface (Reset background color to 'Black').
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        // Set the camera position (View matrix)
        setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
		
		// Multiply the view and projection matrices together.
        multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
		// Draw the sky plane
     	positionSkyPlane(eyeZ * 10);
     	textureProgram.useProgram();
        textureProgram.setUniforms(mMVPMatrix, skyTexture);
		sky.bindData(textureProgram);
		sky.draw();
		
		// Draw the water plane
		positionWaterPlane(eyeZ * 10);
		textureProgram.useProgram();
        textureProgram.setUniforms(mMVPMatrix, waterTexture);
		sea.bindData(textureProgram);
		sea.draw();
		
		// Draw enemy Ships
		// TODO
		
		// Draw the player Ship
		positionPlayerShip();
		textureProgram.useProgram();
		if(playerShip.isMoving())
			textureProgram.setUniforms(mMVPMatrix, rippleTexture);
		textureProgram.setUniformsLevel1(mMVPMatrix, shipTexture);
		playerShip.bindData(textureProgram);
		playerShip.draw();
		
		// Draw the Sun Light
		positionLight(angleInDegrees);
		lightPointProgram.useProgram();
		lightPointProgram.setUniforms(mLightPosInModelSpace);
		sun.transformInWorldSpace(lightPointProgram, mLightModelMatrix, mViewProjectionMatrix);
		sun.draw();
		
		// Draw GUI
		// TODO
    }
	
	private void positionLight(float angleInDegrees){
		// Draw the light moving around
		setIdentityM(mLightModelMatrix, 0);
        translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -2.0f);
		// Obtener coordenadas de angulo 23? 26'(703 / 30 = 23.433333333) y radio 9; @source: http://www.pawean.com/MVM/Coordenadas%20Cartesianas3D.html
		// x = 9 * cos(23.433333333) * cos(0f) = 8.257710772f
		// y = 9 * cos(23.433333333) * sin(0f) = 0f
		// z = 9 * sin(23.433333333) = 3.579135763f
        rotateM(mLightModelMatrix, 0, angleInDegrees, 8.257f, 1.0f, 3.579f);
        translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 3.5f);
		
		multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);
	}
	
	private void positionPlayerShip(){
		setIdentityM(mModelMatrix, 0);
		rotateM(mModelMatrix, 0, 180, 1f, 0f, 0f);
		scaleM(mModelMatrix, 0, 0.4f, 1.0f, 0.4f);
		multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
	}

	private void positionWaterPlane(float scaleFactor) {
        // The plane is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        setIdentityM(mModelMatrix, 0);
        scaleM(mModelMatrix, 0, scaleFactor, 1.0f, scaleFactor);
		rotateM(mModelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
    }
	
	private void positionSkyPlane(float scaleFactor) {
        // The plane is defined in terms of X & Y coordinates, so it lie flat on the XY plane.
        setIdentityM(mModelMatrix, 0);
        scaleM(mModelMatrix, 0, scaleFactor, scaleFactor, 1f);
		// We move the plane to the farthest extreme of the Render Box
		// Positive Z-axis translation goes OUT of the screen (Negative goes INTO the screen)
        translateM(mModelMatrix, 0, 0f, 0.5f, -5f);
        multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
    }
	
	public void moveWorld(float distance, float angle){
		// Change position of water plane
		// Change position of sky plane
		// Change position of enemy ships
		
	}

	public void resetEye() {
		eyeX = 0;
	    eyeY = 1.2f;		
		eyeZ = 1.2f;
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