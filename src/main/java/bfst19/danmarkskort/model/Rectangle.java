package bfst19.danmarkskort.model;

import java.io.Serializable;

public class Rectangle implements Serializable {
	public float xMin, yMin, xMax, yMax;

	public Rectangle(float xMin, float yMin, float xMax, float yMax) {
		if (yMax < yMin){
			throw new IllegalArgumentException("yMax (" + yMax +") less then yMin (" + yMin + ")");
		}
		else if (xMax < xMin){
			throw new IllegalArgumentException("xMax (" + xMax +") less then xMin (" + xMin + ")");
		}
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}

	public Rectangle() {
		this.xMin = 0;
		this.yMin = 0;
		this.xMax = 0;
		this.yMax = 0;
	}

	public Rectangle(Rectangle rect2) {
		this.xMin = rect2.xMin;
		this.yMin = rect2.yMin;
		this.xMax = rect2.xMax;
		this.yMax = rect2.yMax;
	}

	@Override
	public String toString() {
		return xMin + " " + yMin + " " + xMax + " " + yMax;
	}

	public boolean intersect(Rectangle rectB){
		return intersect(rectB.xMin, rectB.yMin, rectB.xMax, rectB.yMax);
	}

	public boolean intersect(float xmin, float ymin, float xmax,float ymax){
		if (this.xMin < xmax && this.xMax > xmin &&
				this.yMax > ymin && this.yMin < ymax ){
			return true;
		}
		else {
			return false;
		}
	}

	public boolean contains(Rectangle rectB){
		return contains(rectB.xMin, rectB.yMin, rectB.xMax, rectB.yMax);
	}

	public boolean contains(float xMin, float yMin, float xMax,float yMax){
		if (this.xMin < xMin && this.xMax > xMax &&
				this.yMin < yMin && this.yMax > yMax ){
			return true;
		}
		else {
			return false;
		}
	}

	public void growToEncompass(Rectangle rectangle) {
		if (rectangle.xMin < this.xMin) {
			this.xMin = rectangle.xMin;
		}
		if (rectangle.xMax > this.xMax){
			this.xMax = rectangle.xMax;
		}
		if (rectangle.yMin < this.yMin){
			this.yMin = rectangle.yMin;
		}
		if (rectangle.yMax > this.yMax){
			this.yMax = rectangle.yMax;
		}
	}

	public float euclideanDistanceSquaredTo(float x, float y){
		float resultX = 0, resultY = 0;
		if (x < xMin){
			resultX = xMin - x;
		}
		else if (x > xMax) {
			resultX = x - xMax;
		}
		if (y < yMin){
			resultY = yMin-y;
		}
		else if (y > yMax){
			resultY = y-yMax;
		}
		return resultX*resultX + resultY*resultY;
	}
}
