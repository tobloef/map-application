package bfst19.osmdrawing.view;

import bfst19.osmdrawing.model.Model;
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

	public void initialize(Model model) {
		this.model = model;
		initializeDrawers(model);
		panViewToMapBounds();
		transform.prependScale(1,-1, 0, 0);
		graphicsContext.setFillRule(FillRule.EVEN_ODD);
		model.addObserver(this::repaint);
		makeCanvasUpdateOnResize();
		repaint();

	}

	private void initializeDrawers(Model model) {
		//The order in which elements are drawn is fairly important, please check if everything works as intended after changing
		drawers = new ArrayList<>();
		drawers.add( new MapDrawer(this, model));
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
		//This repaints the map twice.
		pan(-model.modelBounds.xmin, -model.modelBounds.ymax);
		zoom(getWidth()/(model.modelBounds.xmax-model.modelBounds.xmin), 0,0);
	}

	private void updateLineWidth() {
		graphicsContext.setLineWidth(getZoomFactor());
	}


	public void pan(double deltaX, double deltaY) {
		transform.prependTranslation(deltaX, deltaY);
		repaint();
	}

	public void zoom(double factor, double x, double y) {
		transform.prependScale(factor, factor, x, y);
		repaint();
	}

	public double getZoomFactor() {
		return 1/Math.sqrt(Math.abs(transform.determinant()));
	}
}
