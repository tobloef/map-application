package bfst19.osmdrawing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class ZoomIndicatorDrawer implements Drawer {
	private GraphicsContext graphicsContext;
	private int margin = 10;
	private int boxWidth = 30;
	private int boxHeight = 100;

	public ZoomIndicatorDrawer(GraphicsContext graphicsContext) {
		this.graphicsContext = graphicsContext;
	}

	@Override
	public void draw() {
		Affine affine = graphicsContext.getTransform();
		double oldLineWidth = graphicsContext.getLineWidth();
		graphicsContext.setTransform(new Affine());
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillRect(margin, margin, boxWidth, boxHeight);
		graphicsContext.setLineWidth(1);
		graphicsContext.setFill(Color.BLACK);
		graphicsContext.moveTo(margin + boxWidth - margin, margin + margin); //top line, right side
		graphicsContext.lineTo(margin + margin, margin + margin); // top line, left side
		graphicsContext.lineTo(margin + margin, margin + boxHeight - margin); //bottom line, left side
		graphicsContext.lineTo(margin + boxWidth - margin, margin + boxHeight - margin); // bottom line right side
		graphicsContext.setLineWidth(oldLineWidth);
		graphicsContext.setTransform(affine);
	}
}
