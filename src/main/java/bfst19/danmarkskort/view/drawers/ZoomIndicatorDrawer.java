package bfst19.danmarkskort.view.drawers;

import bfst19.danmarkskort.view.MapCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;


public class ZoomIndicatorDrawer implements Drawer {
    private boolean enabled = true;
    private MapCanvas canvas;
    private GraphicsContext graphicsContext;
    private boolean leftAligned = false;
    private boolean topAligned = false;
    private double xOrigin;
    private double yOrigin;
    private int outerMargin = 5;
    private int innerMargin = outerMargin;
    private int boxWidth = 100;
    private int boxHeight = 20;
    private int maxPixelWidth = boxWidth - (innerMargin * 2);


    public ZoomIndicatorDrawer(MapCanvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
    }

    @Override
    public boolean getEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void draw() {
        graphicsContext.save();
        graphicsContext.setTransform(new Affine());
        drawIndicator();
        graphicsContext.restore();
    }

    private void drawIndicator() {
        xOrigin = leftAligned ? outerMargin : canvas.getWidth() - outerMargin - boxWidth;
        yOrigin = topAligned ? outerMargin : canvas.getHeight() - outerMargin - boxHeight;
        drawBackground();
        double maxDistance = calculatePixelsToKm(maxPixelWidth);
        String unit = "km";
        if (maxDistance < 1) {
            maxDistance *= 1000;
            unit = "m";
        }
        double distance = calculateDistance(maxDistance);
        double pixelWidth = maxPixelWidth / maxDistance * distance;
        drawBlackBars(pixelWidth);
        renderText(distance, unit);
    }

    private double calculateDistance(double maxDistance) {
        int numberOfDigits = Double.toString(maxDistance).indexOf('.');
        double magnitude = Math.pow(10, numberOfDigits - 1);
        return Math.floor(maxDistance / magnitude) * magnitude;
    }

    private void drawBackground() {
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setFill(Color.color(1, 1, 1, 0.9));
        graphicsContext.fillRect(xOrigin, yOrigin, boxWidth, boxHeight);
    }

    private double calculatePixelsToKm(int pixels) {
		/*
		double radiusOfEarth = 6371; // For use when we start taking longitude into account
		double latitude = canvas.getBoundsInLocal().getMaxY(); //based on the bottom of the viewed window
		double kilometersPerLon = Math.PI / 180 * radiusOfEarth * Math.cos(latitude);
		*/
        int kilometersPerDegree = 111;
        return pixels * canvas.getDegreesLatitudePerPixel() * kilometersPerDegree;
    }

    private void renderText(double distance, String unit) {
        Font font = new Font(10);
        graphicsContext.setLineWidth(0);
        graphicsContext.setFont(font);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        String text = String.format("%.0f " + unit, distance);
        double x = xOrigin + boxWidth / 2f;
        double y = yOrigin + boxHeight / 2f;
        graphicsContext.strokeText(text, x, y);
    }

    private void drawBlackBars(double width) {
        double newMargin = (boxWidth - width) / 2;
        double barsLeftEdge = xOrigin + newMargin;
        double barsRightEdge = xOrigin + boxWidth - newMargin;
        double barsTopEdge = yOrigin + innerMargin;
        double barsBottomEdge = yOrigin + boxHeight - innerMargin;
        strokeBars(barsLeftEdge, barsRightEdge, barsTopEdge, barsBottomEdge);
    }

    private void strokeBars(double barsLeftEdge, double barsRightEdge, double barsTopEdge, double barsBottomEdge) {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setLineWidth(1);
        graphicsContext.moveTo(barsLeftEdge, barsTopEdge); // left line, top side
        graphicsContext.lineTo(barsLeftEdge, barsBottomEdge); // left line, bottom side
        graphicsContext.lineTo(barsRightEdge, barsBottomEdge); //right line, bottom side
        graphicsContext.lineTo(barsRightEdge, barsTopEdge); //right line, top side
        graphicsContext.stroke();
    }
}
