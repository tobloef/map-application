package bfst19.osmdrawing.view.drawers;

import bfst19.osmdrawing.model.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.util.Map;

import static bfst19.osmdrawing.utils.LoadWayTypeThemeMap.loadWayTypeThemeMap;

public class MapDrawer implements Drawer {
	private Canvas canvas;
	private GraphicsContext graphicsContext;
	private Model model;
	private Map<WayType, WayTypeTheme> wayTypeThemeMap;

	public MapDrawer(Canvas canvas, Model model) {
		this.canvas = canvas;
		this.graphicsContext = canvas.getGraphicsContext2D();
		this.model = model;
		wayTypeThemeMap = loadWayTypeThemeMap();
	}

	public void draw() {
		fillBackground(wayTypeThemeMap);

		for (WayType wayType : WayType.values()){
			graphicsContext.save();
			WayTypeTheme theme = wayTypeThemeMap.get(wayType);
			if (theme == null) {
				continue;
			}
			Paint fill = null;
			if (theme.getFillColor() != null) {
				fill = theme.getFillColor();
			}
			// TODO: Add clause to see if textures are enabled.
			if (theme.getTexture() != null) {
				fill = theme.getTexture();
			}
			if (fill != null) {
				graphicsContext.setFill(fill);
				for (Drawable way : model.getWaysOfType(wayType, getScreenBounds())) {
					way.fill(graphicsContext);
				}
			}
			if (theme.getStrokeColor() != null) {
				graphicsContext.setStroke(theme.getStrokeColor());

				if (theme.getLineDash() > 0) {
					graphicsContext.setLineDashes(theme.getLineDash() / 10000);
				}
				if(theme.getLineWidth() > 0){
					graphicsContext.setLineWidth(theme.getLineWidth());
				}
				for (Drawable way : model.getWaysOfType(wayType, getScreenBounds())){
					way.stroke(graphicsContext);
				}
			}
			graphicsContext.restore();
		}
	}

	private void fillBackground(Map<WayType, WayTypeTheme> themeMap) {
		boolean coastlineVisible = model.getWaysOfType(WayType.COASTLINE, getScreenBounds())
				.iterator()
				.hasNext();
		WayTypeTheme theme = themeMap.get(WayType.COASTLINE);
		if (coastlineVisible) {
			theme = themeMap.get(WayType.WATER);
		}
		if (theme != null && theme.getFillColor() != null) {
			graphicsContext.setFill(theme.getFillColor());
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
