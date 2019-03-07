package bfst19.osmdrawing.Model;

import java.io.Serializable;

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
}
