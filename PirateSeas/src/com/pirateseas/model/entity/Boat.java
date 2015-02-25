package com.pirateseas.model.entity;

import com.pirateseas.controller.utils.Geometry.Point;
import com.pirateseas.controller.utils.Geometry.Vector;
import com.pirateseas.controller.utils.data.VertexArray;

public class Boat extends Entity{
	
	private int mAmmunition = 0;
	
	public Boat(VertexArray vertexArray, Point coordinates, Vector direction, float width, float height, float depth, int health, int ammo){
		super(vertexArray, coordinates,direction,width,height,depth);
		this.mAmmunition = ammo;
		gainHealth(health);
	}
	
	public void move(Vector direction, float distance){
		// TODO
	}
	
}