package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.Rectangle;
import bfst19.danmarkskort.view.drawers.ZoomIndicatorDrawer;
import bfst19.danmarkskort.view.drawers.Drawer;
import bfst19.danmarkskort.view.drawers.MapDrawer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.List;

public class MapCanvas extends Canvas {
	private GraphicsContext graphicsContext = getGraphicsContext2D(); // these are assigned here since they are otherwise the only reason for a constructor
	private Model model;
	private List<Drawer> drawers;
	private MapDrawer mapDrawer;
	private double degreesLatitudePerPixel;
	private double minZoom = 0;
	private double maxZoom = 1000000;
	private Rectangle ultimateBounds;

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
		ultimateBounds = mapDrawer.getScreenBounds();
		System.out.println(ultimateBounds);
	}

	private double findInitialZoomFactor() {
		double minRequiredWidthZoom = getWidth()/(model.modelBounds.xMax -model.modelBounds.xMin);
		double minRequiredHeightZoom = getHeight()/(model.modelBounds.yMax -model.modelBounds.yMin);
		double extraMarginFactor = 0.8; //if more than 1.0, the margin will become negative
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
		if (ultimateBounds != null) {
			Rectangle screenBounds = mapDrawer.getScreenBounds();
			deltaX = clampDelta(deltaX, screenBounds.xMin, screenBounds.xMax, ultimateBounds.xMin, ultimateBounds.xMax);
			deltaY = clampDelta(deltaY, screenBounds.yMin, screenBounds.yMax, ultimateBounds.yMin, ultimateBounds.yMax);
		}
		//System.out.println(transform.getTx() + " " + (transform.getTx() / transform.getMxx()));
		transform.prependTranslation(deltaX, deltaY);
		graphicsContext.setTransform(transform);
		if (shouldRepaint) {
			repaint();
		}
	}

	private double clampDelta(double delta, double screenMin, double screenMax, double ultimateMin, double ultimateMax) {
		if (ultimateBounds == null) {
			return delta;
		}
		double deltaCoordinate = delta * degreesLatitudePerPixel;
		if (screenMin + deltaCoordinate < ultimateMin) {
			System.out.println("Min " + screenMin + " " + deltaCoordinate + " " + ultimateMin);
			delta = Math.max(0, delta);
		}
		else if (screenMax + deltaCoordinate > ultimateMax) {
			System.out.println("Max " + screenMax + " " + deltaCoordinate + " " + ultimateMax);
			delta = Math.min(0, delta);
		}
		return delta;
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
}
