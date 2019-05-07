package bfst19.danmarkskort.model;

import bfst19.danmarkskort.utils.ResourceLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.Serializable;

public class PointOfInterest implements Drawable, SpatialIndexable, Serializable {
    private static String iconUrl = "rs:textures/poi.png";
    private static float imgFactor = 0.05f;
    private static Image iconImg = new Image(ResourceLoader.getResourceAsStream(iconUrl));
    private float xCoord = 0, yCoord = 0;


    public PointOfInterest(float xCoordinate, float yCoordinate) {
        this.xCoord = xCoordinate;
        this.yCoord = yCoordinate;
    }

    public float getRepresentativeX() {
        return xCoord;
    }

    public float getRepresentativeY() {
        return yCoord;
    }

    public Rectangle getMinimumBoundingRectangle() {
        return new Rectangle(xCoord, yCoord, xCoord, yCoord);
    }

    @Override
    public float euclideanDistanceSquaredTo(float x, float y) {
        return (x - xCoord) * (x - xCoord) + (y - yCoord) * (y - yCoord);
    }

    @Override
    public void stroke(GraphicsContext gc, double zoomFactor) {

    }

    @Override
    public void fill(GraphicsContext gc, double zoomFactor) {
        float imgWidth = (float) (iconImg.getWidth() * zoomFactor) * imgFactor;
        float imgHeight = (float) (iconImg.getHeight() * zoomFactor) * imgFactor;
        float centerX = imgWidth / 2;
        gc.drawImage(iconImg, xCoord - centerX, yCoord + imgHeight, imgWidth, -imgHeight);
        gc.fill();
    }

    @Override
    public long getNumOfFloats() {
        return 1;
    }
}