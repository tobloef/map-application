package bfst19.osmdrawing.model;


import bfst19.osmdrawing.utils.EnumHelper;

import java.util.List;
import java.util.Map;

public class BasicDrawableModel implements DrawableModel {

	Map<WayType, List<Drawable>> wayTypeEnumMap = EnumHelper.createWayTypeDrawablesMap();
	Rectangle modelBounds;


	public BasicDrawableModel(){

	}

	@Override
	public void add(WayType type, Drawable drawable) {
		wayTypeEnumMap.get(type).add(drawable);
	}

	@Override
	public Iterable<Drawable> getDrawablesOfTypeInBounds(WayType type, Rectangle bounds) {
		return wayTypeEnumMap.get(type);
	}

	@Override
	public Iterable<Drawable> getAllDrawablesOfType(WayType type) {
		return wayTypeEnumMap.get(type);
	}

	@Override
	public void doneAdding() {
		//Does nothing in this model, but is needed for more complex models.
		return;
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
