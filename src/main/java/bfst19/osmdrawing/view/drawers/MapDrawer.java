package bfst19.osmdrawing.view.drawers;

import bfst19.osmdrawing.model.*;
import bfst19.osmdrawing.model.Drawable;
import bfst19.osmdrawing.model.Model;
import bfst19.osmdrawing.model.Rectangle;
import bfst19.osmdrawing.view.controls.MapCanvas;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.Map;

import static bfst19.osmdrawing.utils.LoadDrawingInfoMap.loadDrawingInfoMap;

public class MapDrawer implements Drawer {
	private MapCanvas canvas;
	private GraphicsContext graphicsContext;
	private Model model;
	private Map<WayType, DrawingInfo> drawableDrawingInfoMap;

	public MapDrawer(MapCanvas canvas, Model model) {
		this.canvas = canvas;
		this.graphicsContext = canvas.getGraphicsContext2D();
		this.model = model;
		// TODO: This should be done somewhere else
		String defaultPath = "config/themes/default.yaml";
		try {
			drawableDrawingInfoMap = loadDrawingInfoMap(defaultPath, null);
		} catch (YAMLException e) {
			throw new RuntimeException("Couldn't read default theme from path \"" + defaultPath + "\", can't continue.", e);
		}
	}

	public void draw() {
		if (drawableDrawingInfoMap == null) {
			return;
		}
		double currentZoomLevel = canvas.getDegreesLatitudePerPixel();
		fillBackground(drawableDrawingInfoMap);
		for (WayType wayType : WayType.values()){
			Iterable<Drawable> drawablesToDraw = model.getWaysOfType(wayType, getScreenBounds());
			// Skip if no drawables to draw
			if (!drawablesToDraw.iterator().hasNext()) {
				continue;
			}
			// Skip if no theme found
			DrawingInfo drawingInfo = drawableDrawingInfoMap.get(wayType);
			if (drawingInfo == null) {
				continue;
			}
			boolean visibleAtZoom = isVisibleAtZoom(drawingInfo, currentZoomLevel);
			if (!visibleAtZoom) {
				continue;
			}
			drawDrawables(drawablesToDraw, drawingInfo, currentZoomLevel);
		}
	}

	private boolean isVisibleAtZoom(DrawingInfo drawingInfo, double zoomLevel) {
		// If no zoom level specified, just draw it.
		if (!drawingInfo.hasZoomLevel()) {
			return true;
		}
		// If the element should be visible at the current zoom level
		if (drawingInfo.getZoomLevel() > zoomLevel) {
			return true;
		}
		// If the element should always be drawn, draw it regardless of zoom.
		return drawingInfo.getAlwaysDraw();
	}

	private void drawDrawables(Iterable<Drawable> drawables, DrawingInfo drawingInfo, double currentZoomLevel) {
		graphicsContext.save();
		Paint fill = null;
		if (drawingInfo.hasFillColor()) {
			fill = drawingInfo.getFillColor();
		}
		// TODO: Add clause to see if textures are enabled.
		if (drawingInfo.hasTexture()) {
			fill = drawingInfo.getTexture();
		}
		if (fill != null) {
			graphicsContext.setFill(fill);
			for (Drawable drawable : drawables) {
				drawable.fill(graphicsContext, currentZoomLevel);
			}
		}
		if (drawingInfo.hasStrokeColor()) {
			graphicsContext.setStroke(drawingInfo.getStrokeColor());

			if (drawingInfo.hasLineDash()) {
				graphicsContext.setLineDashes(drawingInfo.getLineDash() / 10000);
			}
			if(drawingInfo.hasLineWidth()){
				graphicsContext.setLineWidth(drawingInfo.getLineWidth());
			}
			for (Drawable drawable : drawables){
				drawable.stroke(graphicsContext, currentZoomLevel);
			}
		}
		graphicsContext.restore();
	}

	private void fillBackground(Map<WayType, DrawingInfo> themeMap) {
		graphicsContext.save();
		boolean coastlineVisible = model.getWaysOfType(WayType.COASTLINE, getScreenBounds())
				.iterator()
				.hasNext();
		DrawingInfo theme = themeMap.get(WayType.COASTLINE);
		if (coastlineVisible) {
			theme = themeMap.get(WayType.WATER);
		}
		if (theme != null && theme.hasFillColor()) {
			graphicsContext.setFill(theme.getFillColor());
		}
		graphicsContext.setTransform(new Affine());
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		graphicsContext.restore();
	}

	private Rectangle getScreenBounds(){
		Bounds bounds = canvas.getBoundsInLocal();
		Point2D min = modelCoords(bounds.getMinX(), bounds.getMinY());
		Point2D max = modelCoords(bounds.getMaxX(), bounds.getMaxY());
		// Needed because the model is flipped
		return new Rectangle((float)min.getX(), (float)max.getY(), (float)max.getX(), (float)min.getY());
	}

	//Test function to visualize if the KDTree works.
	private Rectangle getSmallModelBounds(){
		int boxsize = 100;
		Bounds bounds = canvas.getBoundsInLocal();
		double minX = bounds.getMinX() + bounds.getMaxX()/2 - boxsize;
		double minY = bounds.getMinY()+ bounds.getMaxY()/2 - boxsize;
		double maxX = bounds.getMaxX()/2 + boxsize;
		double maxY = bounds.getMaxY()/2 + boxsize;
		Point2D min = modelCoords(minX, minY);
		Point2D max = modelCoords(maxX, maxY);
				// Needed because the model is flipped
		return new Rectangle((float)min.getX(), (float)max.getY(), (float)max.getX(), (float)min.getY());
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
