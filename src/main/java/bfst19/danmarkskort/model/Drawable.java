package bfst19.danmarkskort.model;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
    void stroke(GraphicsContext gc, double zoomFactor);

    void fill(GraphicsContext gc, double zoomFactor);

    long getNumOfFloats();
}
