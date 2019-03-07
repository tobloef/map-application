package bfst19.osmdrawing;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;

public class MapCanvas extends Canvas {
	GraphicsContext gc = getGraphicsContext2D();
	Affine transform = new Affine();
	Model model;
	MapDrawer mapDrawer;
	public void init(Model model) {
		this.model = model;
		mapDrawer = new MapDrawer(gc, model);
		panViewToModel();
		transform.prependScale(1,-1, 0, 0);
		gc.setFillRule(FillRule.EVEN_ODD);
		model.addObserver(this::repaint);
		makeCanvasUpdateOnResize();
		repaint();

	}


	public void repaint() {
		clearBackground();
		updateLineWidth();
		mapDrawer.draw();
		//drawShapes();
	}

	private void makeCanvasUpdateOnResize() {
		this.widthProperty().addListener(observable -> repaint());
		this.heightProperty().addListener(observable -> repaint());
	}

	private void clearBackground() {
		gc.setTransform(new Affine());
		if (model.getWaysOfType(WayType.COASTLINE).iterator().hasNext()) {
			gc.setFill(Color.BLUE);
		} else {
			gc.setFill(Color.GREEN);
		}
		gc.fillRect(0, 0, getWidth(), getHeight());
		gc.setTransform(transform);
	}

	private void panViewToModel() {
		pan(-model.minlon, -model.maxlat);
		zoom(getWidth()/(model.maxlon-model.minlon), 0,0);
	}

	private void updateLineWidth() {
		gc.setLineWidth(1/Math.sqrt(Math.abs(transform.determinant())));
	}


	public void pan(double dx, double dy) {
		transform.prependTranslation(dx, dy);
		repaint();
	}

	public void zoom(double factor, double x, double y) {
		transform.prependScale(factor, factor, x, y);
		repaint();
	}

	public Point2D modelCoords(double x, double y) {
		try {
			return transform.inverseTransform(x, y);
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
			return null;
		}
	}
}
