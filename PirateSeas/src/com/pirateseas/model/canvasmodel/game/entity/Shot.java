package com.pirateseas.model.canvasmodel.game.entity;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class Shot extends Entity{
	
	private Context mContext;
	
	private Point startPoint;
	private Point endPoint;
	
	private float pathLength;
	private float mFlyingTime;
	private float mCurrentTime;
	
	private long mTimestamp;
	
	private int mDamage;
	
	private int mShotStatus;
	
	protected Paint mBrush = null;
	protected Board board = null;
	
	protected static int shotWidth, shotHeight;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public Shot(Context context, double screenX, double screenY, double mCanvasWidth, double mCanvasHeight, Point entityBeginning, Point entityDestiny, int eDirection, int power, long timestampLastShot){
		super(context, screenX, screenY, mCanvasWidth, mCanvasHeight, entityBeginning, eDirection, 1, 1, 1);
		
		mContext = context;
		
		startPoint = entityBeginning;
		endPoint = entityDestiny;
		
		mShotStatus = Constants.SHOT_FIRED;
	
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_smoke, null));
		} else {
			setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_smoke));
		}
		
		shotWidth = mWidth;
		shotHeight = mHeight;
		
		setPathLength(getLength(startPoint, endPoint));
		
		mDamage = power;
		setTimestamp(timestampLastShot);
		
		board = new Board(context);
		board.requestFocus();
		
		setBrushProperties();
		
		mHealthPoints = 1;
		if(mHealthPoints > 0)
			setStatus(Constants.STATE_ALIVE);
	}
	
	private float getLength(Point origin, Point destiny){
		return (float) Math.hypot(destiny.x - origin.x, destiny.y - origin.y);
	}
	
	/**
	 * Set brush properties
	 */
	private void setBrushProperties() {
		mBrush = new Paint();
		mBrush.setStyle(Paint.Style.STROKE);
		mBrush.setStrokeJoin(Paint.Join.ROUND);
		mBrush.setStrokeCap(Paint.Cap.ROUND);
		mBrush.setAntiAlias(true);
		mBrush.setDither(true);
		mBrush.setColor(Color.WHITE);
		mBrush.setStrokeWidth(1);
	}
	
	public int getDamage(){
		return mDamage;
	}
	
	public void setDamage(int damage){
		this.mDamage = damage;
	}
	
	public float getPathLength() {
		return pathLength;
	}

	public void setPathLength(float pathLength) {
		this.pathLength = pathLength;
		this.setFlyingTime(1000 * pathLength);
	}

	public class Board extends View {
		private Bitmap mBitmap = null;
		private Canvas mCanvas = null;
		public Path mPath = null;
		private float mX, mY;
		private static final float TOLERANCE = 8;

		/**
		 * Constructor of the Board class
		 * @param context
		 */
		@SuppressWarnings("deprecation")
		public Board(Context context) {
			super(context);
			
			// Obtener pantalla
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				Point size = new Point();
				((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
				mBitmap = Bitmap.createBitmap(size.x, size.y,
						Bitmap.Config.ARGB_8888);
			} else {
				Display display = ((WindowManager) context
						.getSystemService(Context.WINDOW_SERVICE))
						.getDefaultDisplay();
				mBitmap = Bitmap.createBitmap(display.getWidth(),
						display.getHeight(), Bitmap.Config.ARGB_8888);
			}

			mCanvas = new Canvas(mBitmap);
			mPath = new Path();
		}

		public Bitmap getDrawnBitmap() {
			return mBitmap;
		}

		/**
		 * It starts the path on the selected coordinates
		 * 
		 * @param x
		 * @param y
		 */
		private void pathStart(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
			invalidate();
		}

		/**
		 * It adds the coordinates to the path
		 *  
		 * @param x
		 * @param y
		 */
		private void pathMove(float x, float y) {
			if (Math.abs(x - mX) >= TOLERANCE || Math.abs(y - mY) >= TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
			invalidate();
		}

		/**
		 * It ends the path
		 */
		private void pathUp() {
			if(!mPath.isEmpty()){
				mPath.lineTo(mX, mY);
				mCanvas.drawPath(mPath, mBrush);
				mPath.reset();
			}
			invalidate();
		}
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void drawOnScreen(Canvas canvas) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			switch(mShotStatus){
				case Constants.SHOT_FIRED:
					setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_smoke, null));
					board.pathStart(startPoint.x, startPoint.y);
					break;
				case Constants.SHOT_FLYING:
					setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_cannonball, null));
					board.pathMove(endPoint.x, endPoint.y);
					break;
				case Constants.SHOT_HIT:
					setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_hit, null));
					board.pathUp();
					break;
				case Constants.SHOT_MISSED:
					setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_miss, null));
					board.pathUp();
					break;
			}
		} else {
			switch(mShotStatus){
				case Constants.SHOT_FIRED:
					setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_smoke));
					board.pathStart(startPoint.x, startPoint.y);
					break;
				case Constants.SHOT_FLYING:
					setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_cannonball));
					board.pathMove(endPoint.x, endPoint.y);
					break;
				case Constants.SHOT_HIT:
					setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_hit));
					board.pathUp();
					break;
				case Constants.SHOT_MISSED:
					setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_miss));
					board.pathUp();
					break;
			}
		}
		
		// el trazo actual
		canvas.drawPath(board.mPath, mBrush);
		
		// x -= mWidth;
		// y -= mHeight;
		
		super.drawOnScreen(canvas);
	}
	
	public void setShotStatus(int shotStatus) {
		mShotStatus = shotStatus;		
	}

	public int getShotStatus() {
		return mShotStatus;
	}

	public long getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(long mTimestamp) {
		this.mTimestamp = mTimestamp;
	}
	
	public Point getEndPoint() {
		return endPoint;
	}

	public float getFlyingTime() {
		return mFlyingTime;
	}

	public void setFlyingTime(float mFlyingTime) {
		this.mFlyingTime = mFlyingTime;
	}

	public float getCurrentTime() {
		return mCurrentTime;
	}

	public void setCurrentTime(float mCurrentTime) {
		this.mCurrentTime = mCurrentTime;
	}

	@Override
	public String toString() {
		return "Shot [startPoint=" + startPoint + ", endPoint=" + endPoint
				+ ", pathLength=" + pathLength + ", mDamage=" + mDamage
				+ ", mStatus=" + mShotStatus +  ", entityDirection=" + entityDirection
				+ ", entityCoordinates=" + entityCoordinates + "]";
	}

	public boolean isInBounds(float initialHeight) {
		return x >= initialHeight && x + mWidth <= mCanvasWidth && y >= initialHeight && y + mHeight <= mCanvasHeight;
	}
	
}