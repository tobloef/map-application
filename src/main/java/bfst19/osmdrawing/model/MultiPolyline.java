package bfst19.osmdrawing.model;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;

public class MultiPolyline extends ArrayList<Polyline> implements Drawable, Serializable {
	//TODO: Move the arraylist into a field
	public MultiPolyline(OSMRelation rel) {
		for (OSMWay way : rel) add(new Polyline(way));
	}

	@Override
	public void stroke(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.stroke();
	}

	public void trace(GraphicsContext gc) {
		for (Polyline p : this) p.trace(gc);
	}

	@Override
	public void fill(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.fill();
	}
}
