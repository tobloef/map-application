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

	public void init(Model model) {
		this.model = model;
		pan(-model.minlon, -model.maxlat);
		zoom(800/(model.maxlon-model.minlon), 0,0);
		transform.prependScale(1,-1, 0, 0);
		model.addObserver(this::repaint);
		model.addObserver(this::repaint);
		repaint();
	}

	public void repaint() {
		gc.setTransform(new Affine());
		if (model.getWaysOfType(WayType.COASTLINE).iterator().hasNext()) {
			gc.setFill(Color.BLUE);
		} else {
			gc.setFill(Color.GREEN);
		}
		gc.fillRect(0, 0, getWidth(), getHeight());
		gc.setTransform(transform);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1/Math.sqrt(Math.abs(transform.determinant())));
//		for (Drawable way : model.getWaysOfType(WayType.UNKNOWN)) way.stroke(gc);
		gc.setFillRule(FillRule.EVEN_ODD);
		gc.setFill(Color.GREEN);
		for (Drawable way : model.getWaysOfType(WayType.COASTLINE)) way.fill(gc);
		for (Drawable way : model.getWaysOfType(WayType.COASTLINE)) way.stroke(gc);
		gc.setFill(Color.BLUE);
		for (Drawable way : model.getWaysOfType(WayType.WATER)) way.fill(gc);
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
