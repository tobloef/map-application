package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.model.*;
import bfst19.danmarkskort.view.controls.MapCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static bfst19.danmarkskort.utils.ThemeLoader.loadTheme;

public class RouteDrawer implements Drawer{
	public static boolean ShowExplored;
	private MapCanvas canvas;
	private Model model;
	public static boolean debugging = true;

	public RouteDrawer(MapCanvas canvas, Model model) {
		this.canvas = canvas;
		this.model = model;
	}

	@Override
	public void draw() {
		if (!debugging) {
			return;
		}

		Theme theme = loadTheme("config/themes/default.yaml", null);
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setLineWidth(theme.getDrawingInfo(WayType.RESIDENTIAL_ROAD).getLineWidth() * 4);
		Set<PolyRoad> oneWayRoads = new HashSet<>();
		graphicsContext.save();
		if (ShowExplored) {
			graphicsContext.setStroke(Color.RED);
			for (PolyRoad road : Dijkstra.lastUsedRoads) {
				road.stroke(graphicsContext, canvas.getDegreesLatitudePerPixel());
				if (road.isOneWay()) {
					oneWayRoads.add(road);
				}
			}
			graphicsContext.setStroke(Color.BLUE);
			for (PolyRoad road : oneWayRoads) {
				road.stroke(graphicsContext, canvas.getDegreesLatitudePerPixel());
			}
		}
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setLineWidth(canvas.getDegreesLatitudePerPixel() * 4);
		for (Drawable drawable : model.getShortestPath()) {
			drawable.stroke(graphicsContext, canvas.getDegreesLatitudePerPixel());

		}
		graphicsContext.restore();
	}
}
