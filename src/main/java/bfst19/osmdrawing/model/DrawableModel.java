package bfst19.osmdrawing.model;

import bfst19.osmdrawing.view.WayType;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public interface DrawableModel extends Serializable {
	void add(WayType type, Drawable drawable);
	Iterable<Drawable> getDrawablesOfType(WayType type, Rectangle bounds);
	void doneAdding();
	void setModelBounds(Rectangle bounds);
	Rectangle getModelBounds();


}
