package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.model.drawables.Drawable;
import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.drawables.PointOfInterest;
import bfst19.danmarkskort.model.WayType;
import bfst19.danmarkskort.view.controls.MapCanvas;
import javafx.scene.canvas.GraphicsContext;

public class POIDrawer implements Drawer {
    private final MapCanvas canvas;
    private final Model model;

    public POIDrawer(MapCanvas mapCanvas, Model model) {
        this.canvas = mapCanvas;
        this.model = model;
    }
	@Override
    public void draw() {
        if (model.dontDraw(WayType.POI)) {
            return;
        }
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        double zoomFactor = canvas.getDegreesLatitudePerPixel();
        for (Drawable drawable : model.getWaysOfType(WayType.POI, canvas.getScreenBounds())) {
            drawable.fill(graphicsContext, zoomFactor);
        }
        drawAddressIndicators(graphicsContext, model, zoomFactor);
    }

    private void drawAddressIndicators(GraphicsContext graphicsContext, Model model, double zoomFactor) {
        if (model.getStart() != null) {
            PointOfInterest startPOI = new PointOfInterest(
                    model.getStart().getRepresentativeX(),
                    model.getStart().getRepresentativeY()
            );
            startPOI.fill(graphicsContext, zoomFactor);
        }
        if (model.getEnd() != null) {
            PointOfInterest endPoi = new PointOfInterest(
                    model.getEnd().getRepresentativeX(),
                    model.getEnd().getRepresentativeY()
            );
            endPoi.fill(graphicsContext, zoomFactor);
        }
    }
}