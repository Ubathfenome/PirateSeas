package com.pirateseas.model.entity;

import com.pirateseas.exceptions.NoAmmoException;
import com.pirateseas.utils.Geometry.Point;
import com.pirateseas.utils.Geometry.Vector;
import com.pirateseas.utils.data.VertexArray;

import static com.pirateseas.utils.Constants.BYTES_PER_FLOAT;

public class Boat extends Entity{
	
	private static final int POSITION_COMPONENT_COUNT = 3;
	
	private int mAmmunition = 0;
	
	private static final int STATE_ALIVE = 1;
	private static final int STATE_DEAD = 0;
	private static final int STATE_UNKNOWN = -1;
	private static final int UNLIMITED = -1;
	
	private int mStatus = STATE_DEAD;
	
	private static final float[] VERTEX_DATA = {
		-1.0f, 1.0f, 3.5f,  
		 1.0f, 1.0f, 3.5f,
		 0.0f, 0.0f, 3.5f,
		 1.0f, 1.0f, 5.0f,
		 0.0f, 0.0f, 5.0f,
		 1.0f, 1.0f, 6.5f,
		 0.0f, 0.0f, 6.5f,
		 0.0f, 1.0f, 8.5f,
		-1.0f, 1.0f, 6.5f,
		-1.0f, 1.0f, 5.0f
	};
	
	private final static VertexArray mVertexArray = new VertexArray(VERTEX_DATA);
	
	public Boat(Point coordinates, Vector direction, float width, float height, float depth, int health, int ammo){
		super(mVertexArray, coordinates,direction,width,height,depth);
		this.mAmmunition = ammo;
		gainHealth(health);
		if(mHealthPoints > 0)
			mStatus = STATE_ALIVE;
	}
	
	public void move(Vector direction, float distance){
		// TODO
	}
	
	public void gainAmmo(int ammo){
		if(ammo > 0)
			mAmmunition += ammo;
		else
			throw new IllegalArgumentException("Encontrado valor de puntos negativo al modificar Ammunition");
	}
	
	public Vector shootFront() throws NoAmmoException{
		Vector cannonballVector = null;
		
		switch(mAmmunition){
			case UNLIMITED:
				cannonballVector = new Vector(mDirection);
				break;
			case 0:
				mAmmunition += 3;
				throw new NoAmmoException();
		default:
				mAmmunition--;
				cannonballVector = new Vector(mDirection);
				break;
		}
		
		return cannonballVector;
	}
	
	public Vector[] shootSide(boolean isRight) throws NoAmmoException{
		Vector[] cannonballArray = null;
		float angle = 2;
		
		switch(mAmmunition){
			case UNLIMITED:
				if(isRight){
					/*
					cannonballArray = {
						new Vector(10,0,-2),
						new Vector(10,0,0),
						new Vector(10,0,2),
					};
					*/					
				} else {
					/*
					cannonballArray = {
						new Vector(-10,0,-2),
						new Vector(-10,0,0),
						new Vector(-10,0,2),
					};
					*/
				}
				break;
			case 0:
			case 1:
			case 2:
				// mAmmunition += 3;
				throw new NoAmmoException();
			default:
				mAmmunition -= 3;
				if(isRight){
					/*
					cannonballArray = {
						new Vector(10,0,-2),
						new Vector(10,0,0),
						new Vector(10,0,2),
					}
					*/					
				} else {
					/*
					cannonballArray = {
						new Vector(-10,0,-2),
						new Vector(-10,0,0),
						new Vector(-10,0,2),
					}
					*/
				}
				break;
		}
		return cannonballArray;
	}
	
}