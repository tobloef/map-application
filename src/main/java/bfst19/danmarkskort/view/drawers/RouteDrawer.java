package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.model.*;
import bfst19.danmarkskort.view.MapCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;

public class RouteDrawer implements Drawer{
	private boolean enabled = true;
	public static boolean ShowExplored;
	private MapCanvas canvas;
	private Model model;
	public static boolean debugging = true;
	Theme theme;


	public RouteDrawer(MapCanvas canvas, Model model) {
		this.canvas = canvas;
		this.model = model;
		theme = model.getCurrentTheme();
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean getEnabled() {
		return enabled;
	}

	@Override
	public void draw() {
		if (!debugging) {
			return;
		}
		
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setLineWidth(theme.getDrawingInfo(WayType.RESIDENTIAL_ROAD).getLineWidth() * 2);
		Set<PolyRoad> oneWayRoads = new HashSet<>();
		graphicsContext.save();
		if (ShowExplored) {
			graphicsContext.setStroke(Color.RED);
			for (PolyRoad road : Dijkstra.getLastVisitedRoads()) {
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
