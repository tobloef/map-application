package bfst19.osmdrawing.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DrawingInfo {
    private Color fillColor;
    private Color strokeColor;
    private Double lineDash;
    private Double lineWidth;
    private Double zoomLevel;
    private Boolean alwaysDraw;
    private ImagePattern texture;

    private boolean fillColorSet;
    private boolean strokeColorSet;
    private boolean lineDashSet;
    private boolean lineWidthSet;
    private boolean zoomLevelSet;
    private boolean alwaysDrawSet;
    private boolean textureSet;

    public DrawingInfo() {
    }

    DrawingInfo createMerged(DrawingInfo other) {
        DrawingInfo result = new DrawingInfo();
        setMergeProp(result::setFillColor, this::getFillColor, this::isFillColorSet, other::getFillColor, other::isFillColorSet);
        setMergeProp(result::setFillColor, this::getFillColor, this::isFillColorSet, other::getFillColor, other::isFillColorSet);
        setMergeProp(result::setStrokeColor, this::getStrokeColor, this::isStrokeColorSet, other::getStrokeColor, other::isStrokeColorSet);
        setMergeProp(result::setLineDash, this::getLineDash, this::isLineDashSet, other::getLineDash, other::isLineDashSet);
        setMergeProp(result::setLineWidth, this::getLineWidth, this::isLineWidthSet, other::getLineWidth, other::isLineWidthSet);
        setMergeProp(result::setZoomLevel, this::getZoomLevel, this::isZoomLevelSet, other::getZoomLevel, other::isZoomLevelSet);
        setMergeProp(result::setAlwaysDraw, this::getAlwaysDraw, this::isAlwaysDrawSet, other::getAlwaysDraw, other::isAlwaysDrawSet);
        setMergeProp(result::setTexture, this::getTexture, this::isTextureSet, other::getTexture, other::isTextureSet);
        return result;
    }

    /***
     * Sets a property based on whether another object's property has been set.
     *
     * For example:
     * setMergeProp(
     *      result::setFillColor,
     *      this::getFillColor,
     *      this::isFillColorSet,
     *      other::getFillColor,
     *      other::isFillColorSet
     * );
     *
     * @param setter Setter for the resulting property
     * @param getter1 Getter for the first objects property
     * @param isSet1 Getter to check whether the first object's property has been set
     * @param getter2 Getter for the second objects property
     * @param isSet2 Getter to check whether the second object's property has been set
     * @param <T> Type of the property
     */
    private <T> void setMergeProp(
            BiConsumer<T, Boolean> setter,
            Supplier<T> getter1,
            Supplier<Boolean> isSet1,
            Supplier<T> getter2,
            Supplier<Boolean> isSet2
    ) {
        if (isSet2.get()) {
            setter.accept(getter2.get(), isSet2.get());
        } else {
            setter.accept(getter1.get(), isSet1.get());
        }
    }

    // Getters

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

    private Boolean isFillColorSet() {
        return fillColorSet;
    }

    private Boolean isStrokeColorSet() {
        return strokeColorSet;
    }

    private Boolean isLineDashSet() {
        return lineDashSet;
    }

    private Boolean isLineWidthSet() {
        return lineWidthSet;
    }

    private Boolean isZoomLevelSet() {
        return zoomLevelSet;
    }

    private Boolean isAlwaysDrawSet() {
        return alwaysDrawSet;
    }

    private Boolean isTextureSet() {
        return textureSet;
    }

    // Setters

    public void setFillColor(Color fillColor, boolean isFillColorSet) {
        this.fillColor = fillColor;
        fillColorSet = isFillColorSet;
    }

    public void setStrokeColor(Color strokeColor, boolean isStrokeColorSet) {
        this.strokeColor = strokeColor;
        strokeColorSet = isStrokeColorSet;
    }

    public void setLineDash(Double lineDash, boolean isLineDashSet) {
        this.lineDash = lineDash;
        lineDashSet = isLineDashSet;
    }

    public void setLineWidth(Double lineWidth, boolean isLineWidthSet) {
        this.lineWidth = lineWidth;
        lineWidthSet = isLineWidthSet;
    }

    public void setZoomLevel(Double zoomLevel, boolean isZoomLevelSet) {
        this.zoomLevel = zoomLevel;
        zoomLevelSet = isZoomLevelSet;
    }

    public void setAlwaysDraw(Boolean alwaysDraw, boolean isAlwaysDrawSet) {
        this.alwaysDraw = alwaysDraw;
        alwaysDrawSet = isAlwaysDrawSet;
    }

    public void setTexture(ImagePattern texture, boolean isTextureSet) {
        this.texture = texture;
        textureSet = isTextureSet;
    }

    // Hassers

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
