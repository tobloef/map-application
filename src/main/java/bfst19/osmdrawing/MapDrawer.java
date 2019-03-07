package bfst19.osmdrawing;

import javafx.scene.canvas.GraphicsContext;

public class MapDrawer implements Drawer {
	private GraphicsContext graphicsContext;
	private Model model;

	public MapDrawer(GraphicsContext graphicsContext, Model model) {
		this.graphicsContext = graphicsContext;
		this.model = model;
	}

	public void draw() {
		for (WayType wayType : WayType.values()){
			if (wayType.hasFill()) {
				graphicsContext.setFill(wayType.getFillColor());
				for (Drawable way : model.getWaysOfType(wayType)) {
					way.fill(graphicsContext);
				}
			}
			if (wayType.hasStroke()) {
				graphicsContext.setLineDashes(wayType.getLineDash() / 10000);
				graphicsContext.setStroke(wayType.getStrokeColor());
				for (Drawable way : model.getWaysOfType(wayType)){
					way.stroke(graphicsContext);
				}
			}
		}
	}
}
