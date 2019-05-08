package bfst19.danmarkskort.model.drawableModel;

import bfst19.danmarkskort.model.drawables.Drawable;
import bfst19.danmarkskort.model.drawables.DrawableType;

import java.io.Serializable;

public interface DrawableModel extends Serializable {
    void add(DrawableType type, Drawable drawable);

    Iterable<Drawable> getDrawablesOfTypeInBounds(DrawableType type, Rectangle bounds);

    Iterable<Drawable> getAllDrawablesOfType(DrawableType type);

    void doneAdding();

    Rectangle getModelBounds();

    void setModelBounds(Rectangle bounds);

    Drawable getNearestNeighbor(DrawableType type, float x, float y);

    void insert(DrawableType type, Drawable drawable);

}
