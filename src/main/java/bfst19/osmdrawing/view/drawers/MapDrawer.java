package bfst19.osmdrawing.view.drawers;

import bfst19.osmdrawing.model.Drawable;
import bfst19.osmdrawing.model.Model;
import bfst19.osmdrawing.model.Rectangle;
import bfst19.osmdrawing.view.WayType;
import bfst19.osmdrawing.view.controls.MapCanvas;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MapDrawer implements Drawer {
	private MapCanvas canvas;
	private GraphicsContext graphicsContext;
	private Model model;

	public MapDrawer(MapCanvas canvas, Model model) {
		this.canvas = canvas;
		this.graphicsContext = canvas.getGraphicsContext2D();
		this.model = model;
	}

	public void draw() {
		fillBackground();
		for (WayType wayType : WayType.values()){
			if (wayType.hasFill()) {
				graphicsContext.setFill(wayType.getFill());
				for (Drawable way : model.getWaysOfType(wayType, getScreenBounds())) {
					if (visibleAtCurrentZoom(wayType)) {
						way.fill(graphicsContext);
					}
				}
			}
			if (wayType.hasStroke()) {
				graphicsContext.setLineDashes(wayType.getLineDash() / 10000);
				graphicsContext.setStroke(wayType.getStrokeColor());
				for (Drawable way : model.getWaysOfType(wayType, getScreenBounds())){
					if (visibleAtCurrentZoom(wayType)){
						way.stroke(graphicsContext);
					}
				}
			}
		}
	}

	private boolean visibleAtCurrentZoom(WayType wayType) {
		return wayType.getZoomLevel() > canvas.getDegreesPerPixel();
	}

	private void fillBackground() {
		if (model.getWaysOfType(WayType.COASTLINE, getScreenBounds()).iterator().hasNext()) {
			graphicsContext.setFill(WayType.WATER.getFill());
		} else {
			graphicsContext.setFill(WayType.COASTLINE.getFill());
		}
		Affine affine = graphicsContext.getTransform();
		graphicsContext.setTransform(new Affine());
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		graphicsContext.setTransform(affine);
	}
	private Rectangle getScreenBounds(){
		Bounds bounds = canvas.getBoundsInLocal();
		Point2D min = modelCoords(bounds.getMinX(), bounds.getMinY());
		Point2D max = modelCoords(bounds.getMaxX(), bounds.getMaxY());
		return new Rectangle((float)min.getX(), (float)min.getY(), (float)max.getX(), (float)max.getY());
	}

	public Point2D modelCoords(double x, double y) {
		try {
			return graphicsContext.getTransform().inverseTransform(x, y);
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
			return null;
		}
	}
}
