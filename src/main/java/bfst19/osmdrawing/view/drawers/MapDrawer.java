package bfst19.osmdrawing.view.drawers;

import bfst19.osmdrawing.model.Drawable;
import bfst19.osmdrawing.model.Model;
import bfst19.osmdrawing.model.Rectangle;
import bfst19.osmdrawing.view.WayType;
import bfst19.osmdrawing.view.controls.MapCanvas;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
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
		double defaultLineWidth = graphicsContext.getLineWidth();

		for (WayType wayType : WayType.values()){
			if (visibleAtCurrentZoom(wayType)) {
				if (wayType.hasFill()) {
					fillWays(wayType);
				}
				if (wayType.hasStroke()) {
					strokeWays(defaultLineWidth, wayType);
				}
			}
		}
	}

	private void strokeWays(double defaultLineWidth, WayType wayType) {
		graphicsContext.setLineDashes(wayType.getLineDash() / 10000);
		graphicsContext.setStroke(wayType.getStrokeColor());
		if (wayType.hasLineWidth()) {
			graphicsContext.setLineWidth(wayType.getLineWidth());
		}
		for (Drawable way : model.getWaysOfType(wayType, getScreenBounds())) {
			way.stroke(graphicsContext, canvas.getDegreesLatitudePerPixel());
		}
		graphicsContext.setLineWidth(defaultLineWidth);
	}

	private void fillWays(WayType wayType) {
		graphicsContext.setFill(wayType.getFill());
		for (Drawable way : model.getWaysOfType(wayType, getScreenBounds())) {
			way.fill(graphicsContext, canvas.getDegreesLatitudePerPixel());
		}
	}

	private boolean visibleAtCurrentZoom(WayType wayType) {
		return wayType.getZoomLevel() > canvas.getDegreesLatitudePerPixel();
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
		return new Rectangle((float)min.getX(), (float)max.getY(), (float)max.getX(), (float)min.getY()); //needed because the model is flipped
	}

	//Test function to visualize if the KDTree works.
	private Rectangle getSmallModelBounds(){
		int boxsize = 100;
		Bounds bounds = canvas.getBoundsInLocal();
		Point2D min = modelCoords(bounds.getMinX() + bounds.getMaxX()/2 - boxsize, bounds.getMinY()+ bounds.getMaxY()/2 - boxsize);
		Point2D max = modelCoords(bounds.getMaxX()/2 + boxsize, bounds.getMaxY()/2 + boxsize);
		return new Rectangle((float)min.getX(), (float)max.getY(), (float)max.getX(), (float)min.getY()); //needed because the model is flipped
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
