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

import java.util.ArrayList;

import static bfst19.osmdrawing.utils.ThemeLoader.loadTheme;

public class MapDrawer implements Drawer {
	private MapCanvas canvas;
	private GraphicsContext graphicsContext;
	private Model model;
	private Theme theme;

	public MapDrawer(MapCanvas canvas, Model model) {
		this.canvas = canvas;
		this.graphicsContext = canvas.getGraphicsContext2D();
		this.model = model;
		// TODO: This should be done somewhere else
		String defaultPath = "config/themes/default.yaml";
		try {
			theme = loadTheme(defaultPath, null);
		} catch (YAMLException e) {
			throw new RuntimeException("Couldn't read default theme from path \"" + defaultPath + "\", can't continue.", e);
		}
	}

	public void draw() {
		if (theme == null) {
			return;
		}
		double currentZoomLevel = canvas.getDegreesLatitudePerPixel();
		fillBackground(theme);
		for (WayType wayType : WayType.values()){

			//Skip if no theme found
			DrawingInfo drawingInfo = theme.getDrawingInfo(wayType);
			if (drawingInfo == null) {
				continue;
			}


			Iterable<Drawable> drawablesToDraw;
			if (drawingInfo.getAlwaysDraw()) {
				drawablesToDraw = model.getWaysOfType(wayType);
			}
			else if (isVisibleAtZoom(drawingInfo, currentZoomLevel)) {
				drawablesToDraw = model.getWaysOfType(wayType, getScreenBounds());
			}
			else {
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
		fillDrawables(drawables, drawingInfo, currentZoomLevel);
		strokeDrawables(drawables, drawingInfo, currentZoomLevel);
		graphicsContext.restore();
	}

	private void fillDrawables(Iterable<Drawable> drawables, DrawingInfo drawingInfo, double currentZoomLevel) {
		Paint fill = null;
		if (drawingInfo.hasFillColor()) {
			fill = drawingInfo.getFillColor();
		}
		// TODO: Add clause to see if textures are enabled.
		if (drawingInfo.hasTexture()) {
			fill = drawingInfo.getTexture();
		}
		if (fill == null) {
			return;
		}
		graphicsContext.setFill(fill);
		for (Drawable drawable : drawables) {
			drawable.fill(graphicsContext, currentZoomLevel);
		}
	}

	private void strokeDrawables(Iterable<Drawable> drawables, DrawingInfo drawingInfo, double currentZoomLevel) {
		if (!drawingInfo.hasStrokeColor()) {
			return;
		}
		graphicsContext.setStroke(drawingInfo.getStrokeColor());
		if (drawingInfo.hasLineDash()) {
			graphicsContext.setLineDashes(drawingInfo.getLineDash() / 10000);
		}
		if (drawingInfo.hasLineWidth()){
			graphicsContext.setLineWidth(drawingInfo.getLineWidth());
		}
		for (Drawable drawable : drawables){
			drawable.stroke(graphicsContext, currentZoomLevel);
		}
	}

	private void fillBackground(Theme theme) {
		graphicsContext.save();
		boolean coastlineVisible = model.getWaysOfType(WayType.COASTLINE, getScreenBounds())
				.iterator()
				.hasNext();
		DrawingInfo drawingInfo = theme.getDrawingInfo(WayType.COASTLINE);
		if (coastlineVisible) {
			drawingInfo = theme.getDrawingInfo(WayType.WATER);
		}
		if (drawingInfo != null && drawingInfo.hasFillColor()) {
			graphicsContext.setFill(drawingInfo.getFillColor());
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
