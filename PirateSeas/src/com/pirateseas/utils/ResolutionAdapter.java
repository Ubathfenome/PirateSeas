package com.pirateseas.utils;

/**
 * Class to improve the view with different resolutions
 * @author Miguel
 *
 */
public class ResolutionAdapter {

	private int screenWidth;
	private int screenHeight;
	private double factorX;
	private double factorY;

	/**
	 * Constructor
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 */
	public ResolutionAdapter(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		instanceFactor();
	}

	/**
	 * Set the default factor for the views 
	 */
	private void instanceFactor() {
		factorY = screenHeight / 1024d;
		factorX = screenWidth / 768d;
	}

	/**
	 * Scale the y-coordinate
	 * 
	 * @param y
	 * @return Scaled coordinate
	 */
	public double y(double y) {
		return (y * factorY);
	}

	/**
	 * Scale the x-coordinate
	 * 
	 * @param x
	 * @return Scaled coordinate
	 */
	public double x(double x) {
		return (x * factorX);
	}

	/**
	 * Scale the height
	 * 
	 * @param height
	 * @return Scaled height
	 */
	public int height(int height) {
		if (factorX < factorY) {
			return (int) (height * factorX);
		} else {
			return (int) (height * factorY);
		}
	}

	/**
	 * Scale the width
	 * 
	 * @param width
	 * @return Scaled width
	 */
	public int width(int width) {
		if (factorY < factorX) {
			return (int) (width * factorY);
		} else {
			return (int) (width * factorX);
		}
	}
}
