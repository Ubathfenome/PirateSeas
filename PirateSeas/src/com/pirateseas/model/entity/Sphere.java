package com.pirateseas.model.entity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import com.pirateseas.view.graphics.MyGLRenderer;

public class Sphere {

	private final String vertexShaderCode = "precision highp float;"
			+ "// Matrices" + "uniform mat4 u_mvMatrix;"
			+ "uniform mat4 u_mvpMatrix;" +
			"// Attributes" + "attribute vec4 a_position;"
			+ "attribute vec3 a_normal;" +
			"// Varyings" + "varying vec3 v_ecNormal;" +
			"void main() {" +
			"// Define position and normal in model coordinates"
			+ "vec4 mcPosition = a_position;" + "vec3 mcNormal = a_normal;" +
			"// Calculate and normalize eye space normal"
			+ "vec3 ecNormal = vec3(u_mvMatrix * vec4(mcNormal, 0.0));"
			+ "ecNormal = ecNormal / length(ecNormal);"
			+ "v_ecNormal = ecNormal;" +
			"gl_Position = u_mvpMatrix * mcPosition;" + "}";
	
	private final String fragmentShaderCode = "precision highp float;"
			+"struct DirectionalLight {"
			+ "vec3 direction;"
			+ "vec3 halfplane;"
			+ "vec4 ambientColor;"
			+ "vec4 diffuseColor;"
			+ "vec4 specularColor;"
			+ "};"
			+"struct Material {"
			+ "vec4 ambientFactor;"
			+ "vec4 diffuseFactor;"
			+ "vec4 specularFactor;"
			+ "float shininess;"
			+ "};"
			+
			"// Light"
			+ "uniform DirectionalLight u_directionalLight;"
			+
			"// Material"
			+ "uniform Material u_material;"
			+
			"varying vec3 v_ecNormal;"
			+
			"void main() {"
			+
			"// Normalize v_ecNormal"
			+ "vec3 ecNormal = v_ecNormal / length(v_ecNormal);"
			+
			"float ecNormalDotLightDirection = max(0.0, dot(ecNormal, u_directionalLight.direction));"
			+ "float ecNormalDotLightHalfplane = max(0.0, dot(ecNormal, u_directionalLight.halfplane));"
			+
			"// Calculate ambient light"
			+ "vec4 ambientLight = u_directionalLight.ambientColor * u_material.ambientFactor;"
			+
			"// Calculate diffuse light"
			+ "vec4 diffuseLight = ecNormalDotLightDirection * u_directionalLight.diffuseColor * u_material.diffuseFactor;"
			+
			"// Calculate specular light"
			+ "vec4 specularLight = vec4(0.0);"
			+ "if (ecNormalDotLightHalfplane > 0.0) {"
			+ "specularLight = pow(ecNormalDotLightHalfplane, u_material.shininess) * u_directionalLight.specularColor * u_material.specularFactor;"
			+ "}" +
			"vec4 light = ambientLight + diffuseLight + specularLight;" +
			"gl_FragColor = light;" + "}";
	
	private final FloatBuffer vertexBuffer;
	private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float sphereCoords[] = {
            // in counterclockwise order:
            -1.0f,  1.0f, 0.0f,   // top left
           -1.0f, -1.0f, 0.0f,   // bottom left
            -1.0f, 1.0f, 0.0f,    // bottom right
            1.0f,  1.0f, 0.0f   // top right
    };
    
    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    
    private final int vertexCount = sphereCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.0f };

	public Sphere() {
		// initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
        		sphereCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(sphereCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
	}
	
	/**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
