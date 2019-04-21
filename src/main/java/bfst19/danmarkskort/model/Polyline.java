package bfst19.danmarkskort.model;

import bfst19.danmarkskort.model.parsing.OSMWay;
import javafx.scene.canvas.GraphicsContext;
import java.io.Serializable;

public class Polyline implements Drawable, Serializable, SpatialIndexable {
	float[] coords;
	private float xMin, yMin, xMax, yMax;

	public Polyline(OSMWay way) {
		coords = new float[way.size() * 2];
		for (int i = 0 ; i < way.size() ; i++) {
			coords[2*i] = way.get(i).getLon();
			coords[2*i+1] = way.get(i).getLat();
		}
		createMinimumBoundingRectangle();

	}

	private void createMinimumBoundingRectangle() {
		xMin = coords[0];
		xMax = coords[0];
		yMin = coords[1];
		yMax = coords[1];
		for (int i = 2; i < coords.length ; i+=2) {
			if (xMin > coords[i]) {
				xMin = coords[i];
			}
			else if (xMax < coords[i]) {
				xMax = coords[i];
			}
			if (yMin > coords[i+1]) {
				yMin = coords[i+1];
			}
			else if (yMax < coords[i+1]) {
				yMax = coords[i+1];
			}
		}
	}

	public void stroke(GraphicsContext gc, double zoomFactor) {
		gc.beginPath();
		trace(gc, zoomFactor);
		gc.stroke();
	}

	public void trace(GraphicsContext gc, double zoomFactor) {
		gc.moveTo(coords[0], coords[1]);
		float[] lastCoords = new float[2];
		lastCoords[0] = coords[0];
		lastCoords[1] = coords[1];
		for (int i = 2; i < coords.length -2; i+=2) {
			traceWithoutSubpixel(coords[i], coords[i+1], zoomFactor, gc, lastCoords);
		}
		gc.lineTo(coords[coords.length - 2], coords[coords.length - 1]);
	}

	public void fill(GraphicsContext gc, double zoomFactor) {
		gc.beginPath();
		trace(gc, zoomFactor);
		gc.fill();
	}

	@Override
	public float euclideanDistanceSquaredTo(float x, float y) {
		float minimumDistance = Float.POSITIVE_INFINITY;
		for (int i = 0; i < coords.length; i+=2){
			float tempDistance = distance(x - coords[i], y - coords[i+1]);
			if (tempDistance < minimumDistance){
				minimumDistance = tempDistance;
			}
		}
		return minimumDistance;
	}

	private static float distance(float x, float y) {
		return x * x + y * y;
	}

	@Override
	public long getNumOfFloats() {
		return coords.length;
	}

	@Override
	public float getRepresentativeX() {
		//TODO: Make something more representative then just the first coords.
		return coords[0];
	}

	@Override
	public float getRepresentativeY() {
		//TODO: Make something more representative then just the first coords.
		return coords[1];
	}

	@Override
	public Rectangle getMinimumBoundingRectangle() { //TODO:Write a test for this function.
		return new Rectangle(xMin,  yMin, xMax, yMax);
	}

	public void traceWithoutSubpixel(float x, float y, double zoomFactor, GraphicsContext gc, float lastCoords[]){
		if(Math.abs(lastCoords[0] - x) > zoomFactor || Math.abs(lastCoords[1] - y) > zoomFactor){
			gc.lineTo(x, y);
			lastCoords[0] = x;
			lastCoords[1] = y;
		}
	}

}