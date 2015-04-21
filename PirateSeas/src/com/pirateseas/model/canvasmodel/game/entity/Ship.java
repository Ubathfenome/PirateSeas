package com.pirateseas.model.canvasmodel.game.entity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.SystemClock;

import com.pirateseas.R;
import com.pirateseas.exceptions.NoAmmoException;
import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.scene.Island;

public class Ship extends Entity {

	private int mAmmunition = 0;
	
	private int mReloadTime;
	private float mPower;
	private float mRange;
	private long timestampLastShot;
	
	private boolean isPlayable;
	
	private ShipType sType;
	
	private Context context;
	
	public Ship(){
		super(null, 0, 0, 0, 0, new Point(0, 0), 0, 0, 0);
		this.mAmmunition = 0;
		this.sType = ShipType.LIGHT;
		this.isPlayable = false;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public Ship(Context context, ShipType sType, double x, double y, double canvasWidth, 
				double canvasHeight, Point coordinates, int width, int height, int length, int ammo){
		super(context, x, y, canvasWidth, canvasHeight, coordinates, width, height, length);
		
		this.context = context;
		
		this.mAmmunition = ammo;
		
		this.sType = sType;
		this.mRange = sType.rangeMultiplier();
		this.mPower = sType.powerMultiplier();
		this.mReloadTime = (int) sType.powerMultiplier() * Constants.SHIP_RELOAD;
		gainHealth(this.mMaxHealth = sType.defaultHealthPoints());		
		
		this.isPlayable = (ammo == Constants.SHOT_AMMO_UNLIMITED) ? false : true;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			if(isPlayable)
				setImage(context.getResources().getDrawable(sType.drawableValue(), null));
			else{
				switch(sType.ordinal()){
					case 0:
						break;
					case 1:
						setImage(context.getResources().getDrawable(R.drawable.enemy_front_medium, null));
						break;
					case 2:
						setImage(context.getResources().getDrawable(R.drawable.enemy_front_heavy, null));
						break;
				}
			}
		} else {
			if(isPlayable)
				setImage(context.getResources().getDrawable(sType.drawableValue()));
			else{
				switch(sType.ordinal()){
					case 0:
						break;
					case 1:
						setImage(context.getResources().getDrawable(R.drawable.enemy_front_medium));
						break;
					case 2:
						setImage(context.getResources().getDrawable(R.drawable.enemy_front_heavy));
						break;
				}
			}
		}
		
		if(mHealthPoints > 0)
			setStatus(Constants.STATE_ALIVE);
		
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public Ship(Context context, Ship baseShip, ShipType sType, Point coordinates, int width, int height, int length, int health, int ammo){
		super(context, baseShip.x, baseShip.y, baseShip.mCanvasWidth, baseShip.mCanvasHeight, coordinates, width, height, length);
		
		this.context = context;
		
		this.mAmmunition = ammo;
		
		this.sType = sType;
		gainHealth(health);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(context.getResources().getDrawable(sType.drawableValue(), null));
		} else {
			setImage(context.getResources().getDrawable(sType.drawableValue()));
		}
		
		if(mHealthPoints > 0)
			setStatus(Constants.STATE_ALIVE);
	}
	
	public void gainAmmo(int ammo){
		if(ammo > 0)
			mAmmunition += ammo;
		else
			throw new IllegalArgumentException("Encontrado valor de puntos negativo al modificar mAmmunition");
	}
	
	public Shot shootFront() throws NoAmmoException{
		Shot cannonballVector = null;
		
		if(mAmmunition > 0 || mAmmunition == Constants.SHOT_AMMO_UNLIMITED){
			timestampLastShot = SystemClock.elapsedRealtime();
			cannonballVector = new Shot(context, this.x, this.y + entityLength, this.mCanvasWidth, this.mCanvasHeight, new Point(0, Constants.SHIP_BASIC_RANGE * sType.rangeMultiplier()));
			cannonballVector.setDamage((int) (10 * sType.powerMultiplier()));
			if(mAmmunition != Constants.SHOT_AMMO_UNLIMITED)
				mAmmunition--;
		} else {
			throw new NoAmmoException(context.getResources().getString(R.string.exception_ammo));
		}

		return cannonballVector;
	}
	
