package bfst19.osmdrawing;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.NonInvertibleTransformException;

public class MapDrawer implements Drawer {
	private Canvas canvas;
	private GraphicsContext graphicsContext;
	private Model model;

	public MapDrawer(Canvas canvas, Model model) {
		this.canvas = canvas;
		this.graphicsContext = canvas.getGraphicsContext2D();
		this.model = model;
	}

	public void draw() {
		fillBackground();
		for (WayType wayType : WayType.values()){
			if (wayType.hasFill()) {
				graphicsContext.setFill(wayType.getFillColor());
				for (Drawable way : model.getWaysOfType(wayType, getModelBounds())) {
					way.fill(graphicsContext);
				}
			}
			if (wayType.hasStroke()) {
				graphicsContext.setLineDashes(wayType.getLineDash() / 10000);
				graphicsContext.setStroke(wayType.getStrokeColor());
				for (Drawable way : model.getWaysOfType(wayType, getModelBounds())){
					way.stroke(graphicsContext);
				}
			}
		}
	}

	private void fillBackground() {
		if (model.getWaysOfType(WayType.COASTLINE, getModelBounds()).iterator().hasNext()) {
			graphicsContext.setFill(WayType.WATER.getFillColor());
		} else {
			graphicsContext.setFill(WayType.COASTLINE.getFillColor());
		}
		graphicsContext.fillRect(0, 0, 4000, 4000);
	}
	private Rectangle getModelBounds(){
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
