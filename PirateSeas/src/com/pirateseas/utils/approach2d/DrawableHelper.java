package com.pirateseas.utils.approach2d;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DrawableHelper{
	
	
	/**
	 * Returns a bitmap with the upper half of the current sheet
	 * @return Upper half bitmap
	 */
	public Bitmap getFirstHalf(Drawable image){
		Bitmap bmp = ((BitmapDrawable)image).getBitmap();
		Bitmap croppedBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight() / 2);
		return croppedBmp;
	}
	
	/**
	 * Returns a bitmap with the lower half of the current sheet
	 * @return Lower half bitmap
	 */
	public Bitmap getLastHalf(Drawable image){
		Bitmap bmp = ((BitmapDrawable)image).getBitmap();
		Bitmap croppedBmp = Bitmap.createBitmap(bmp, 0, bmp.getHeight() / 2, bmp.getWidth(), bmp.getHeight() / 2);
		return croppedBmp;
	}
	
	/**
	 * Returns the received Drawable rotated x degrees
	 * @param d
	 * @return x Degrees rotated drawable
	 */
	public static Drawable rotateDrawable(Drawable d, Context context, float angle) {
		Drawable rotatedDrawable = null;
		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap rotatedBitmap = rotateBitmap(bd.getBitmap(), angle);
		BitmapDrawable tmp = new BitmapDrawable(context.getResources(),
				rotatedBitmap);
		rotatedDrawable = tmp.getCurrent();
		return rotatedDrawable;
	}
	
	/**
	 * Returns the received bitmap rotated the received degrees 
	 * @param bmp
	 * @param degrees
	 * @return Rotated bitmap
	 */
	public static Bitmap rotateBitmap(Bitmap bmp, float degrees) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);
		return rotatedBitmap;
	}

	public static double getWidth(Resources r, int drawableValue) {
		BitmapFactory.Options dimensions = new BitmapFactory.Options(); 
		dimensions.inJustDecodeBounds = true;
		@SuppressWarnings("unused")
		Bitmap mBitmap = BitmapFactory.decodeResource(r, drawableValue, dimensions);
		int width =  dimensions.outWidth;
		return width;
	}
	
	public static double getHeight(Resources r, int drawableValue) {
		BitmapFactory.Options dimensions = new BitmapFactory.Options(); 
		dimensions.inJustDecodeBounds = true;
		@SuppressWarnings("unused")
		Bitmap mBitmap = BitmapFactory.decodeResource(r, drawableValue, dimensions);
		int height =  dimensions.outHeight;
		return height;
	}
	
}