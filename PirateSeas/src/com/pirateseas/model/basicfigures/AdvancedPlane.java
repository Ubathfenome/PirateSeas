package com.pirateseas.model.basicfigures;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;

import com.pirateseas.utils.data.VertexArray;
import com.pirateseas.utils.programs.TextureShaderProgram;
import com.pirateseas.utils.Geometry;
import com.pirateseas.utils.Geometry.Point;

import static com.pirateseas.utils.Constants.BYTES_PER_FLOAT;

public class AdvancedPlane {
	
	private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT 
        + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    
    private final VertexArray mVertexArray;
    
    public AdvancedPlane(VertexArray vertexArray) {
        mVertexArray = vertexArray;
    }
    
    public void bindData(TextureShaderProgram textureProgram) {
        mVertexArray.setVertexAttribPointer(
            0, 
            textureProgram.getPositionAttributeLocation(), 
            POSITION_COMPONENT_COUNT,
            STRIDE);
        
        mVertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT, 
            textureProgram.getTextureCoordinatesAttributeLocation(),
            TEXTURE_COORDINATES_COMPONENT_COUNT, 
            STRIDE);
    }
	
	public Point getModelOrigin(){
		// TODO Change point
		return new Point(0,0,0);
	}
	
	public Point getTextureOrigin(){
		// TODO Change point
		return new Point(0,0,0);
	}
        
    public void draw() {                                
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}
