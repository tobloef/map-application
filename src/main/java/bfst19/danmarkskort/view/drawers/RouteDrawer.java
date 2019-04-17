package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.model.*;
import bfst19.danmarkskort.view.controls.MapCanvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

import static bfst19.danmarkskort.utils.ThemeLoader.loadTheme;

public class RouteDrawer implements Drawer{
	private MapCanvas canvas;
	private Model model;

	public RouteDrawer(MapCanvas canvas, Model model) {
		this.canvas = canvas;
		this.model = model;
	}

	@Override
	public void draw() {
		Theme theme = loadTheme("config/themes/default.yaml", null);
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setLineWidth(theme.getDrawingInfo(WayType.RESIDENTIAL_ROAD).getLineWidth() * 4);
		for (Drawable drawable : model.getShortestPath()){
			drawable.stroke(graphicsContext, canvas.getDegreesLatitudePerPixel());
		}
	}
}
