package bfst19.danmarkskort.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import static bfst19.danmarkskort.utils.Misc.pickNotNull;

public class DrawingInfo {
    private Wrapper<Color> fillColorWrapper;
    private Wrapper<Color> strokeColorWrapper;
    private Wrapper<Double> lineDashWrapper;
    private Wrapper<Double> lineWidthWrapper;
    private Wrapper<Double> zoomLevelWrapper;
    private Wrapper<Boolean> alwaysDrawWrapper;
    private Wrapper<ImagePattern> textureWrapper;

    public DrawingInfo(
            Wrapper<Color> fillColorWrapper,
            Wrapper<Color> strokeColorWrapper,
            Wrapper<Double> lineDashWrapper,
            Wrapper<Double> lineWidthWrapper,
            Wrapper<Double> zoomLevelWrapper,
            Wrapper<Boolean> alwaysDrawWrapper,
            Wrapper<ImagePattern> textureWrapper
    ) {
        this.fillColorWrapper = fillColorWrapper;
        this.strokeColorWrapper = strokeColorWrapper;
        this.lineDashWrapper = lineDashWrapper;
        this.lineWidthWrapper = lineWidthWrapper;
        this.zoomLevelWrapper = zoomLevelWrapper;
        this.alwaysDrawWrapper = alwaysDrawWrapper;
        this.textureWrapper = textureWrapper;
    }

    DrawingInfo createMerged(DrawingInfo other) {
        return new DrawingInfo(
                pickNotNull(other.getFillColorWrapper(), this.getFillColorWrapper()),
                pickNotNull(other.getStrokeColorWrapper(), this.getStrokeColorWrapper()),
                pickNotNull(other.getLineDashWrapper(), this.getLineDashWrapper()),
                pickNotNull(other.getLineWidthWrapper(), this.getLineWidthWrapper()),
                pickNotNull(other.getZoomLevelWrapper(), this.getZoomLevelWrapper()),
                pickNotNull(other.getAlwaysDrawWrapper(), this.getAlwaysDrawWrapper()),
                pickNotNull(other.getTextureWrapper(), this.getTextureWrapper())
        );
    }

    // Getters

    public Color getFillColor() {
        if (fillColorWrapper == null) {
            return null;
        }
        return fillColorWrapper.getValue();
    }

    public Color getStrokeColor() {
        if (strokeColorWrapper == null) {
            return null;
        }
        return strokeColorWrapper.getValue();
    }

    public Double getLineDash() {
        if (lineDashWrapper == null) {
            return null;
        }
        return lineDashWrapper.getValue();
    }

    public Double getLineWidth() {
        if (lineWidthWrapper == null) {
            return null;
        }
        return lineWidthWrapper.getValue();
    }

    public Double getZoomLevel() {
        if (zoomLevelWrapper == null) {
            return null;
        }
        return zoomLevelWrapper.getValue();
    }

    public Boolean getAlwaysDraw() {
        if (alwaysDrawWrapper == null) {
            return null;
        }
        return alwaysDrawWrapper.getValue();
    }

    public ImagePattern getTexture() {
        if (textureWrapper == null) {
            return null;
        }
        return textureWrapper.getValue();
    }

    public Wrapper<Color> getFillColorWrapper() {
        return fillColorWrapper;
    }

    public Wrapper<Color> getStrokeColorWrapper() {
        return strokeColorWrapper;
    }

    public Wrapper<Double> getLineDashWrapper() {
        return lineDashWrapper;
    }

    public Wrapper<Double> getLineWidthWrapper() {
        return lineWidthWrapper;
    }

    public Wrapper<Double> getZoomLevelWrapper() {
        return zoomLevelWrapper;
    }

    public Wrapper<Boolean> getAlwaysDrawWrapper() {
        return alwaysDrawWrapper;
    }

    public Wrapper<ImagePattern> getTextureWrapper() {
        return textureWrapper;
    }

    // Hassers

    public boolean hasFillColor() {
        return fillColorWrapper != null && fillColorWrapper.getValue() != null;
    }

    public boolean hasStrokeColor() {
        return strokeColorWrapper != null && strokeColorWrapper.getValue() != null;
    }

    public boolean hasLineDash() {
        return lineDashWrapper != null && lineDashWrapper.getValue() != null;
    }

    public boolean hasLineWidth() {
        return lineWidthWrapper != null && lineWidthWrapper.getValue() != null;
    }

    public boolean hasZoomLevel() {
        return zoomLevelWrapper != null && zoomLevelWrapper.getValue() != null;
    }

    public boolean hasAlwaysDraw() {
        return alwaysDrawWrapper != null && alwaysDrawWrapper.getValue() != null;
    }

    public boolean hasTexture() {
        return textureWrapper != null && textureWrapper.getValue() != null;
    }

}
