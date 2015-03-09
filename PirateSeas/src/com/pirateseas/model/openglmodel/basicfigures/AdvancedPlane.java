package com.pirateseas.model.openglmodel.basicfigures;

import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glBlendFunc;

import com.pirateseas.global.Constants;
import com.pirateseas.utils.data.VertexArray;
import com.pirateseas.utils.programs.TextureShaderProgram;

public class AdvancedPlane {
	
	private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT 
        + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;
    
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
        
    public void draw() {     
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}
