package bfst19.osmdrawing.model;

import bfst19.osmdrawing.view.WayType;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BasicDrawableModel implements DrawableModel {

	Map<WayType, List<Drawable>> wayTypeEnumMap = DrawableModel.initializeWaysEnumMap();
	Rectangle modelBounds;


	public BasicDrawableModel(){

	}

	@Override
	public void add(WayType type, Drawable drawable) {
		wayTypeEnumMap.get(type).add(drawable);
	}

	@Override
	public Iterable<Drawable> getDrawablesOfType(WayType type, Rectangle bounds) {
		return wayTypeEnumMap.get(type);
	}

	@Override
	public void doneAdding() {
		return; //Does nothing in this model, but is needed for more complex models.
	}

	@Override
	public void setModelBounds(Rectangle bounds) {
		modelBounds = bounds;
	}

	@Override
	public Rectangle getModelBounds() {
		return modelBounds;
	}

}
