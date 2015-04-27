package com.pirateseas.utils.approach2d;

import android.graphics.Point;

public class Geometry{
	
	public static float getRotationAngle(Point start, Point center, Point end){
		double angleRad = 0;
		float degrees = 0f;
		float angle = 0f;
		int quad = 0;		// Exclusively 1, 2, 3 or 4
		
		Point u = new Point(start.x - center.x, start.y - center.y);
		Point v = new Point(end.x - center.x, end.y - center.y);
		double modU = Math.hypot(u.x, u.y);
		double modV = Math.hypot(v.x, v.y);
		
		// u* v = mod(u) * mod(v) * cos(alpha)
		// cos(alpha) = (u * v) / (mod(u) * mod(v))
		// u = center - start
		// v = center - end
		
		angleRad = Math.acos((u.x * v.x + u.y * v.y) / (modU * modV));
		degrees = (float) Math.toDegrees(angleRad);
		
		// Obtain quadrant
		quad = getQuadrant(center, end);
		
		// Modify angle value with quad
		switch(quad){
			case 1:
				angle = 0 + degrees;
				break;
			case 2:
				angle = 90 + degrees;
				break;
			case 3:
				angle = 180 + degrees;
				break;
			case 4:
				angle = 270 + degrees;
				break;
		}
		
		return angle;
	}
	
	private static int getQuadrant(Point center, Point checker) {
		int quad = 0;
		if(checker.x > center.x){
			if(checker.y > center.y){
				quad = 1;
			} else {
				quad = 4;
			}
		} else {
			if(checker.y > center.y){
				quad = 2;
			} else {
				quad = 3;
			}
		}
		return quad;
	}

	public static class Point3D{
		public final float x, y, z;

        public Point3D(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }   
        
        public Point3D translateY(float distance) {
            return new Point3D(x, y + distance, z);
        }
		
		public Point3D translate(Vector3D vector) {
            return new Point3D(
                x + vector.x, 
                y + vector.y, 
                z + vector.z);
        }
	}
	
	public static class Vector3D  {
        public final float x, y, z;

        public Vector3D(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
		
		public Vector3D(Vector3D other) {
            this.x = other.x;
            this.y = other.y;
            this.z = other.z;
        }
		
		public Vector3D(Point3D orig, Point3D dest){
			this.x = dest.x - orig.x;
			this.y = dest.y - orig.y;
			this.z = dest.z - orig.z;
		}
        
        public float length() {
            return (float) Math.sqrt(
                x * x 
              + y * y 
              + z * z);
        }
		
		public Vector3D getOrtogonalVector(Point3D p){
			return null;			
		}
		
		public float getAngle(Vector3D other){
			return (float) Math.acos(dotProduct(other)/(length() * other.length()));
		}
        
        // http://en.wikipedia.org/wiki/Cross_product        
        public Vector3D crossProduct(Vector3D other) {
            return new Vector3D(
                (y * other.z) - (z * other.y), 
                (z * other.x) - (x * other.z), 
                (x * other.y) - (y * other.x));
        }
        // http://en.wikipedia.org/wiki/Dot_product
        public float dotProduct(Vector3D other) {
            return x * other.x 
                 + y * other.y 
                 + z * other.z;
        }
        
        public Vector3D scale(float f) {
            return new Vector3D(
                x * f, 
                y * f, 
                z * f);
        }     
    }
}