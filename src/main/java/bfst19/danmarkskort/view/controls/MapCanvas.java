package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.model.Model;
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
	private double degreesLatitudePerPixel;
	private double minZoom = 1;
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
		drawers.add(new MapDrawer(this, model));
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
		//FIXME: This repaints the map twice.
		if (model.modelBounds == null) {
			return;
		}
		pan(-model.modelBounds.xMin, model.modelBounds.yMax);
		zoom(getWidth()/(model.modelBounds.xMax -model.modelBounds.xMin), 0,0);
	}

	private void updateLineWidth() {
		graphicsContext.setLineWidth(getDegreesLatitudePerPixel());
	}


	public void pan(double deltaX, double deltaY) {
		Affine transform = graphicsContext.getTransform();
		transform.prependTranslation(deltaX, deltaY);
		graphicsContext.setTransform(transform);
		repaint();
	}

	public void zoom(double factor, double x, double y) {
		Affine transform = graphicsContext.getTransform();
		factor = clampZoom(factor, transform);
		if (factor == 1) {
			return;
		}
		transform.prependScale(factor, factor, x, y);
		graphicsContext.setTransform(transform);
		updateDegreesPerPixel();
		repaint();
		System.out.println(transform.getMxx());
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
