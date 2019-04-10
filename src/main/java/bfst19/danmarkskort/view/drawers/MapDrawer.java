package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.model.*;
import bfst19.danmarkskort.view.controls.MapCanvas;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.util.ArrayList;

import static bfst19.danmarkskort.utils.ThemeLoader.loadTheme;

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
		theme = loadTheme("config/themes/default.yaml", null);
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
			if (model.dontDraw(wayType)){
				continue;
			}

			Iterable<Drawable> drawablesToDraw;
			if (drawingInfo.hasAlwaysDraw() && drawingInfo.getAlwaysDraw()) {
				drawablesToDraw = model.getWaysOfType(wayType);
			}
			else if (isVisibleAtZoom(drawingInfo, currentZoomLevel)) {
				drawablesToDraw = model.getWaysOfType(wayType, getScreenBounds());
			}
			else {
				continue;
			}
			drawDrawables(drawablesToDraw, drawingInfo, currentZoomLevel);
			dontStrokeLastFill();
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
		return drawingInfo.hasAlwaysDraw() && drawingInfo.getAlwaysDraw();
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

	private void dontStrokeLastFill() {
		/*
		There appears to be a bug in javafx that if there is no stroke, it just strokes the last fill with black.
		So the avoid this bug we have to make stroke somewhere. The values for where is arbitrary and dont matter.
		*/
		graphicsContext.beginPath();
		graphicsContext.moveTo(-2000000, -2000000);
		graphicsContext.lineTo(-2000000, -2000000);
		graphicsContext.stroke();
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
		DrawingInfo backgroundDrawingInfo = theme.getDrawingInfo(WayType.COASTLINE);
		if (coastlineIsVisible()) {
			backgroundDrawingInfo = theme.getDrawingInfo(WayType.WATER);
		}
		if (backgroundDrawingInfo != null && backgroundDrawingInfo.hasFillColor()) {
			graphicsContext.setFill(backgroundDrawingInfo.getFillColor());
		}
		graphicsContext.setTransform(new Affine());
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		graphicsContext.restore();
	}

	private boolean coastlineIsVisible() {
		return model.getWaysOfType(WayType.COASTLINE, getScreenBounds())
					.iterator()
					.hasNext();
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
