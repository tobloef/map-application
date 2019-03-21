package bfst19.osmdrawing.model;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultiPolyline implements Drawable, Serializable {
	List<Polyline> list;
	public MultiPolyline(OSMRelation rel) {
		rel.merge();
		list = new ArrayList<>();
		for (OSMWay way : rel.getList()) list.add(new Polyline(way));
	}

	@Override
	public void stroke(GraphicsContext graphicsContext, double zoomFactor) {
		graphicsContext.beginPath();
		trace(graphicsContext, zoomFactor);
		graphicsContext.stroke();
	}

	public void trace(GraphicsContext graphicsContext, double zoomFactor) {
		for (Polyline polyline : list) {
			polyline.trace(graphicsContext, zoomFactor);
		}
	}

	@Override
	public void fill(GraphicsContext graphicsContext, double zoomFactor) {
		graphicsContext.beginPath();
		trace(graphicsContext, zoomFactor);
		graphicsContext.fill();
	}
}
