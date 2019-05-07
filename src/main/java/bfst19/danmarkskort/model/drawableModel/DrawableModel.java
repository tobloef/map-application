package bfst19.danmarkskort.model.drawableModel;

import bfst19.danmarkskort.model.drawables.DrawableType;
import bfst19.danmarkskort.model.drawables.Drawable;

import java.io.Serializable;

public interface DrawableModel extends Serializable {
    void add(DrawableType type, Drawable drawable);

    Iterable<Drawable> getDrawablesOfTypeInBounds(DrawableType type, Rectangle bounds);

    Iterable<Drawable> getAllDrawablesOfType(DrawableType type);

    void doneAdding();

    void doNewDataSet();

    Rectangle getModelBounds();

    void setModelBounds(Rectangle bounds);

    Drawable getNearestNeighbor(DrawableType type, float x, float y);

    void insert(DrawableType type, Drawable drawable);

}
