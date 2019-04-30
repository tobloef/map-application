package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.model.*;
import bfst19.danmarkskort.view.controls.MapCanvas;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;

public class MapDrawer implements Drawer {
	private MapCanvas canvas;
	private GraphicsContext graphicsContext;
	private Model model;
	private Rectangle textureDefaultRect;
	private final double textureScaleFactor = 10000;

	public MapDrawer(MapCanvas canvas, Model model) {
		this.canvas = canvas;
		this.graphicsContext = canvas.getGraphicsContext2D();
		this.model = model;
		textureDefaultRect = new Rectangle(model.modelBounds);
	}

	public void draw() {
		if (model.getCurrentTheme() == null) {
			return;
		}
		double currentZoomLevel = canvas.getDegreesLatitudePerPixel();
		fillBackground(model.getCurrentTheme());
		for (WayType wayType : WayType.values()){

			//Skip if no theme found
			DrawingInfo drawingInfo = model.getCurrentTheme().getDrawingInfo(wayType);
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
				drawablesToDraw = model.getWaysOfType(wayType, canvas.getScreenBounds());
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
		if (drawingInfo.hasTexture()) {
			double width = (textureDefaultRect.xMax - textureDefaultRect.xMin) / textureScaleFactor;
			double height = -(textureDefaultRect.yMax - textureDefaultRect.yMin) / textureScaleFactor;
			double modelX = (textureDefaultRect.xMin + (width/2));
			double modelY = (textureDefaultRect.yMin + (height/2));
			ImagePattern scaledImg = new ImagePattern(
					drawingInfo.getTexture().getImage(),
					modelX,
					modelY,
					width,
					height,
					false
			);
			fill = scaledImg;
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
			graphicsContext.setLineWidth(drawingInfo.calculateLineWidth(currentZoomLevel));
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
		return model.getWaysOfType(WayType.COASTLINE, canvas.getScreenBounds())
					.iterator()
					.hasNext();
	}
}
