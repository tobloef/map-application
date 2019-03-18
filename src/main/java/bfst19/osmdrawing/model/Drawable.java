package bfst19.osmdrawing.model;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable{
	public void stroke(GraphicsContext gc);
	public void fill(GraphicsContext gc);

}
