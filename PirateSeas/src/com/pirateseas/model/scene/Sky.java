package com.pirateseas.model.scene;

import com.pirateseas.model.basicfigures.AdvancedPlane;
import com.pirateseas.utils.data.VertexArray;


public class Sky extends AdvancedPlane{

	private static AdvancedPlane mPlane;
	
	private static final float[] VERTEX_DATA = {
        // Order of coordinates: X, Y, S, T
		// X: x coord of texture
		// Y: y coord of texture
		// S: x coord of model
		// T: y coord of model
        // Triangle Fan (The first vertex of a triangle fan acts like a hub.
		// The vertices following connect with the previous non-starting 
		// vertex and the hub)
            0f,  0f,   1f, 1f, 
           -1f, -1f,   0f, 0f,  
            1f, -1f,   2f, 0f, 
            1f,  1f,   2f, 2f, 
           -1f,  1f,   0f, 2f, 
           -1f, -1f,   0f, 0f };
	
	private static VertexArray vArray = new VertexArray(VERTEX_DATA);
	
	public Sky(){
		super(vArray);		
		mPlane = new AdvancedPlane(vArray);
	}
	
	// TODO Check: http://www.andersriggelsen.dk/glblendfunc.php
}
