package bfst19.danmarkskort.model.drawables;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import static bfst19.danmarkskort.utils.Misc.pickNotNull;

public class DrawingInfo {
    private final NullWrapper<Color> fillColorNullWrapper;
    private final NullWrapper<Color> strokeColorNullWrapper;
    private final NullWrapper<Double> lineDashNullWrapper;
    private final NullWrapper<Double> lineWidthNullWrapper;
    private final NullWrapper<Double> lineWidthMaxNullWrapper;
    private final NullWrapper<Double> lineWidthMinNullWrapper;
    private final NullWrapper<Double> zoomLevelNullWrapper;
    private final NullWrapper<Boolean> alwaysDrawNullWrapper;
    private final NullWrapper<ImagePattern> textureNullWrapper;

    public DrawingInfo(
            NullWrapper<Color> fillColorNullWrapper,
            NullWrapper<Color> strokeColorNullWrapper,
            NullWrapper<Double> lineDashNullWrapper,
            NullWrapper<Double> lineWidthNullWrapper,
            NullWrapper<Double> lineWidthMaxNullWrapper,
            NullWrapper<Double> lineWidthMinNullWrapper,
            NullWrapper<Double> zoomLevelNullWrapper,
            NullWrapper<Boolean> alwaysDrawNullWrapper,
            NullWrapper<ImagePattern> textureNullWrapper
    ) {
        this.fillColorNullWrapper = fillColorNullWrapper;
        this.strokeColorNullWrapper = strokeColorNullWrapper;
        this.lineDashNullWrapper = lineDashNullWrapper;
        this.lineWidthNullWrapper = lineWidthNullWrapper;
        this.lineWidthMaxNullWrapper = lineWidthMaxNullWrapper;
        this.lineWidthMinNullWrapper = lineWidthMinNullWrapper;
        this.zoomLevelNullWrapper = zoomLevelNullWrapper;
        this.alwaysDrawNullWrapper = alwaysDrawNullWrapper;
        this.textureNullWrapper = textureNullWrapper;
    }

    public DrawingInfo createMerged(DrawingInfo other) {
        return new DrawingInfo(
                pickNotNull(other.getFillColorNullWrapper(), this.getFillColorNullWrapper()),
                pickNotNull(other.getStrokeColorNullWrapper(), this.getStrokeColorNullWrapper()),
                pickNotNull(other.getLineDashNullWrapper(), this.getLineDashNullWrapper()),
                pickNotNull(other.getLineWidthNullWrapper(), this.getLineWidthNullWrapper()),
                pickNotNull(other.getLineWidthMaxNullWrapper(), this.getLineWidthMaxNullWrapper()),
                pickNotNull(other.getLineWidthMinNullWrapper(), this.getLineWidthMinNullWrapper()),
                pickNotNull(other.getZoomLevelNullWrapper(), this.getZoomLevelNullWrapper()),
                pickNotNull(other.getAlwaysDrawNullWrapper(), this.getAlwaysDrawNullWrapper()),
                pickNotNull(other.getTextureNullWrapper(), this.getTextureNullWrapper())
        );
    }

    // Getters

    public Color getFillColor() {
        if (fillColorNullWrapper == null) {
            return null;
        }
        return fillColorNullWrapper.getValue();
    }

    public Color getStrokeColor() {
        if (strokeColorNullWrapper == null) {
            return null;
        }
        return strokeColorNullWrapper.getValue();
    }

    public Double getLineDash() {
        if (lineDashNullWrapper == null) {
            return null;
        }
        return lineDashNullWrapper.getValue();
    }

    public Double getLineWidth() {
        if (lineWidthNullWrapper == null) {
            return null;
        }
        return lineWidthNullWrapper.getValue();
    }

    public Double getZoomLevel() {
        if (zoomLevelNullWrapper == null) {
            return null;
        }
        return zoomLevelNullWrapper.getValue();
    }

    public Boolean getAlwaysDraw() {
        if (alwaysDrawNullWrapper == null) {
            return null;
        }
        return alwaysDrawNullWrapper.getValue();
    }

    public ImagePattern getTexture() {
        if (textureNullWrapper == null) {
            return null;
        }
        return textureNullWrapper.getValue();
    }

    public NullWrapper<Color> getFillColorNullWrapper() {
        return fillColorNullWrapper;
    }

    public NullWrapper<Color> getStrokeColorNullWrapper() {
        return strokeColorNullWrapper;
    }

    public NullWrapper<Double> getLineDashNullWrapper() {
        return lineDashNullWrapper;
    }

    public NullWrapper<Double> getLineWidthNullWrapper() {
        return lineWidthNullWrapper;
    }

    public NullWrapper<Double> getLineWidthMaxNullWrapper() {
        return lineWidthMaxNullWrapper;
    }

    public NullWrapper<Double> getLineWidthMinNullWrapper() {
        return lineWidthMinNullWrapper;
    }

    public NullWrapper<Double> getZoomLevelNullWrapper() {
        return zoomLevelNullWrapper;
    }

    public NullWrapper<Boolean> getAlwaysDrawNullWrapper() {
        return alwaysDrawNullWrapper;
    }

    public NullWrapper<ImagePattern> getTextureNullWrapper() {
        return textureNullWrapper;
    }

    // Hassers

    public boolean hasFillColor() {
        return fillColorNullWrapper != null && fillColorNullWrapper.getValue() != null;
    }

    public boolean hasStrokeColor() {
        return strokeColorNullWrapper != null && strokeColorNullWrapper.getValue() != null;
    }

    public boolean hasLineDash() {
        return lineDashNullWrapper != null && lineDashNullWrapper.getValue() != null;
    }

    public boolean hasLineWidth() {
        return lineWidthNullWrapper != null && lineWidthNullWrapper.getValue() != null;
    }

    public boolean hasZoomLevel() {
        return zoomLevelNullWrapper != null && zoomLevelNullWrapper.getValue() != null;
    }

    public boolean hasAlwaysDraw() {
        return alwaysDrawNullWrapper != null && alwaysDrawNullWrapper.getValue() != null;
    }

    public boolean hasTexture() {
        return textureNullWrapper != null && textureNullWrapper.getValue() != null;
    }


    public double calculateLineWidth(double currentZoomLevel) {
        if (lineWidthMaxNullWrapper == null || lineWidthMinNullWrapper == null) {
            return lineWidthNullWrapper.getValue();
        }
        double lineWidth = lineWidthNullWrapper.getValue() * currentZoomLevel;
        if (lineWidth < lineWidthMinNullWrapper.getValue()) {
            lineWidth = lineWidthMinNullWrapper.getValue();
        }
        if (lineWidth > lineWidthMaxNullWrapper.getValue()) {
            lineWidth = lineWidthMaxNullWrapper.getValue();
        }
        return lineWidth;
    }
}
