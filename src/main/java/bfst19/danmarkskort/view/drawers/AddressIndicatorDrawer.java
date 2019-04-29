package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.PolyRoad;
import bfst19.danmarkskort.view.controls.MapCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

public class AddressIndicatorDrawer implements Drawer {
    private static final float maxDistance = 0.0000001f;
    private static final float minZoom = 0.00003f;

    private boolean enabled = false;
    private MapCanvas canvas;
    private GraphicsContext graphicsContext;
    private Model model;

    public AddressIndicatorDrawer(MapCanvas canvas, Model model) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.model = model;
    }

    @Override
    public void draw() {
        graphicsContext.setTransform(new Affine());

        if (canvas.getDegreesLatitudePerPixel() > minZoom) {
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

        // TODO: Max distance to road, so we don't draw roads when hovering in the sea.
        // TODO: This should be scaled to be the same, regardless of zoom level, so it's the same number of pixels you need to be within a road of.
        double distance = road.euclideanDistanceSquaredTo(mouseModelX, mouseModelY);
        //distance = distance / canvas.getDegreesLatitudePerPixel();
        if (distance > maxDistance) {
            return;
        }

        // Draw text
        // TODO: Better styled text. Background box?
        graphicsContext.setLineWidth(1);
        graphicsContext.setFont(Font.getDefault());
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        double x = mouseScreenX;
        double y = mouseScreenY - 5;
        graphicsContext.strokeText(road.getStreetName(), x, y);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean getEnabled() {
        return enabled;
    }
}
