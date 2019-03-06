package bfst19.osmdrawing;

public interface DrawableModel {
	void add(WayType type, Drawable drawable);
	Iterable<Drawable> getDrawablesOfType(WayType type, ModelBounds bounds);
}
