package com.pirateseas.model.canvasmodel.game.entity;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

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
	
	private int mDamage;
	
	private int mStatus;
	
	protected Paint mBrush = null;
	protected Board board = null;
	
	public Shot(Context context, double x, double y, double mCanvasWidth, double mCanvasHeight, Point destiny){
		super(context, x, y, mCanvasWidth, mCanvasHeight, new Point((int)x, (int)y), 1, 1, 1);
		
		mContext = context;
		
		startPoint = new Point((int)x + entityWidth, (int)y + entityLength);
		endPoint = destiny;
		
		setPathLength(getLength(startPoint, endPoint));
		
		mDamage = 10;
		
		board = new Board(context);
		board.requestFocus();
		
		// CanvasView.addView(board);
		
		setBrushProperties();
		
		mHealthPoints = 1;
		if(mHealthPoints > 0)
			setStatus(Constants.STATE_ALIVE);
	}
	
	private float getLength(Point origin, Point destiny){
		return (float) Math.sqrt(Math.pow(destiny.x - origin.x, 2)+Math.pow(destiny.y - origin.y, 2));
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
	
	@Override
	public void drawOnScreen(Canvas canvas) {
		
		switch(mStatus){
			case Constants.SHOT_FIRED:
				setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_smoke));
				board.pathStart(startPoint.x, startPoint.y);
				break;
			case Constants.SHOT_FLYING:
				setImage(mContext.getResources().getDrawable(R.drawable.txtr_shot_cannonball));
				board.pathMove(1 / endPoint.x, 1 / endPoint.y);
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
		
		// el trazo actual
		canvas.drawPath(board.mPath, mBrush);
	}

	@Override
	public String toString() {
		return "Shot [startPoint=" + startPoint + ", endPoint=" + endPoint
				+ ", pathLength=" + pathLength + ", mDamage=" + mDamage
				+ ", mStatus=" + mStatus +  ", entityDirection=" + entityDirection
				+ ", entityCoordinates=" + entityCoordinates + "]";
	}
	
	
	
}