package bfst19.danmarkskort.model.drawables;

import bfst19.danmarkskort.model.drawableModel.Rectangle;
import bfst19.danmarkskort.model.drawableModel.SpatialIndexable;
import bfst19.danmarkskort.model.osmParsing.OSMWay;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Polyline implements Drawable, Serializable, SpatialIndexable {
    final float[] coords;
    private float xMin, yMin, xMax, yMax;

    public Polyline(OSMWay way) {
        coords = new float[way.size() * 2];
        for (int i = 0; i < way.size(); i++) {
            coords[2 * i] = way.get(i).getLon();
            coords[2 * i + 1] = way.get(i).getLat();
        }
        createMinimumBoundingRectangle();
    }

    private static float distance(float x, float y) {
        return x * x + y * y;
    }

    private void createMinimumBoundingRectangle() {
        xMin = coords[0];
        xMax = coords[0];
        yMin = coords[1];
        yMax = coords[1];
        for (int i = 2; i < coords.length; i += 2) {
            if (xMin > coords[i]) {
                xMin = coords[i];
            } else if (xMax < coords[i]) {
                xMax = coords[i];
            }
            if (yMin > coords[i + 1]) {
                yMin = coords[i + 1];
            } else if (yMax < coords[i + 1]) {
                yMax = coords[i + 1];
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
        for (int i = 2; i < coords.length - 2; i += 2) {
            traceWithoutSubpixel(coords[i], coords[i + 1], zoomFactor, gc, lastCoords);
        }
        gc.lineTo(coords[coords.length - 2], coords[coords.length - 1]);
    }

    public void fill(GraphicsContext gc, double zoomFactor) {
        if (Drawable.checkForSize(getMinimumBoundingRectangle(), zoomFactor)) return;
        gc.beginPath();
        trace(gc, zoomFactor);
        gc.fill();
    }

    @Override
    public float euclideanDistanceSquaredTo(float x, float y) {
        float minimumDistance = Float.POSITIVE_INFINITY;
        for (int i = 0; i < coords.length; i += 2) {
            float tempDistance = distance(x - coords[i], y - coords[i + 1]);
            if (tempDistance < minimumDistance) {
                minimumDistance = tempDistance;
            }
        }
        return minimumDistance;
    }

    @Override
    public long getNumOfFloats() {
        return coords.length;
    }

    @Override
    public float getRepresentativeX() {
		float sum = 0;
		for (int i = 0; i < coords.length / 2; i++) {
			sum += coords[i*2];
		}
        return sum / (coords.length / 2);
    }

    @Override
    public float getRepresentativeY() {
		float sum = 0;
		for (int i = 0; i < coords.length / 2; i++) {
			sum += coords[i*2+1];
		}
		return sum / (coords.length / 2);
    }

    @Override
    public Rectangle getMinimumBoundingRectangle() { //TODO:Write a test for this function.
        return new Rectangle(xMin, yMin, xMax, yMax);
    }

    public void traceWithoutSubpixel(float x, float y, double zoomFactor, GraphicsContext gc, float[] lastCoords) {
        if (Math.abs(lastCoords[0] - x) > zoomFactor || Math.abs(lastCoords[1] - y) > zoomFactor) {
            gc.lineTo(x, y);
            lastCoords[0] = x;
            lastCoords[1] = y;
        }
    }

}