package com.pirateseas.utils;

import android.util.FloatMath;

public class Geometry{
	public static class Point{
		public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }   
        
        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }
		
		public Point translate(Vector vector) {
            return new Point(
                x + vector.x, 
                y + vector.y, 
                z + vector.z);
        }
	}
	
	public static class Vector  {
        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
		
		public Vector(Vector other) {
            this.x = other.x;
            this.y = other.y;
            this.z = other.z;
        }
		
		public Vector(Point orig, Point dest){
			this.x = dest.x - orig.x;
			this.y = dest.y - orig.y;
			this.z = dest.z - orig.z;
		}
        
        public float length() {
            return FloatMath.sqrt(
                x * x 
              + y * y 
              + z * z);
        }
		
		public Vector getOrtogonalVector(Point p){
			return null;			
		}
		
		public float getAngle(Vector other){
			return (float) Math.acos(dotProduct(other)/(length() * other.length()));
		}
        
        // http://en.wikipedia.org/wiki/Cross_product        
        public Vector crossProduct(Vector other) {
            return new Vector(
                (y * other.z) - (z * other.y), 
                (z * other.x) - (x * other.z), 
                (x * other.y) - (y * other.x));
        }
        // http://en.wikipedia.org/wiki/Dot_product
        public float dotProduct(Vector other) {
            return x * other.x 
                 + y * other.y 
                 + z * other.z;
        }
        
        public Vector scale(float f) {
            return new Vector(
                x * f, 
                y * f, 
                z * f);
        }     
    }
	
}