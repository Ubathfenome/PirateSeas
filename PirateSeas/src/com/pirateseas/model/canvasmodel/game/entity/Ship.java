package com.pirateseas.model.canvasmodel.game.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

import com.pirateseas.R;
import com.pirateseas.exceptions.NoAmmoException;
import com.pirateseas.global.Constants;

public class Ship extends Entity {

	private int mAmmunition = 0;
	private int mStatus = Constants.STATE_DEAD;
	
	private int reloadTime = Constants.SHIP_RELOAD_SHORT;
	
	private ShipType sType;
	
	private Context context;
	
	public Ship(Context context, ShipType sType, double x, double y, double canvasWidth, 
				double canvasHeight, Point coordinates, int width, int height, int length, int ammo){
		super(context, x, y, canvasWidth, canvasHeight, coordinates, width, height, length);
		
		this.context = context;
		
		this.mAmmunition = ammo;
		
		this.sType = sType;
		gainHealth(sType.defaultHealthPoints());
		setImage(context.getResources().getDrawable(sType.drawableValue()));
		
		if(mHealthPoints > 0)
			setStatus(Constants.STATE_ALIVE);
		
	}
	
	public Ship(Context context, Ship baseShip, ShipType sType, Point coordinates, int width, int height, int length, int health, int ammo){
		super(context, baseShip.x, baseShip.y, baseShip.mCanvasWidth, baseShip.mCanvasHeight, coordinates, width, height, length);
		
		this.context = context;
		
		this.mAmmunition = ammo;
		
		this.sType = sType;
		gainHealth(sType.defaultHealthPoints());
		setImage(context.getResources().getDrawable(sType.drawableValue()));
		
		if(mHealthPoints > 0)
			setStatus(Constants.STATE_ALIVE);
	}
	
	public void gainAmmo(int ammo){
		if(ammo > 0)
			mAmmunition += ammo;
		else
			throw new IllegalArgumentException("Encontrado valor de puntos negativo al modificar Ammunition");
	}
	
	public Shot shootFront() throws NoAmmoException{
		Shot cannonballVector = null;
		
		if(mAmmunition > 0 || mAmmunition == Constants.SHOT_AMMO_UNLIMITED){
			cannonballVector = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(0, 3 * sType.rangeMultiplier()));
			cannonballVector.setDamage((int) (10 * sType.powerMultiplier()));
			if(mAmmunition != Constants.SHOT_AMMO_UNLIMITED)
				mAmmunition--;
		} else {
			throw new NoAmmoException(mAmmunition);
		}

		return cannonballVector;
	}
	
	public Shot[] shootSide(boolean isRight) throws NoAmmoException{
		Shot[] cannonballArray = new Shot[3];
		
		// TODO Adaptar segun el tipo de barco
		
		switch(mAmmunition){
			case Constants.SHOT_AMMO_UNLIMITED:
				if(isRight){
					cannonballArray[0] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(3, -1));
					cannonballArray[1] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(3, 0));
					cannonballArray[2] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(3, 1));
				} else {
					cannonballArray[0] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(-3,-1));
					cannonballArray[1] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(-3,0));
					cannonballArray[2] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(-3,1));
				}
				break;
			case 0:
			case 1:
			case 2:
				cannonballArray = null;
				throw new NoAmmoException(mAmmunition);
			default:
				mAmmunition -= 3;
				if(isRight){
					cannonballArray[0] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(3, -1));
					cannonballArray[1] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(3, 0));
					cannonballArray[2] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(3, 1));
				} else {
					cannonballArray[0] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(-3,-1));
					cannonballArray[1] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(-3,0));
					cannonballArray[2] = new Shot(context, this.x, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(-3,1));
				}
				break;
		}
		return cannonballArray;
	}
	
	/**
     * Draws on the screen the image of the model
     * 
     * @param canvas
     */
    public void drawOnScreen(Canvas canvas) {
        yUp = (int) y - mHeight / 2;
        xLeft = (int) x - mWidth / 2;
 
        mImage.setBounds(xLeft, yUp, xLeft + mWidth, yUp + mHeight);
        mImage.draw(canvas);
    }

	/**
	 * @return the sType
	 */
	public ShipType getType() {
		return sType;
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