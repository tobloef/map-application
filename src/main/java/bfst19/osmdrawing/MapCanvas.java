package bfst19.osmdrawing;


import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MapCanvas extends Canvas{
	GraphicsContext gc = getGraphicsContext2D();
	Affine transform = new Affine();
	Model model;
	private double zoomLevel;

	public void init(Model model) {
		this.model = model;
		panViewToModel();
		gc.setFillRule(FillRule.EVEN_ODD);
		flipYCoordinates();
		model.addObserver(this::repaint);
		makeCanvasUpdateOnResize();
		repaint();
	}

	private void makeCanvasUpdateOnResize() {
		this.widthProperty().addListener(observable -> repaint());
		this.heightProperty().addListener(observable -> repaint());
	}

	private void panViewToModel() {
		pan(-model.minlon, -model.maxlat);
		zoom(getWidth()/(model.maxlon-model.minlon), 0,0);
	}

	private void flipYCoordinates() {
		transform.prependScale(1,-1, 0, 0);
	}

	public void repaint() {
		clearCanvas();
		updateTransform();
		setColors();
		drawShapes();
	}

	private void drawShapes() {
		for (WayType wayType : WayType.values()){
			if (wayType.hasFill()) {
				gc.setFill(wayType.getFillColor());
				for (Drawable way : model.getWaysOfType(wayType)) {
					if (way instanceof MultiPolyline)
						way.fill(gc);
				}
			}
			if (wayType.hasStroke()) {
				gc.setLineDashes(wayType.getLineDash() / 10000);
				gc.setStroke(wayType.getStrokeColor());
				for (Drawable way : model.getWaysOfType(wayType)){
					if (way instanceof MultiPolyline)
						way.stroke(gc);
				}
			}
		}
	}

	private void setColors() {
		gc.setStroke(Color.BLACK);
		gc.setFill(Color.RED);
	}

	private void updateTransform() {
		gc.setTransform(transform);
		updateZoomLevel();
		gc.setLineWidth(this.zoomLevel);
	}

	private void updateZoomLevel() {
		this.zoomLevel = 1/Math.sqrt(Math.abs(transform.determinant()));
	}

	private void clearCanvas() {
		gc.setTransform(new Affine());
		gc.clearRect(0, 0, getWidth(), getHeight());
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
