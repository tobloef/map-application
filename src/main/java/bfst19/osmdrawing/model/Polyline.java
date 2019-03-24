package bfst19.osmdrawing.model;

import bfst19.osmdrawing.model.parsing.OSMWay;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Polyline implements Drawable, Serializable, SpatialIndexable {
	private float[] coords;

	public Polyline(OSMWay way) {
		coords = new float[way.size() * 2];
		for (int i = 0 ; i < way.size() ; i++) {
			coords[2*i] = way.get(i).getLon();
			coords[2*i+1] = way.get(i).getLat();
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

		for (int i = 2; i < coords.length ; i+=2) {
			traceWithoutSubpixel(coords[i], coords[i+1], zoomFactor, gc, lastCoords);
		}
	}

	public void fill(GraphicsContext gc, double zoomFactor) {
		gc.beginPath();
		trace(gc, zoomFactor);
		gc.fill();
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
		Rectangle rectangle = new Rectangle(coords[0],  coords[1], coords[0], coords[1]);
		for (int i = 2; i < coords.length ; i+=2) {
			if (rectangle.xMin > coords[i]) {
				rectangle.xMin = coords[i];
			}
			else if (rectangle.xMax < coords[i]) {
				rectangle.xMax = coords[i];
			}
			if (rectangle.yMin > coords[i+1]) {
				rectangle.yMin = coords[i+1];
			}
			else if (rectangle.yMax < coords[i+1]) {
				rectangle.yMax = coords[i+1];
			}
		}
		return rectangle;
	}

	public void traceWithoutSubpixel(float x, float y, double zoomFactor, GraphicsContext gc, float lastCoords[]){
		if(Math.abs(lastCoords[0] - x) > zoomFactor || Math.abs(lastCoords[1] - y) > zoomFactor){
			gc.lineTo(x, y);
			lastCoords[0] = x;
			lastCoords[1] = y;
		}
	}

}
