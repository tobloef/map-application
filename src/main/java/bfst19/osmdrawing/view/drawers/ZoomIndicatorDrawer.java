package bfst19.osmdrawing.view.drawers;

import bfst19.osmdrawing.view.controls.MapCanvas;
import bfst19.osmdrawing.view.drawers.Drawer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;


public class ZoomIndicatorDrawer implements Drawer {
	private MapCanvas canvas;
	private GraphicsContext graphicsContext;
	private int margin = 5;
	private int boxWidth = 100;
	private int boxHeight = 20;
	private int bottomOffset = 0;

	public ZoomIndicatorDrawer(MapCanvas canvas) {
		this.canvas = canvas;
		this.graphicsContext = canvas.getGraphicsContext2D();
	}

	@Override
	public void draw() {
		Affine affine = graphicsContext.getTransform();
		double oldLineWidth = graphicsContext.getLineWidth();
		graphicsContext.setTransform(new Affine());
		drawIndicator();
		graphicsContext.setLineWidth(oldLineWidth);
		graphicsContext.setTransform(affine);
	}

	private void drawIndicator() {
		drawBackground();
		drawBlackBars();
		drawText();
	}

	private void drawBackground() {
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillRect(margin, canvas.getHeight() - (margin + boxHeight + bottomOffset), boxWidth, boxHeight);
	}

	private void drawText() {
		double distance = (boxWidth - (margin * 2)) * canvas.getZoomFactor() * 111;
		String unit = "km";
		if (distance < 1) {
			distance *= 1000;
			unit = "m";
		}
		renderText(distance, unit);
	}

	private void renderText(double distance, String unit) {
		Font font = new Font(10);
		graphicsContext.setLineWidth(0);
		graphicsContext.setFont(font);
		graphicsContext.setTextAlign(TextAlignment.CENTER);
		graphicsContext.strokeText(String.format("%.1f " + unit, distance), margin + boxWidth / 2f, canvas.getHeight() - (bottomOffset + margin + boxHeight / 2f));
	}

	private void drawBlackBars() {
		graphicsContext.setFill(Color.BLACK);
		graphicsContext.setLineWidth(1);
		graphicsContext.moveTo(margin + margin, canvas.getHeight() - (margin + boxHeight + bottomOffset) + margin); // left line, top side
		graphicsContext.lineTo(margin + margin, canvas.getHeight() - (margin + boxHeight + bottomOffset) + boxHeight - margin); //left line, bottom side
		graphicsContext.lineTo(margin + boxWidth - margin, canvas.getHeight() - (margin + boxHeight + bottomOffset) + boxHeight - margin); // right line, bottom side
		graphicsContext.lineTo(margin + boxWidth - margin, canvas.getHeight() - (margin + boxHeight + bottomOffset) + margin); //right line, top side
		graphicsContext.stroke();
	}
}
