package bfst19.osmdrawing.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class DrawingInfo {
    private Color fillColor;
    private Color strokeColor;
    private Double lineDash;
    private Double lineWidth;
    private Double zoomLevel;
    private Boolean alwaysDraw;
    private ImagePattern texture;

    public DrawingInfo(
            Color fillColor,
            Color strokeColor,
            Double lineDash,
            Double lineWidth,
            Double zoomLevel,
            Boolean alwaysDraw,
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

    public DrawingInfo mergeWith(DrawingInfo other) {
        Color fillColor = tryPickNotNull(other.getFillColor(), this.fillColor);
        Color strokeColor = tryPickNotNull(other.getStrokeColor(), this.strokeColor);
        Double lineDash = tryPickNotNull(other.getLineDash(), this.lineDash);
        Double lineWidth = tryPickNotNull(other.getLineWidth(), this.lineWidth);
        Double zoomLevel = tryPickNotNull(other.getZoomLevel(), this.zoomLevel);
        Boolean alwaysDraw = tryPickNotNull(other.getAlwaysDraw(), this.alwaysDraw);
        ImagePattern texture = tryPickNotNull(other.getTexture(), this.texture);

        return new DrawingInfo(
                fillColor,
                strokeColor,
                lineDash,
                lineWidth,
                zoomLevel,
                alwaysDraw,
                texture
        );
    }

    private <T> T tryPickNotNull(T thing1, T thing2) {
        if (thing1 != null) {
            return thing1;
        }
        return thing2;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public Double getLineDash() {
        return lineDash;
    }

    public Double getLineWidth() {
        return lineWidth;
    }

    public Double getZoomLevel() {
        return zoomLevel;
    }

    public Boolean getAlwaysDraw() {
        if (alwaysDraw == null) {
            return false;
        }
        return alwaysDraw;
    }

    public ImagePattern getTexture() {
        return texture;
    }

    public boolean hasFillColor() {
        return fillColor != null;
    }

    public boolean hasStrokeColor() {
        return strokeColor != null;
    }

    public boolean hasLineDash() {
        return lineDash != null;
    }

    public boolean hasLineWidth() {
        return lineWidth != null;
    }

    public boolean hasZoomLevel() {
        return zoomLevel != null;
    }

    public boolean hasAlwaysDraw() {
        return alwaysDraw != null;
    }

    public boolean hasTexture() {
        return texture != null;
    }

}
