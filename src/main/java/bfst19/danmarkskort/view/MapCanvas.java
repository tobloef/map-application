package bfst19.danmarkskort.view;

import bfst19.danmarkskort.model.*;
import bfst19.danmarkskort.view.drawers.*;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.List;

public class MapCanvas extends Canvas {
    private static final float tooltipMaxDistance = 50f;
    private static final float tooltipMinZoom = 0.00003f;

    private GraphicsContext graphicsContext = getGraphicsContext2D(); // these are assigned here since they are otherwise the only reason for a constructor
    private Model model;
    private List<Drawer> drawers;
    private double degreesLatitudePerPixel;
    private double minZoom = 0;
    private double maxZoom = 1000000;
    private Tooltip tooltip;

    public void initialize(Model model) {
        this.model = model;
        model.addWayTypeObserver(this::repaint);
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
        model.addMouseIdleObserver(idle -> {
            if (idle) {
                showTooltip();
            } else {
                hideTooltip();
            }
        });

        tooltip = new Tooltip();
        tooltip.setAutoHide(false);
        tooltip.setAutoFix(false);
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

    public void panViewToAddress(Address address) {
        // TODO: Implement
    }

    public void panViewToRoute(Route route) {
        Rectangle routeBBox = route.getBoundingBox();
        Rectangle screenBounds = getScreenBounds();
        System.out.println(routeBBox);
        System.out.println(screenBounds);
        double sizeDelta = screenBounds.getSizeLargestDelta(routeBBox);
        double x = -(routeBBox.xMin - screenBounds.getMiddleX()) / getDegreesLatitudePerPixel();
        double y = (routeBBox.yMax - screenBounds.getMiddleY()) / getDegreesLatitudePerPixel();
        pan(x, y);
        System.out.println(sizeDelta);
        //zoom(sizeDelta, routeBBox.xMin, routeBBox.yMax);
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


    public Point2D modelCoords(double x, double y) {
        try {
            return graphicsContext.getTransform().inverseTransform(x, y);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Rectangle getScreenBounds() {
        Bounds bounds = this.getBoundsInLocal();
        Point2D min = this.modelCoords(bounds.getMinX(), bounds.getMinY());
        Point2D max = this.modelCoords(bounds.getMaxX(), bounds.getMaxY());
        // Needed because the model is flipped
        return new Rectangle((float) min.getX(), (float) max.getY(), (float) max.getX(), (float) min.getY());
    }

    //Test function to visualize if the KDTree works.
    private Rectangle getSmallModelBounds() {
        int boxsize = 100;
        Bounds bounds = this.getBoundsInLocal();
        double minX = bounds.getMinX() + bounds.getMaxX() / 2 - boxsize;
        double minY = bounds.getMinY() + bounds.getMaxY() / 2 - boxsize;
        double maxX = bounds.getMaxX() / 2 + boxsize;
        double maxY = bounds.getMaxY() / 2 + boxsize;
        Point2D min = this.modelCoords(minX, minY);
        Point2D max = this.modelCoords(maxX, maxY);
        // Needed because the model is flipped
        return new Rectangle((float) min.getX(), (float) max.getY(), (float) max.getX(), (float) min.getY());
    }

    private void hideTooltip() {
        if (tooltip != null) {
            tooltip.hide();
        }
    }

    private void showTooltip() {
        if (!model.getIsMouseInWindow()) {
            return;
        }
        if (tooltip == null) {
            return;
        }
        if (getDegreesLatitudePerPixel() > tooltipMinZoom) {
            return;
        }
        float mouseModelX = model.getMouseModelX();
        float mouseModelY = model.getMouseModelY();
        float mouseScreenX = model.getMouseScreenX();
        float mouseScreenY = model.getMouseScreenY();
        PolyRoad road = model.getClosestRoad(mouseModelX, mouseModelY);
        if (road == null) {
            return;
        }
        if (road.getStreetName() == null) {
            return;
        }
        double distance = Math.sqrt(road.euclideanDistanceSquaredTo(mouseModelX, mouseModelY));
        distance = distance / getDegreesLatitudePerPixel();
        if (distance > tooltipMaxDistance) {
            return;
        }
        Platform.runLater(() -> {
            tooltip.setText(road.getStreetName());
            Point2D screenCoords = this.localToScreen(mouseScreenX, mouseScreenY);
            tooltip.show(this, screenCoords.getX(), screenCoords.getY() - 30);
        });
    }
}
