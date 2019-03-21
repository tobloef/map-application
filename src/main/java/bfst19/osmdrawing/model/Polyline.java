package bfst19.osmdrawing.model;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Polyline implements Drawable, Serializable {
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

	public void traceWithoutSubpixel(float x, float y, double zoomFactor, GraphicsContext gc, float lastCoords[]){
		if(Math.abs(lastCoords[0] - x) > zoomFactor || Math.abs(lastCoords[1] - y) > zoomFactor){
			gc.lineTo(x, y);
			lastCoords[0] = x;
			lastCoords[1] = y;
		}
	}

}
