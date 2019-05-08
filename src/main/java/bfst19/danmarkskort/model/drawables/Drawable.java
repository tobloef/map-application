package bfst19.danmarkskort.model.drawables;

import bfst19.danmarkskort.model.drawableModel.Rectangle;
import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
    float sizeNonDrawFactor = 50;

    static boolean checkForSize(Rectangle minimumBoundingRectangle, double zoomFactor) {
        if (minimumBoundingRectangle.getSize() < zoomFactor*zoomFactor * sizeNonDrawFactor)
            return true;
        return false;
    }
    void stroke(GraphicsContext gc, double zoomFactor);

    void fill(GraphicsContext gc, double zoomFactor);

    long getNumOfFloats();
}
