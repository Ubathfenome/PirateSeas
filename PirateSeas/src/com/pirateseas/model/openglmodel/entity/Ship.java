package com.pirateseas.model.openglmodel.entity;

import android.content.Context;

import com.pirateseas.R;
import com.pirateseas.exceptions.NoAmmoException;
import com.pirateseas.global.Constants;
import com.pirateseas.model.openglmodel.basicfigures.AdvancedPlane;
import com.pirateseas.utils.approach2d.Geometry.Point3D;
import com.pirateseas.utils.approach2d.Geometry.Vector3D;
import com.pirateseas.utils.approach3d.data.VertexArray;
import com.pirateseas.utils.approach3d.programs.TextureShaderProgram;

public class Ship extends Entity {

	/*
	private static final float[] VERTEX_DATA = {
		0.6f, 1.234f,  1f, 1f,
		0.0f, 0.0f,	   0f, 0f,
		0.4f, 0.0f,    2f, 0f,
		0.4f, 1.166f,  2f, 2f,
		0.0f, 1.166f,  0f, 2f,
		0.0f, 0.0f,    0f, 0f   
	};
	*/
	
	private static final float[] VERTEX_DATA = {
        // Order of coordinates: X, Y, S, T
		// X: x coord of texture
		// Y: y coord of texture
		// S: x coord of model
		// T: y coord of model
        // Triangle Fan (The first vertex of a triangle fan acts like a hub.
		// The vertices following connect with the previous non-starting 
		// vertex and the hub)
            0f,  0f,   0.5f, 0.5f, 
           -1f, -1f,   0f, 0f,  
            1f, -1f,   1f, 0f, 
            1f,  1f,   1f, 1f, 
           -1f,  1f,   0f, 1f, 
           -1f, -1f,   0f, 0f };
	
	private static AdvancedPlane mPlane;
	
	private static VertexArray vArray = new VertexArray(VERTEX_DATA);

	private int mAmmunition = 0;
	private int mStatus = Constants.STATE_ALIVE;
	
	Context context;
	
	@SuppressWarnings("deprecation")
	public Ship(Context context, ShipType sType, Point3D coordinates, Vector3D direction, float width, float height, float depth, int health, int ammo){
		super(context, coordinates,direction,width,height,depth);
		mPlane = new AdvancedPlane(vArray);
		
		this.context = context;
		
		this.mAmmunition = ammo;
		gainHealth(health);
		
		if(mHealthPoints > 0)
			setStatus(Constants.STATE_ALIVE);
		
		switch(sType){
		case LIGHT:
			mImage = context.getResources().getDrawable(R.drawable.txtr_ship_light);
			break;
		case MEDIUM:
			mImage = context.getResources().getDrawable(R.drawable.txtr_ship_medium);
			break;
		case HEAVY:
			mImage = context.getResources().getDrawable(R.drawable.txtr_ship_base);
			break;
		}
		
		
	}
	
	/**
	*	@param angle: Angle over the XZ plane (positive = turn left; negative = turn right)
	*/
	public Vector3D move(float angle){
		Vector3D direction = null;
		
		switch(mSpeed){
			case 0:
			break;
			case 1:
			break;
			case 2:
			break;
		}
		
		return direction;
	}
	
	public void gainAmmo(int ammo){
		if(ammo > 0)
			mAmmunition += ammo;
		else
			throw new IllegalArgumentException("Encontrado valor de puntos negativo al modificar Ammunition");
	}
	
	public Vector3D shootFront() throws NoAmmoException{
		Vector3D cannonballVector = null;
		
		switch(mAmmunition){
			case Constants.SHOT_AMMO_UNLIMITED:
				cannonballVector = new Vector3D(mDirection);
				break;
			case 0:
				mAmmunition += 3;
				throw new NoAmmoException(context.getResources().getString(R.string.exception_ammo));
		default:
				mAmmunition--;
				cannonballVector = new Vector3D(mDirection);
				break;
		}
		
		return cannonballVector;
	}
	
	public Vector3D[] shootSide(boolean isRight) throws NoAmmoException{
		Vector3D[] cannonballArray = new Vector3D[3];
		
		switch(mAmmunition){
			case Constants.SHOT_AMMO_UNLIMITED:
				if(isRight){
					cannonballArray[0] = new Vector3D(10,0,-2);
					cannonballArray[1] = new Vector3D(10,0,0);
					cannonballArray[2] = new Vector3D(10,0,2);
				} else {
					cannonballArray[0] = new Vector3D(-10,0,-2);
					cannonballArray[1] = new Vector3D(-10,0,0);
					cannonballArray[2] = new Vector3D(-10,0,2);
				}
				break;
			case 0:
			case 1:
			case 2:
				// mAmmunition += 3;
				cannonballArray = null;
				throw new NoAmmoException(context.getResources().getString(R.string.exception_ammo));
			default:
				mAmmunition -= 3;
				if(isRight){
					cannonballArray[0] = new Vector3D(10,0,-2);
					cannonballArray[1] = new Vector3D(10,0,0);
					cannonballArray[2] = new Vector3D(10,0,2);
				} else {
					cannonballArray[0] = new Vector3D(-10,0,-2);
					cannonballArray[1] = new Vector3D(-10,0,0);
					cannonballArray[2] = new Vector3D(-10,0,2);
				}
				break;
		}
		return cannonballArray;
	}

	public void bindData(TextureShaderProgram textureProgram) {
		mPlane.bindData(textureProgram);	
	}

	public void draw() {
		mPlane.draw();		
	}

	/**
	 * @return the mStatus
	 */
	public int getStatus() {
		return mStatus;
	}

	/**
	 * @param mStatus the mStatus to set
	 */
	public void setStatus(int mStatus) {
		this.mStatus = mStatus;
	}

	/**
	 * @return the mAmmunition
	 */
	public int getAmmunition() {
		return mAmmunition;
	}
}