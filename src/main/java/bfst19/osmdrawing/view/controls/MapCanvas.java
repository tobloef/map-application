package bfst19.osmdrawing.view.controls;

import bfst19.osmdrawing.model.Model;
import bfst19.osmdrawing.view.drawers.ZoomIndicatorDrawer;
import bfst19.osmdrawing.view.drawers.Drawer;
import bfst19.osmdrawing.view.drawers.MapDrawer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.List;

public class MapCanvas extends Canvas {
	private GraphicsContext graphicsContext = getGraphicsContext2D(); // these are assigned here since they are otherwise the only reason for a constructor
	private Affine transform = new Affine();
	private Model model;
	private List<Drawer> drawers;
	private double degreesLatitudePerPixel;

	public void initialize(Model model) {
		this.model = model;
		initializeDrawers(model);
		transform.prependScale(1,-1, 0, 0);
		panViewToMapBounds();
		updateDegreesPerPixel();
		graphicsContext.setFillRule(FillRule.EVEN_ODD);
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
		graphicsContext.setTransform(new Affine());
		graphicsContext.clearRect(0,0, getWidth(), getHeight());
		graphicsContext.setTransform(transform);
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
		transform.prependTranslation(deltaX, deltaY);
		repaint();
	}

	public void zoom(double factor, double x, double y) {
		transform.prependScale(factor, factor, x, y);
		updateDegreesPerPixel();
		repaint();
	}

	public void updateDegreesPerPixel() {
		//TODO make this a static method
		degreesLatitudePerPixel = 1/Math.sqrt(Math.abs(transform.determinant()));
	}

	public double getDegreesLatitudePerPixel() {
		return degreesLatitudePerPixel;
	}
}
