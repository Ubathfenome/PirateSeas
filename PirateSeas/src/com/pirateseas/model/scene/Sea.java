package com.pirateseas.model.scene;

import com.pirateseas.model.basicfigures.AdvancedPlane;
import com.pirateseas.controller.utils.Geometry;
import com.pirateseas.controller.utils.Geometry.Point;
import com.pirateseas.controller.utils.data.VertexArray;

public class Sea extends AdvancedPlane{
	
	private AdvancedPlane mPlane;
	
	private Point modelOrigin;
	
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
	
	public Sea(){
		super(new VertexArray(VERTEX_DATA));
		modelOrigin = mPlane.getModelOrigin();
	}

}
