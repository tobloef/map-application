package bfst19.osmdrawing;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MapCanvas extends Canvas {
	private GraphicsContext graphicsContext = getGraphicsContext2D();
	private Affine transform = new Affine();
	private Model model;
	private MapDrawer mapDrawer;
	private ZoomIndicatorDrawer zoomIndicatorDrawer;

	public void init(Model model) {
		this.model = model;
		mapDrawer = new MapDrawer(this, model);
		zoomIndicatorDrawer = new ZoomIndicatorDrawer(this);
		panViewToModel();
		transform.prependScale(1,-1, 0, 0);
		graphicsContext.setFillRule(FillRule.EVEN_ODD);
		model.addObserver(this::repaint);
		makeCanvasUpdateOnResize();
		repaint();

	}


	public void repaint() {
		clearBackground();
		updateLineWidth();
		//The order in which elements are drawn is fairly important, please check if everything works as intended after changing
		mapDrawer.draw();
		zoomIndicatorDrawer.draw();
	}

	private void makeCanvasUpdateOnResize() {
		this.widthProperty().addListener(observable -> repaint());
		this.heightProperty().addListener(observable -> repaint());
	}

	private void clearBackground() {
		graphicsContext.setTransform(new Affine());

		graphicsContext.setTransform(transform);
	}

	private void panViewToModel() {
		//This repaints the map twice.
		pan(-model.modelBounds.xmin, -model.modelBounds.ymax);
		zoom(getWidth()/(model.modelBounds.xmax-model.modelBounds.xmin), 0,0);
	}

	private void updateLineWidth() {
		graphicsContext.setLineWidth(1/Math.sqrt(Math.abs(transform.determinant())));
	}


	public void pan(double dx, double dy) {
		transform.prependTranslation(dx, dy);
		repaint();
	}

	public void zoom(double factor, double x, double y) {
		transform.prependScale(factor, factor, x, y);
		repaint();
	}
}
