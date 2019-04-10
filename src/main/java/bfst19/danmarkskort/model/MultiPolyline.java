package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMRelation;
import bfst19.danmarkskort.model.parsing.OSMWay;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultiPolyline implements Drawable, Serializable, SpatialIndexable {
	List<Polyline> list;
	private float xMin, yMin, xMax, yMax;
	public MultiPolyline(OSMRelation rel) {
		rel.merge();
		list = new ArrayList<>();
		for (OSMWay way : rel.getList()){
			list.add(new Polyline(way));
		}
		createMinimumBoundingRectangle();
	}

	private void createMinimumBoundingRectangle() {
		if (list.size() == 0){
			return;
		}
		Rectangle boundingRectangle = list.get(0).getMinimumBoundingRectangle();
		for (Polyline line : list){
			boundingRectangle.growToEncompass(line.getMinimumBoundingRectangle());
		}
		this.xMin = boundingRectangle.xMin;
		this.yMin = boundingRectangle.yMin;
		this.xMax = boundingRectangle.xMax;
		this.yMax = boundingRectangle.yMax;
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
	public float euclideanDistanceSquaredTo(float x, float y) {
		float minDistance = Float.POSITIVE_INFINITY;
		for (Polyline polyline : list) {
			float tempDistance = polyline.euclideanDistanceSquaredTo(x, y);
			if (tempDistance < minDistance) {
				minDistance = tempDistance;
			}
		}
		return minDistance;
	}

	@Override
	public long getNumOfFloats() {
		long numOfFloats = 0;
		for (Polyline polyline : list){
			numOfFloats += polyline.getNumOfFloats();
		}
		return numOfFloats;
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
		return new Rectangle(xMin,yMin,xMax,yMax);
	}


}
