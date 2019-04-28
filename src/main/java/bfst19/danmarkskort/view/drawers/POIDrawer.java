package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.model.Drawable;
import bfst19.danmarkskort.model.Model;
import bfst19.danmarkskort.model.PointOfInterest;
import bfst19.danmarkskort.model.WayType;
import bfst19.danmarkskort.view.controls.MapCanvas;
import javafx.scene.canvas.GraphicsContext;

public class POIDrawer implements Drawer{
	private MapCanvas canvas;
	private Model model;
	public POIDrawer (MapCanvas mapCanvas, Model model){
		this.canvas = mapCanvas;
		this.model = model;
	}

	@Override
	public void draw() {
		if (model.dontDraw(WayType.POI)){
			return;
		}
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		double zoomFactor = canvas.getDegreesLatitudePerPixel();
		for (Drawable drawable : model.getWaysOfType(WayType.POI, canvas.getScreenBounds())){
			drawable.fill(graphicsContext,zoomFactor);
		}
	}
}