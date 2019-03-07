package bfst19.osmdrawing.Model;

import bfst19.osmdrawing.Drawable;
import bfst19.osmdrawing.WayType;

import java.io.Serializable;

public interface DrawableModel extends Serializable {
	void add(WayType type, Drawable drawable);
	Iterable<Drawable> getDrawablesOfType(WayType type, Rectangle bounds);
	void doneAdding();
	void setModelBounds(Rectangle bounds);
	Rectangle getModelBounds();
}
