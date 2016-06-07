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
	// Crear array de variables para almacenar la cantidad de cada tipo de municion
	private int nAmmoTypes = Ammunitions.values().length;
	private int[] nAmmunitions = new int[nAmmoTypes];
	private Ammunitions selectedAmmo;
	private int selectedAmmoIndex= 0;
	
	private int mReloadTime;
	private float mPower;
	private float mRange;
	private long timestampLastShot;
	
	private boolean isPlayable;
	
	private ShipType sType;
	
	private Context context;
	
	public Ship(){
		super(null, 0, 0, 0, 0, new Point(0, 0), 90, 0, 0, 0);
		for(int i = 0; i < nAmmoTypes; i++){
			nAmmunitions[i] = 0;
		}
		
		this.sType = ShipType.LIGHT;
		this.isPlayable = false;
		this.selectedAmmo = Ammunitions.DEFAULT;
		this.selectedAmmoIndex = Ammunitions.valueOf(selectedAmmo.getName()).ordinal();
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public Ship(Context context, ShipType sType, double x, double y, double canvasWidth, 
				double canvasHeight, Point coordinates, int direction, int width, int height, int length, int ammo){
		super(context, x, y, canvasWidth, canvasHeight, coordinates, direction, width, height, length);
		
		this.context = context;
		
		this.selectedAmmo = Ammunitions.DEFAULT;
		this.selectedAmmoIndex = Ammunitions.valueOf(selectedAmmo.getName()).ordinal();
		this.nAmmunitions[selectedAmmoIndex] = ammo;
		
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
						setImage(context.getResources().getDrawable(R.drawable.enemy_light_front, null));
						break;
					case 1:
						setImage(context.getResources().getDrawable(R.drawable.enemy_medium_front, null));
						break;
					case 2:
						setImage(context.getResources().getDrawable(R.drawable.enemy_heavy_front, null));
						break;
				}
			}
		} else {
			if(isPlayable)
				setImage(context.getResources().getDrawable(sType.drawableValue()));
			else{
				switch(sType.ordinal()){
					case 0:
						setImage(context.getResources().getDrawable(R.drawable.enemy_light_front));
						break;
					case 1:
						setImage(context.getResources().getDrawable(R.drawable.enemy_medium_front));
						break;
					case 2:
						setImage(context.getResources().getDrawable(R.drawable.enemy_heavy_front));
						break;
				}
			}
		}
		
		if(mHealthPoints > 0)
			setStatus(Constants.STATE_ALIVE);
		
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public Ship(Context context, Ship baseShip, ShipType sType, Point coordinates, int direction, int width, int height, int length, int health, int ammo){
		super(context, baseShip.x, baseShip.y, baseShip.mCanvasWidth, baseShip.mCanvasHeight, coordinates, direction, width, height, length);
		
		this.context = context;
		
		this.nAmmunitions[0] = ammo;
		this.selectedAmmo = Ammunitions.DEFAULT;
		this.selectedAmmoIndex = Ammunitions.valueOf(selectedAmmo.getName()).ordinal();
		
		this.sType = sType;
		this.mRange = sType.rangeMultiplier();
		this.mPower = sType.powerMultiplier();
		this.mReloadTime = (int) sType.powerMultiplier() * Constants.SHIP_RELOAD;
		this.mMaxHealth = sType.defaultHealthPoints() > health ? sType.defaultHealthPoints() : health;
		gainHealth(health);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(context.getResources().getDrawable(sType.drawableValue(), null));
		} else {
			setImage(context.getResources().getDrawable(sType.drawableValue()));
		}
		
		if(mHealthPoints > 0)
			setStatus(Constants.STATE_ALIVE);
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public Ship(Context context, Ship baseShip, ShipType sType, Point coordinates, int direction, int width, int height, int length, int health, int[] ammoTypes,
			int selectedAmmoType) {
		super(context, baseShip.x, baseShip.y, baseShip.mCanvasWidth, baseShip.mCanvasHeight, coordinates, direction, width, height, length);
		
		this.context = context;
		
		this.nAmmunitions = ammoTypes;
		this.selectedAmmo = Ammunitions.values()[selectedAmmoType];
		this.selectedAmmoIndex = selectedAmmoType;
		
		this.sType = sType;
		this.mRange = sType.rangeMultiplier();
		this.mPower = sType.powerMultiplier();
		this.mReloadTime = (int) sType.powerMultiplier() * Constants.SHIP_RELOAD;
		this.mMaxHealth = sType.defaultHealthPoints() > health ? sType.defaultHealthPoints() : health;
		gainHealth(health);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setImage(context.getResources().getDrawable(sType.drawableValue(), null));
		} else {
			setImage(context.getResources().getDrawable(sType.drawableValue()));
		}
		
		if(mHealthPoints > 0)
			setStatus(Constants.STATE_ALIVE);
		
	}

	public void gainAmmo(int ammo, Ammunitions ammoType){
		if(ammo > 0){
			nAmmunitions[Ammunitions.valueOf(ammoType.getName()).ordinal()] += ammo;
		} else
			throw new IllegalArgumentException("Encontrado valor de puntos negativo al modificar mAmmunition");
	}
	
	public Shot shootFront() throws NoAmmoException {
		Shot cannonballVector = null;

		if (nAmmunitions[selectedAmmoIndex] > 0 || nAmmunitions[selectedAmmoIndex] == Constants.SHOT_AMMO_UNLIMITED) {
			timestampLastShot = SystemClock.elapsedRealtime();
			cannonballVector = new Shot(context, x + (mWidth / 2) - (Shot.shotWidth / 2), y,
					this.mCanvasWidth, this.mCanvasHeight, new Point(
							this.entityCoordinates.x, this.entityCoordinates.y
									+ entityLength / 2), new Point(0,
							Constants.SHIP_BASIC_RANGE
									* sType.rangeMultiplier()), 90,
					(int) (Constants.SHIP_BASIC_DAMAGE * sType
							.powerMultiplier()), timestampLastShot);
			if (nAmmunitions[selectedAmmoIndex] != Constants.SHOT_AMMO_UNLIMITED)
				nAmmunitions[selectedAmmoIndex]--;
		} else {
			throw new NoAmmoException(context.getResources().getString(
					R.string.exception_ammo));
		}

		return cannonballVector;
	}

	public Shot[] shootSide() throws NoAmmoException {
		Shot[] cannonballArray = new Shot[3];
		Shot cannonballVector = null;
		
		// TODO Ajustar tiro disperso hacia abajo en vez de hacia la derecha

		if (nAmmunitions[selectedAmmoIndex] >= 3 || nAmmunitions[selectedAmmoIndex] == Constants.SHOT_AMMO_UNLIMITED) {
			timestampLastShot = SystemClock.elapsedRealtime();
			for (int i = 0, length = cannonballArray.length; i < length; i++) {

				Point ini = new Point(this.entityCoordinates.x + entityWidth
						/ 2, this.entityCoordinates.y);
				Point fin = new Point(Constants.SHIP_BASIC_RANGE
						* sType.rangeMultiplier(), i - 1);
				int num = fin.y - ini.y;
				int den = fin.x - ini.x;
				float m = (num * 1.0f) / den;
				double angM = Math.toDegrees(Math.atan(m));
				cannonballVector = new Shot(context, x + (mWidth / 2), y
						+ (mHeight / 4), this.mCanvasWidth, this.mCanvasHeight,
						ini, fin, (int) (-angM),
						(int) (Constants.SHIP_BASIC_DAMAGE * sType
								.powerMultiplier()), timestampLastShot);

				cannonballArray[i] = cannonballVector;
				if (nAmmunitions[selectedAmmoIndex] != Constants.SHOT_AMMO_UNLIMITED)
					nAmmunitions[selectedAmmoIndex]--;
			}
		} else {
			cannonballArray = null;
			throw new NoAmmoException(context.getResources().getString(
					R.string.exception_ammo));
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
	
	public void selectNextAmmo(){
		int actualAmmoIndex = selectedAmmoIndex;
		
		if(actualAmmoIndex + 1 == nAmmoTypes)
			actualAmmoIndex = 0;
		else
			actualAmmoIndex++;
		
		this.selectedAmmoIndex = actualAmmoIndex;
		this.selectedAmmo = Ammunitions.values()[selectedAmmoIndex];
	}
	
	public void selectPreviousAmmo(){
		int actualAmmoIndex = selectedAmmoIndex;
		
		if(actualAmmoIndex == 0)
			actualAmmoIndex = nAmmoTypes -1;
		else
			actualAmmoIndex--;
		
		this.selectedAmmoIndex = actualAmmoIndex;
		this.selectedAmmo = Ammunitions.values()[selectedAmmoIndex];
	}
	
	/*public boolean changeAmmoType () {
			// Obtener indice actual
			int nextIndexWithAmmo = -1; 
			
			for (int i = 0 ;i < nAmmoTypes; i++) {
				if(nAmmunitions[i] > 0 && i != selectedAmmoIndex){
					nextIndexWithAmmo = i;
					break;
				}
			}
			
			if(nextIndexWithAmmo != -1 && nextIndexWithAmmo != selectedAmmoIndex) {
				selectedAmmoIndex = nextIndexWithAmmo;
				return true;
			} else
				return false;
	}*/
	
	public boolean isReloaded(long timestamp){
		return timestamp - timestampLastShot > (mReloadTime * 1000) ? true : false;
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

	public int[] getAmmunitions() {
		return nAmmunitions;
	}
	
	public int getAmmunition(Ammunitions a){
		return nAmmunitions[a.ordinal()];
	}

	public boolean isPlayable() {
		return isPlayable;
	}

	@Override
	public String toString() {
		return "Ship [sType=" + sType + ", nAmmunitions=" + nAmmunitions[selectedAmmoIndex] + ", mReloadTime="
				+ mReloadTime + ", mRange=" + mRange + ", timestampLastShot="
				+ timestampLastShot + ", entityDirection=" + entityDirection
				+ ", entityCoordinates=" + entityCoordinates + "]";
	}

	public int getSelectedAmmunition() {
		return nAmmunitions[selectedAmmoIndex];
	}
	
}