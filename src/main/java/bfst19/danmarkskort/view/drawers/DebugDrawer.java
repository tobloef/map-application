package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.model.*;
import bfst19.danmarkskort.view.controls.MapCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static bfst19.danmarkskort.utils.ThemeLoader.loadTheme;

public class DebugDrawer implements Drawer {
	private MapCanvas canvas;
	private Model model;
	private static Set<PolyRoad> roads = new HashSet<>();

	public DebugDrawer(MapCanvas canvas, Model model) {
		this.canvas = canvas;
		this.model = model;
	}
	@Override
	public void draw() {
		Theme theme = loadTheme("config/themes/default.yaml", null);
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.save();
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setLineWidth(canvas.getDegreesLatitudePerPixel() * 4);
		for (Drawable drawable : roads) {
			drawable.stroke(graphicsContext, canvas.getDegreesLatitudePerPixel());
		}
		graphicsContext.restore();
	}

	public static void toggle(PolyRoad road) {
		System.out.println(road + " with connections " + road.getFirstConnections() + " and " + road.getLastConnections());
		if (roads.contains(road)) {
			roads.remove(road);
		}
		else {
			roads.add(road);
		}
	}
}
