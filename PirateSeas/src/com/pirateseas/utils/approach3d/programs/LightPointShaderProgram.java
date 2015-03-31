/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.pirateseas.utils.approach3d.programs;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glVertexAttrib3f;
import static android.opengl.GLES20.glDisableVertexAttribArray;

import com.pirateseas.R;

import android.content.Context;



public class LightPointShaderProgram extends ShaderProgram {    
    // Uniform locations
    private final int uMatrixLocation;
    
    // Attribute locations
    private final int aPositionLocation;

    public LightPointShaderProgram(Context context) {
		super(context, R.raw.point_vertex_shader,
				R.raw.point_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
               
        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
    }

    public void setUniforms(float[] lightCoordsArray) {
        // Pass the matrix into the shader program.
		glVertexAttrib3f(uMatrixLocation, lightCoordsArray[0], lightCoordsArray[1], lightCoordsArray[2]);
		
		glDisableVertexAttribArray(aPositionLocation);
    }
    
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
}