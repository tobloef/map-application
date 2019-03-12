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
	public void stroke(GraphicsContext graphicsContext) {
		graphicsContext.beginPath();
		trace(graphicsContext);
		graphicsContext.stroke();
	}

	public void trace(GraphicsContext graphicsContext) {

		for (Polyline polyline : this) {
			polyline.trace(graphicsContext);
		}
	}

	@Override
	public void fill(GraphicsContext graphicsContext) {
		graphicsContext.beginPath();
		trace(graphicsContext);
		graphicsContext.fill();
	}
}
