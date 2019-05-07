package bfst19.danmarkskort.view.controls;

import bfst19.danmarkskort.model.*;
import bfst19.danmarkskort.view.drawers.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.List;

public class MapCanvas extends Canvas {
    private final GraphicsContext graphicsContext;
    private Model model;
    private List<Drawer> drawers;
    private double degreesLatitudePerPixel;
    private double minZoom = 0;
    private double maxZoom = 1000000;

    public MapCanvas() {
        graphicsContext = getGraphicsContext2D();
    }

    public void initialize(Model model) {
        this.model = model;
        model.addReloadObserver(this::initialize);
        initialize();
    }

    public void initialize() {
        Affine affine = new Affine();
        affine.prependScale(1, -1, 0, 0);
        graphicsContext.setTransform(affine);
        graphicsContext.setFillRule(FillRule.EVEN_ODD);
        initializeDrawers(model);
        panViewToMapBounds();
        updateDegreesPerPixel();
        model.addWayTypeObserver(this::repaint);
        makeCanvasUpdateOnResize();
        repaint();
    }

    private void initializeDrawers(Model model) {
        //The order in which elements are drawn is fairly important, please check if everything works as intended after changing
        drawers = new ArrayList<>();
        drawers.add(new MapDrawer(this, model));
        drawers.add(new RouteDrawer(this, model));
        drawers.add(new POIDrawer(this, model));
        drawers.add(new ZoomIndicatorDrawer(this));
    }

    public void repaint() {
        clearBackground();
        updateLineWidth();
        for (Drawer drawer : drawers) {
            if (!drawer.getEnabled()) {
                continue;
            }
            graphicsContext.save();
            try {
                drawer.draw();
            } finally {
                graphicsContext.restore();
            }
        }
    }

    private void makeCanvasUpdateOnResize() {
        this.widthProperty().addListener(observable -> repaint());
        this.heightProperty().addListener(observable -> repaint());
    }

    private void clearBackground() {
        graphicsContext.save();
        graphicsContext.setTransform(new Affine());
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());
        graphicsContext.restore();
    }

    private void panViewToMapBounds() {
        if (model.getModelBounds() == null) {
            return;
        }
        pan(-model.getModelBounds().xMin, model.getModelBounds().yMax, false);
        zoom(findInitialZoomFactor(), 0, 0, false);
        double xMargin = (getWidth() - ((model.getModelBounds().xMax - model.getModelBounds().xMin) / degreesLatitudePerPixel)) / 2;
        double yMargin = (getHeight() - ((model.getModelBounds().yMax - model.getModelBounds().yMin) / degreesLatitudePerPixel)) / 2;
        pan(xMargin, yMargin);
        Affine transform = graphicsContext.getTransform();
        minZoom = transform.getMxx();
    }

    private double findInitialZoomFactor() {
        double minRequiredWidthZoom = getWidth() / (model.getModelBounds().xMax - model.getModelBounds().xMin);
        double minRequiredHeightZoom = getHeight() / (model.getModelBounds().yMax - model.getModelBounds().yMin);
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
        } else if (transform.getMxx() * factor > maxZoom) {
            factor = maxZoom / transform.getMxx();
        }
        return factor;
    }

    public void updateDegreesPerPixel() {
        double determinant = graphicsContext.getTransform().determinant();
        degreesLatitudePerPixel = 1 / Math.sqrt(Math.abs(determinant));
    }

    public double getDegreesLatitudePerPixel() {
        return degreesLatitudePerPixel;
    }


    public Point2D screenToModelCoords(double x, double y) {
        try {
            return graphicsContext.getTransform().inverseTransform(x, y);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rectangle getScreenBounds() {
            Bounds bounds = this.getBoundsInLocal();
            Point2D min = this.screenToModelCoords(bounds.getMinX(), bounds.getMinY());
            Point2D max = this.screenToModelCoords(bounds.getMaxX(), bounds.getMaxY());
            if (min == null || max == null) {
                return null;
            }
            // Needed because the model is flipped
            return new Rectangle((float) min.getX(), (float) max.getY(), (float) max.getX(), (float) min.getY());

    }
}
