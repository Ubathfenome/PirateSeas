package com.pirateseas.model.entity;

import android.content.Context;

import com.pirateseas.R;
import com.pirateseas.exceptions.NoAmmoException;
import com.pirateseas.utils.Geometry.Point;
import com.pirateseas.utils.Geometry.Vector;


public class Ship extends Entity{
	
	
	private int mAmmunition = 0;
	
	private static final int STATE_ALIVE = 1;
	private static final int STATE_DEAD = 0;
	private static final int UNLIMITED = -1;
	
	private int mStatus = STATE_DEAD;
	
	
	
	public Ship(Context context, ShipType sType, Point coordinates, Vector direction, float width, float height, float depth, int health, int ammo){
		super(context, coordinates,direction,width,height,depth);
		this.mAmmunition = ammo;
		gainHealth(health);
		if(mHealthPoints > 0)
			mStatus = STATE_ALIVE;
		switch(sType){
		case LIGHT:
			mImage = context.getResources().getDrawable(R.drawable.ship_light);
			break;
		case MEDIUM:
			mImage = context.getResources().getDrawable(R.drawable.ship_medium);
			break;
		case HEAVY:
			mImage = context.getResources().getDrawable(R.drawable.ship_base);
			break;
		}
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