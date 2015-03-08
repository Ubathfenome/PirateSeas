/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.pirateseas.utils.programs;

import static android.opengl.GLES20.glUseProgram;
import android.content.Context;

import com.pirateseas.utils.ShaderHelper;
import com.pirateseas.utils.ResourceReader;

abstract class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
	
	protected static final String U_MVMATRIX = "u_MVMatrix";
	protected static final String U_LIGHT_POSITION = "u_LightPos";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";    
    protected static final String A_COLOR = "a_Color";    
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    // Shader program
    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId,
        int fragmentShaderResourceId) {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
            ResourceReader
                .readTextResource(context, vertexShaderResourceId),
            ResourceReader
                .readTextResource(context, fragmentShaderResourceId));
    }        

    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program);
    }
}
