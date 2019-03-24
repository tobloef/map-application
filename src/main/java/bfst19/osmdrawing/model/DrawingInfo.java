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
        Color fillColor = other.getFillColor() != null ? other.getFillColor() : this.fillColor;
        Color strokeColor = other.getStrokeColor() != null ? other.getStrokeColor() : this.strokeColor;
        Double lineDash = other.getLineDash() != null ? other.getLineDash() : this.lineDash;
        Double lineWidth = other.getLineWidth() != null ? other.getLineWidth() : this.lineWidth;
        Double zoomLevel = other.getZoomLevel() != null ? other.getZoomLevel() : this.zoomLevel;
        Boolean alwaysDraw = other.getAlwaysDraw() != null ? other.getAlwaysDraw() : this.alwaysDraw;
        ImagePattern texture = other.getTexture() != null ? other.getTexture() : this.texture;

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
        return alwaysDraw;
    }

    public ImagePattern getTexture() {
        return texture;
    }
}
