package bfst19.osmdrawing;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MapCanvas extends Canvas {
	GraphicsContext gc = getGraphicsContext2D();
	Affine transform = new Affine();
	Model model;

	public void init(Model model) {
		this.model = model;
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
		drawShapes();
	}

	private void makeCanvasUpdateOnResize() {
		this.widthProperty().addListener(observable -> repaint());
		this.heightProperty().addListener(observable -> repaint());
	}

	private void clearBackground() {
		gc.setTransform(new Affine());
		if (model.getWaysOfType(WayType.COASTLINE, getModelBounds()).iterator().hasNext()) {
			gc.setFill(WayType.WATER.getFillColor());
		} else {
			gc.setFill(WayType.COASTLINE.getFillColor());
		}
		gc.fillRect(0, 0, getWidth(), getHeight());
		gc.setTransform(transform);
	}

	private void panViewToModel() {
		//This repaints the map twice.
		pan(-model.bounds.xmin, -model.bounds.ymax);
		zoom(getWidth()/(model.bounds.xmax-model.bounds.xmin), 0,0);
	}

	private void updateLineWidth() {
		gc.setLineWidth(1/Math.sqrt(Math.abs(transform.determinant())));
	}

	private void drawShapes() {
		ModelBounds modelBounds = getModelBounds();
		for (WayType wayType : WayType.values()){
			if (wayType.hasFill()) {
				gc.setFill(wayType.getFillColor());
				for (Drawable way : model.getWaysOfType(wayType, modelBounds)) {
					way.fill(gc);
				}
			}
			if (wayType.hasStroke()) {
				gc.setLineDashes(wayType.getLineDash() / 10000);
				gc.setStroke(wayType.getStrokeColor());
				for (Drawable way : model.getWaysOfType(wayType, modelBounds)){
					way.stroke(gc);
				}
			}
		}
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

	private ModelBounds getModelBounds(){
		Bounds bounds = this.getBoundsInLocal();
		Point2D min = modelCoords(bounds.getMinX(), bounds.getMinY());
		Point2D max = modelCoords(bounds.getMaxX(), bounds.getMaxY());
		return new ModelBounds(min.getX(), min.getY(), max.getX(), max.getY());
	}
}
