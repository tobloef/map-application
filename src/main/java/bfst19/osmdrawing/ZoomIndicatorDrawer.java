package bfst19.osmdrawing;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;


public class ZoomIndicatorDrawer implements Drawer {
	private MapCanvas canvas;
	private GraphicsContext graphicsContext;

	public ZoomIndicatorDrawer(MapCanvas canvas) {
		this.canvas = canvas;
		this.graphicsContext = canvas.getGraphicsContext2D();
	}

	@Override
	public void draw() {
		int margin = 5;
		int boxWidth = 100;
		int boxHeight = 30;
		double distance = (boxHeight - (margin * 2)) * canvas.getZoomFactor() * 111;

		Font font = new Font(10);
		Affine affine = graphicsContext.getTransform();
		double oldLineWidth = graphicsContext.getLineWidth();
		graphicsContext.setTransform(new Affine());
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillRect(margin, canvas.getHeight() - (margin + boxHeight + boxHeight), boxWidth, boxHeight);
		graphicsContext.setFill(Color.BLACK);
		graphicsContext.setLineWidth(1);
		graphicsContext.moveTo(margin + boxWidth - margin, margin + margin); //left line, top side
		graphicsContext.lineTo(margin + margin, margin + margin); // left line, bottom side
		graphicsContext.lineTo(margin + margin, margin + boxHeight - margin); //right line, bottom side
		graphicsContext.lineTo(margin + boxWidth - margin, margin + boxHeight - margin); // right line, top side
		graphicsContext.stroke();
		graphicsContext.setLineWidth(0);
		graphicsContext.setFont(font);
		graphicsContext.strokeText(String.format("%.1f km", distance), margin + boxWidth / 2f - 2, margin + boxHeight / 2f);
		graphicsContext.setLineWidth(oldLineWidth);
		graphicsContext.setTransform(affine);
	}
}
