package bfst19.danmarkskort.model;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
	public void stroke(GraphicsContext gc, double zoomFactor);
	public void fill(GraphicsContext gc, double zoomFactor);
}
