package bfst19.danmarkskort.model;

import java.io.Serializable;

public interface DrawableModel extends Serializable {
	void add(WayType type, Drawable drawable);
	Iterable<Drawable> getDrawablesOfTypeInBounds(WayType type, Rectangle bounds);
	Iterable<Drawable> getAllDrawablesOfType(WayType type);
	void doneAdding();
	void setModelBounds(Rectangle bounds);
	Rectangle getModelBounds();
	Drawable getNearestNeighbor(WayType type, float x, float y);
	void insert(WayType type, Drawable drawable);

}
