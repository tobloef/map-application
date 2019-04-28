package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.view.controls.MapCanvas;
import javafx.scene.canvas.GraphicsContext;

public class AddressIndicatorDrawer implements Drawer {
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
        // TODO:
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
