package bfst19.osmdrawing.model;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultiPolyline implements Drawable, Serializable {
	List<Polyline> list;
	public MultiPolyline(OSMRelation rel) {
		list = new ArrayList<>();
		for (OSMWay way : rel.getList()) list.add(new Polyline(way));
	}

	@Override
	public void stroke(GraphicsContext graphicsContext) {
		graphicsContext.beginPath();
		trace(graphicsContext);
		graphicsContext.stroke();
	}

	public void trace(GraphicsContext graphicsContext) {
		for (Polyline polyline : list) {
			polyline.trace(graphicsContext);
		}
	}

	@Override
	public void fill(GraphicsContext graphicsContext) {
		graphicsContext.beginPath();
		trace(graphicsContext);
		graphicsContext.fill();
	}

	@Override
	public float getCenterX() {
		//TODO: Make something better for these, this is merely for testing.
		return list.get(0).getCenterX();
	}

	@Override
	public float getCenterY() {
		//TODO: Make something better for these, this is merely for testing.
		return list.get(0).getCenterY();
	}
}
