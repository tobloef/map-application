package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.model.Drawable;
import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.Rectangle;
import bfst19.danmarkskort.view.drawers.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapCanvas extends Canvas {
	private GraphicsContext graphicsContext = getGraphicsContext2D(); // these are assigned here since they are otherwise the only reason for a constructor
	private Model model;
	private List<Drawer> drawers;
	private MapDrawer mapDrawer;
	private double degreesLatitudePerPixel;
	private double minZoom = 0;
	private double maxZoom = 1000000;

	public void initialize(Model model) {
		this.model = model;
		Affine affine = new Affine();
		affine.prependScale(1,-1, 0, 0);
		graphicsContext.setTransform(affine);
		graphicsContext.setFillRule(FillRule.EVEN_ODD);
		initializeDrawers(model);
		panViewToMapBounds();
		updateDegreesPerPixel();
		model.addObserver(this::repaint);
		makeCanvasUpdateOnResize();
		repaint();
	}

	private void initializeDrawers(Model model) {
		//The order in which elements are drawn is fairly important, please check if everything works as intended after changing
		drawers = new ArrayList<>();
		mapDrawer = new MapDrawer(this, model);
		drawers.add(mapDrawer);
		drawers.add(new RouteDrawer(this, model));
		drawers.add(new DebugDrawer(this, model)); //todo figure out if this should be in the final program or not
		drawers.add(new ZoomIndicatorDrawer(this));
	}


	public void repaint() {
		clearBackground();
		updateLineWidth();
		for (Drawer drawer : drawers) {
			drawer.draw();
		}
	}

	private void makeCanvasUpdateOnResize() {
		this.widthProperty().addListener(observable -> repaint());
		this.heightProperty().addListener(observable -> repaint());
	}

	private void clearBackground() {
		graphicsContext.save();
		graphicsContext.setTransform(new Affine());
		graphicsContext.clearRect(0,0, getWidth(), getHeight());
		graphicsContext.restore();
	}

	private void panViewToMapBounds() {
		if (model.modelBounds == null) {
			return;
		}
		pan(-model.modelBounds.xMin, model.modelBounds.yMax, false);
		zoom(findInitialZoomFactor(), 0,0, false);
		double xMargin = (getWidth() - ((model.modelBounds.xMax - model.modelBounds.xMin) / degreesLatitudePerPixel))/2;
		double yMargin = (getHeight() - ((model.modelBounds.yMax - model.modelBounds.yMin) / degreesLatitudePerPixel))/2;
		pan(xMargin, yMargin);
		Affine transform = graphicsContext.getTransform();
		minZoom = transform.getMxx();
	}

	private double findInitialZoomFactor() {
		double minRequiredWidthZoom = getWidth()/(model.modelBounds.xMax -model.modelBounds.xMin);
		double minRequiredHeightZoom = getHeight()/(model.modelBounds.yMax -model.modelBounds.yMin);
		double extraMarginFactor = 0.5; //if more than 1.0, the margin will become negative
		double smallestRequiredZoom = minRequiredWidthZoom < minRequiredHeightZoom ? minRequiredWidthZoom : minRequiredHeightZoom;
		return smallestRequiredZoom * extraMarginFactor;
	}

	private void updateLineWidth() {
		graphicsContext.setLineWidth(getDegreesLatitudePerPixel());
	}


	public void pan(double deltaX, double deltaY) {
		pan(deltaX, deltaY, true);
	}

	public void pan(double deltaX, double deltaY, boolean shouldRepaint) {
		Affine transform = graphicsContext.getTransform();
		transform.prependTranslation(deltaX, deltaY);
		graphicsContext.setTransform(transform);
		if (shouldRepaint) {
			repaint();
		}
	}

	public void zoom(double factor, double x, double y) {
		zoom(factor, x, y, true);
	}

	public void zoom(double factor, double x, double y, boolean shouldRepaint) {
		Affine transform = graphicsContext.getTransform();
		factor = clampZoom(factor, transform);
		if (factor == 1) {
			return;
		}
		transform.prependScale(factor, factor, x, y);
		graphicsContext.setTransform(transform);
		updateDegreesPerPixel();
		if (shouldRepaint) {
			repaint();
		}
	}

	private double clampZoom(double factor, Affine transform) {
		if (transform.getMxx() * factor < minZoom) {
			factor = minZoom / transform.getMxx();
		}
		else if (transform.getMxx() * factor > maxZoom) {
			factor = maxZoom / transform.getMxx();
		}
		return factor;
	}

	public void updateDegreesPerPixel() {
		//TODO make this a static method
		double determinant = graphicsContext.getTransform().determinant();
		degreesLatitudePerPixel = 1/Math.sqrt(Math.abs(determinant));
	}

	public double getDegreesLatitudePerPixel() {
		return degreesLatitudePerPixel;
	}


	public Point2D modelCoords(double x, double y) {
		try {
			return graphicsContext.getTransform().inverseTransform(x, y);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
