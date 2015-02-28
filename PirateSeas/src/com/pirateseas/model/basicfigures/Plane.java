package com.pirateseas.model.basicfigures;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;

import com.pirateseas.utils.data.VertexArray;
import com.pirateseas.utils.programs.TextureShaderProgram;

import static com.pirateseas.utils.Constants.BYTES_PER_FLOAT;

public class Plane {
	
	private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT 
        + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    
    private static final float[] VERTEX_DATA = {
        // Order of coordinates: X, Y, S, T
		// X: x coord of texture
		// Y: y coord of texture
		// S: x coord of model
		// T: y coord of model
        // Triangle Fan (Counter-Clockwise Direction)
           0f,    0f, 0.5f, 0.5f, 
        -0.5f, -0.8f,   0f, 0.9f,  
         0.5f, -0.8f,   1f, 0.9f, 
         0.5f,  0.8f,   1f, 0.1f, 
        -0.5f,  0.8f,   0f, 0.1f, 
        -0.5f, -0.8f,   0f, 0.9f };
    
    private final VertexArray vertexArray;
    
    public Plane() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }
    
    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(
            0, 
            textureProgram.getPositionAttributeLocation(), 
            POSITION_COMPONENT_COUNT,
            STRIDE);
        
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT, 
            textureProgram.getTextureCoordinatesAttributeLocation(),
            TEXTURE_COORDINATES_COMPONENT_COUNT, 
            STRIDE);
    }
        
    public void draw() {                                
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}
