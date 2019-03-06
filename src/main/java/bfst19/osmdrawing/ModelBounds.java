package bfst19.osmdrawing;

public class ModelBounds {
	double xmin, ymin, xmax, ymax;

	public ModelBounds(double xmin, double ymin, double xmax, double ymax) {
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}

	@Override
	public String toString() {
		return "" + xmin + " " + ymin + " " + xmax + " " + ymax;
	}
}
