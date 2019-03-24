package bfst19.osmdrawing.model;

import bfst19.osmdrawing.model.parsing.OSMRelation;
import bfst19.osmdrawing.model.parsing.OSMWay;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultiPolyline implements Drawable, Serializable, SpatialIndexable {
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

	@Override
	public float getRepresentativeX() {
		//TODO: Make something more representative then just the first coords.
		return list.get(0).getRepresentativeX();
	}

	@Override
	public float getRepresentativeY() {
		//TODO: Make something more representative then just the first coords.
		return list.get(0).getRepresentativeY();
	}

	@Override
	public Rectangle getMinimumBoundingRectangle() {
		Rectangle rectangle = new Rectangle();
		for (Polyline polyline : list) {
			rectangle.growToEncompass(polyline.getMinimumBoundingRectangle());
		}
		return rectangle;
	}
}
