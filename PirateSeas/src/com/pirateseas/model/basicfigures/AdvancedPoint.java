package com.pirateseas.model.basicfigures;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniformMatrix4fv;

import static android.opengl.Matrix.multiplyMM;
import com.pirateseas.utils.programs.LightPointShaderProgram;

public class AdvancedPoint {
	
	public void transformInWorldSpace(LightPointShaderProgram program, float[] mLightModelMatrix, float[] mViewProjectionMatrix){
		float[] mMVPMatrix = new float[16];
		
		multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mLightModelMatrix, 0);
		glUniformMatrix4fv(program.getPositionAttributeLocation(), 1, false, mMVPMatrix, 0);
	}
           
    public void draw() {           
        glDrawArrays(GL_POINTS, 0, 1);
    }
}
