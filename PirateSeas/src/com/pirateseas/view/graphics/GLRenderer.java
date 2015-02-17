package com.pirateseas.view.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.pirateseas.model.basicfigures.Plane;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class GLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    
    private final Context mActivityContext;
    
    private Plane mPlane;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private float mAngle;
    
    /** These are handles to our texture data. */
    private int mWaterDataHandle;
    private int mSkyDataHandle;
    
    public GLRenderer(final Context activityContext){
    	mActivityContext = activityContext;
    	

    	
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mPlane = new Plane(mActivityContext);
        
        //mSkyDataHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.sky_texture);
        //GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //mWaterDataHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.water_texture);
        //GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];
        
        final float eyeX = 0;
        final float eyeY = 0;
        final float eyeZ = -2;
        
        final float lookX = 0f;
        final  float lookY = 0f;
        final float lookZ = 0f;
        
        final float upX = 0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Log.v(TAG, "Model Matrix = " + mMVPMatrix.toString());
        Log.v(TAG, "Projection Matrix = " + mProjectionMatrix.toString());
        Log.v(TAG, "View Matrix = " + mViewMatrix.toString());
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mWaterDataHandle);
        
        // Create a rotation for the plane
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 1.0f, 0);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        
        // Draw square
        mPlane.draw(mMVPMatrix);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);

    }

    

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

}