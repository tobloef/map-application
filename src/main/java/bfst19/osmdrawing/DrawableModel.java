package bfst19.osmdrawing;

import java.io.Serializable;

public interface DrawableModel extends Serializable {
	void add(WayType type, Drawable drawable);
	Iterable<Drawable> getDrawablesOfType(WayType type, ModelBounds bounds);
	void doneAdding();
}