	public Shot[] shootSide(boolean isRight) throws NoAmmoException{
		Shot[] cannonballArray = new Shot[3];
		Shot cannonballVector = null;
		
		if(mAmmunition > 3 || mAmmunition == Constants.SHOT_AMMO_UNLIMITED){
			timestampLastShot = SystemClock.elapsedRealtime();
			for(int i = 0, length = cannonballArray.length; i < length; i++){
				if(isRight)
					cannonballVector = new Shot(context, this.x + entityWidth, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(Constants.SHIP_BASIC_RANGE * sType.rangeMultiplier(), i - 1));
				else
					cannonballVector = new Shot(context, this.x - 1, this.y, this.mCanvasWidth, this.mCanvasHeight, new Point(-Constants.SHIP_BASIC_RANGE * sType.rangeMultiplier(), i - 1));
				cannonballVector.setDamage((int) (10 * sType.powerMultiplier()));
								
				cannonballArray[i] = cannonballVector;
				if(mAmmunition != Constants.SHOT_AMMO_UNLIMITED)
					mAmmunition--;
			}
		} else {
			cannonballArray = null;
			throw new NoAmmoException(context.getResources().getString(R.string.exception_ammo));
		}
		
		return cannonballArray;
	}
	
	public boolean arriveIsland(Island island){
		double islandRightBorder = island.getX() + island.getWidth();
		double islandBottomBorder = island.getY() + island.getHeight();
		
		if(this.y < islandBottomBorder){
			if(this.x < (islandRightBorder) && this.x > island.getX())
				return true;
			if((this.x + this.mWidth) > (island.getX()) && (this.x + this.mWidth) < islandRightBorder)
				return true;
		}
		return false;		
	}
	
/*
	public void turn(int degrees){
		setEntityDirection(degrees);
		
		switch(sType){
			case LIGHT:
				switch(degrees){
					case 0:
						setImage(R.drawable.txtr_ship_light_right);
						break;
					case 90:
						setImage(R.drawable.txtr_ship_light_front);
						break;
					case 180:
						setImage(R.drawable.txtr_ship_light_left);
						break;
					case 270:
						setImage(R.drawable.txtr_ship_light_back);
						break;
				}
				break;
			case MEDIUM:
				switch(degrees){
					case 0:
						setImage(R.drawable.txtr_ship_medium_right);
						break;
					case 90:
						setImage(R.drawable.txtr_ship_medium_front);
						break;
					case 180:
						setImage(R.drawable.txtr_ship_medium_left);
						break;
					case 270:
						setImage(R.drawable.txtr_ship_medium_back);
						break;
				}
				break;
			case HEAVY:
				switch(degrees){
					case 0:
						setImage(R.drawable.txtr_ship_heavy_right);
						break;
					case 90:
						setImage(R.drawable.txtr_ship_heavy_front);
						break;
					case 180:
						setImage(R.drawable.txtr_ship_heavy_left);
						break;
					case 270:
						setImage(R.drawable.txtr_ship_heavy_back);
						break;
				}
			break;
		}
	}
*/
	
	public boolean isReloaded(long timestamp){
		return timestamp - timestampLastShot > mReloadTime ? true : false;
	}

	/**
	 * @return the mRange
	 */
	public float getRange() {
		return mRange;
	}

	/**
	 * @param mRange the mRange to set
	 */
	public void setRange(float mRange) {
		this.mRange = mRange;
	}
	
	public void addRange(float f) {
		this.mRange += f;
	}	

	public float getPower() {
		return mPower;
	}

	public void setPower(float mPower) {
		this.mPower = mPower;
	}
	
	public void addPower(float f) {
		this.mPower += f;		
	}

	/**
	 * @return the sType
	 */
	public ShipType getType() {
		return sType;
	}

	public int getMaxHealth() {
		return mMaxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.mMaxHealth = maxHealth;
	}

	/**
	 * @return the mAmmunition
	 */
	public int getAmmunition() {
		return mAmmunition;
	}

	public boolean isPlayable() {
		return isPlayable;
	}

	@Override
	public String toString() {
		return "Ship [sType=" + sType + ", mAmmunition=" + mAmmunition + ", mReloadTime="
				+ mReloadTime + ", mRange=" + mRange + ", timestampLastShot="
				+ timestampLastShot + ", entityDirection=" + entityDirection
				+ ", entityCoordinates=" + entityCoordinates + "]";
	}
	
}