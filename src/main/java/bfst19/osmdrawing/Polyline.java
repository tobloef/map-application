package bfst19.osmdrawing;

import bfst19.osmdrawing.Model.OSMWay;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Polyline implements Drawable, Serializable {
	private float[] coord;

	public Polyline(OSMWay way) {
		coord = new float[way.size() * 2];
		for (int i = 0 ; i < way.size() ; i++) {
			coord[2*i] = way.get(i).getLon();
			coord[2*i+1] = way.get(i).getLat();
		}
	}

	public void stroke(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.stroke();
	}

	public void trace(GraphicsContext gc) {
		gc.moveTo(coord[0], coord[1]);
		for (int i = 2 ; i < coord.length ; i+=2) {
			gc.lineTo(coord[i], coord[i+1]);
		}
	}

	public void fill(GraphicsContext gc) {
		gc.beginPath();
		trace(gc);
		gc.fill();
	}


}
