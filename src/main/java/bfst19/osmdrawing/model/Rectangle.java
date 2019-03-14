package bfst19.osmdrawing.model;

import java.io.Serializable;
import java.util.Collection;

public class Rectangle implements Serializable {
	public float xmin, ymin, xmax, ymax;

	public Rectangle(float xmin, float ymin, float xmax, float ymax) {
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}

	public Rectangle() {
		//Empty on purpose
	}

	@Override
	public String toString() {
		return "" + xmin + " " + ymin + " " + xmax + " " + ymax;
	}

	public boolean intersect(Rectangle rectB){
		return intersect(rectB.xmin, rectB.ymin, rectB.xmax, rectB.ymax);
	}

	public boolean intersect(float xmin, float ymin, float xmax,float ymax){
		if (this.xmin < xmax && this.xmax > xmin &&
				this.ymax > ymin && this.ymin < ymax ){
			return true;
		}
		else {
			return false;
		}
	}

	public boolean contains(Rectangle rectB){
		return contains(rectB.xmin, rectB.ymin, rectB.xmax, rectB.ymax);
	}

	public boolean contains(float xmin, float ymin, float xmax,float ymax){
		if (this.xmin < xmin && this.xmax > xmax &&
				this.ymin < ymin && this.ymax > ymax ){
			return true;
		}
		else {
			return false;
		}
	}
}
