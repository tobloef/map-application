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
	private int outerMargin = 5;
	private int innerMargin = outerMargin;
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
		double y = canvas.getHeight() - (outerMargin + boxHeight + bottomOffset);
		double x = outerMargin;
		double w = boxWidth;
		double h = boxHeight;
		graphicsContext.fillRect(x, y, w, h);
	}

	private void drawText() {
		double distance = calculateDistance();
		String unit = "km";
		if (distance < 1) {
			distance *= 1000;
			unit = "m";
		}
		renderText(distance, unit);
	}

	private double calculateDistance() {
		int pixelLength = boxWidth - (innerMargin * 2);
		/*
		double radiusOfEarth = 6371; // For use when we start taking longitude into account
		double latitude = canvas.getBoundsInLocal().getMaxY(); //based on the bottom of the viewed window
		double kilometersPerLon = Math.PI / 180 * radiusOfEarth * Math.cos(latitude);
		*/
		int kilometersPerDegree = 111;
		return pixelLength * canvas.getZoomFactor() * kilometersPerDegree;
	}

	private void renderText(double distance, String unit) {
		Font font = new Font(10);
		graphicsContext.setLineWidth(0);
		graphicsContext.setFont(font);
		graphicsContext.setTextAlign(TextAlignment.CENTER);
		String text = String.format("%.1f " + unit, distance);
		double x = outerMargin + boxWidth / 2f;
		double y = canvas.getHeight() - (bottomOffset + outerMargin + boxHeight / 2f);
		graphicsContext.strokeText(text, x, y);
	}

	private void drawBlackBars() {
		graphicsContext.setFill(Color.BLACK);
		graphicsContext.setLineWidth(1);
		int barsLeftEdge = outerMargin + innerMargin;
		int barsRightEdge = outerMargin + boxWidth - innerMargin;
		double barsTopEdge = canvas.getHeight() - (outerMargin + boxHeight + bottomOffset) + innerMargin;
		double barsBottomEdge = canvas.getHeight() - outerMargin - innerMargin - bottomOffset;
		graphicsContext.moveTo(barsLeftEdge, barsTopEdge); // left line, top side
		graphicsContext.lineTo(barsLeftEdge, barsBottomEdge); // left line, bottom side
		graphicsContext.lineTo(barsRightEdge, barsBottomEdge); //right line, bottom side
		graphicsContext.lineTo(barsRightEdge, barsTopEdge); //right line, top side
		graphicsContext.stroke();
	}
}
