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

	public void stroke(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.stroke();
	}

	public void trace(GraphicsContext gc) {
		gc.moveTo(coords[0], coords[1]);
		for (int i = 2; i < coords.length ; i+=2) {
			gc.lineTo(coords[i], coords[i+1]);
		}
	}

	public void fill(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.fill();
	}

	@Override
	public float getCenterX() {
		//TODO: Make something better for these, this is merely for testing.
		return coords[0];
	}

	@Override
	public float getCenterY() {
		//TODO: Make something better for these, this is merely for testing.
		return coords[1];
	}


}
