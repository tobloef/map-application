package bfst19.osmdrawing.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class DrawingInfo {
    private Color fillColor;
    private Color strokeColor;
    private double lineDash;
    private double lineWidth;
    private double zoomLevel;
    private boolean alwaysDraw;
    private ImagePattern texture;

    public DrawingInfo(
            Color fillColor,
            Color strokeColor,
            double lineDash,
            double lineWidth,
            double zoomLevel,
            boolean alwaysDraw,
            ImagePattern texture
    ) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.lineDash = lineDash;
        this.lineWidth = lineWidth;
        this.zoomLevel = zoomLevel;
        this.alwaysDraw = alwaysDraw;
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

    public double getZoomLevel() {
        return zoomLevel;
    }

    public boolean getAlwaysDraw() { return alwaysDraw; }

    public ImagePattern getTexture() {
        return texture;
    }
}
