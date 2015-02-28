package com.pirateseas.model.entity;


import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static com.pirateseas.utils.Constants.BYTES_PER_FLOAT;

import com.pirateseas.utils.Geometry.Point;
import com.pirateseas.utils.Geometry.Vector;
import com.pirateseas.utils.data.VertexArray;
import com.pirateseas.utils.programs.ColorShaderProgram;
import com.pirateseas.utils.programs.TextureShaderProgram;

public class Entity {
	
	private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT 
        + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
		
	private static final int[] SPEEDS = {0, 2, 5, 10};
	
	// Position attribs
	private Point mCoords;
	protected Vector mDirection;
	private float mWidth;
	private float mHeight;
	private float mDepth;
	
	private final float[] mSize = {mWidth, mHeight, mDepth};
	private boolean isTextured = false;
	
	// Common attribs
	protected int mHealthPoints = 0;
	private int mSpeed = 0;	
	
	private final VertexArray mVertexArray;
	
	public Entity(VertexArray vertexArray, Point coordinates, Vector direction, float width, float height, float depth){
		this.mVertexArray = vertexArray;
		this.mCoords = coordinates;
		this.mDirection = direction;
		this.mWidth = width;
		this.mHeight = height;
		this.mDepth = depth;
		mSize[0] = width;
		mSize[1] = height;
		mSize[2] = depth;
	}
	
	public void bindData(TextureShaderProgram textureProgram) {
		isTextured = true;
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
	
	public void bindData(ColorShaderProgram colorProgram) {
        mVertexArray.setVertexAttribPointer(
            0, 
            colorProgram.getPositionAttributeLocation(), 
            POSITION_COMPONENT_COUNT, 0);
    }
	
	public void draw() {
		if (isTextured){ // Textures are being used
			glDrawArrays(GL_TRIANGLE_STRIP, 0, mVertexArray.getVertexPointsNum(POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT));
		} else { // Colours are being used
			glDrawArrays(GL_TRIANGLE_STRIP, 0, mVertexArray.getVertexPointsNum(POSITION_COMPONENT_COUNT));
		}
    }
	
	public boolean intersectionWithEntity(Entity other){
		boolean intersection = false;
		
		// TODO
		
//		// Intersection with front panel
//		if (mCoords.z + mWidth / 2 <= other.Position.z - other.getSize()[2] / 2 && 
//				mCoords.z - mWidth / 2 <= other.Position.z + other.getSize()[2] / 2)
//			intersection = true;
//		// Intersection with right panel
//		if (mCoords.x + mWidth / 2 <= other.Position.x - other.getSize()[0] / 2 && 
//		/* TODO Verificar que 'other' no esté a la izquierda de la entidad*/)
//			intersection = true;
//		// Intersection with back panel
//		if (mCoords.z - mWidth / 2 <= other.Position.z + other.getSize()[2] / 2 && 
//		/* TODO Verificar que 'other' no esté delante de la entidad*/)
//			intersection = true;
//		// Intersection with left panel
//		if (mCoords.x - mWidth / 2 >= other.Position.x + other.getSize()[0] / 2 && 
//		/* TODO Verificar que 'other' no esté a la derecha de la entidad*/)
//			intersection = true;
		
		return intersection;
	}
	
	public void gainHealth(int points){
		if(points > 0)
			mHealthPoints += points;
		else
			throw new IllegalArgumentException("Encontrado valor de puntos negativo al modificar HelthPoints");
	}
	
	public void looseHealth(int points){
		if(points > 0)
			mHealthPoints -= points;
		else
			throw new IllegalArgumentException("Encontrado valor de puntos negativo al modificar HelthPoints");
	}
	
	public boolean isAlive(){
		return mHealthPoints > 0 ? true : false;
	}
	
	public void changeSpeed(int speedLevel){
		if(speedLevel >= 0 && speedLevel <= SPEEDS.length)
			mSpeed = SPEEDS[speedLevel];
	}
	
	public boolean[] compareTo(Entity other){
		boolean[] entityCompass = new boolean[4]; // North, East, South, West
		
		// Initialize
		for(boolean c : entityCompass)
			c = false;
		
		// TODO 
		
		return entityCompass;
	}
	
	public float[] getSize(){
		return mSize;
	}

}
