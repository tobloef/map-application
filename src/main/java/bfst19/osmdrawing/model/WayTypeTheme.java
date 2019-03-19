package bfst19.osmdrawing.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class WayTypeTheme {
    private Color fillColor;
    private Color strokeColor;
    private double lineDash;
    private double lineWidth;
    private ImagePattern texture;

    public WayTypeTheme(
            Color fillColor,
            Color strokeColor,
            double lineDash,
            double lineWidth,
            ImagePattern texture
    ) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.lineDash = lineDash;
        this.lineWidth = lineWidth;
        this.texture = texture;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public double getLineDash() {
        return lineDash;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public ImagePattern getTexture() {
        return texture;
    }
}
