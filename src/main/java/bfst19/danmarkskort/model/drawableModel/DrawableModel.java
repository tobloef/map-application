package bfst19.danmarkskort.model.drawableModel;

import bfst19.danmarkskort.model.WayType;
import bfst19.danmarkskort.model.drawables.Drawable;

import java.io.Serializable;

public interface DrawableModel extends Serializable {
    void add(WayType type, Drawable drawable);

    Iterable<Drawable> getDrawablesOfTypeInBounds(WayType type, Rectangle bounds);

    Iterable<Drawable> getAllDrawablesOfType(WayType type);

    void doneAdding();

    void doNewDataSet();

    Rectangle getModelBounds();

    void setModelBounds(Rectangle bounds);

    Drawable getNearestNeighbor(WayType type, float x, float y);

    void insert(WayType type, Drawable drawable);

}
