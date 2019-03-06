package bfst19.osmdrawing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BasicDrawableModel implements DrawableModel {

	Map<WayType, List<Drawable>> wayTypeEnumMap = new EnumMap<>(WayType.class);


	public BasicDrawableModel(){
		initializeWaysEnumMap();
	}

	@Override
	public void add(WayType type, Drawable drawable) {
		wayTypeEnumMap.get(type).add(drawable);
	}

	@Override
	public Iterable<Drawable> getDrawablesOfType(WayType type, ModelBounds bounds) {
		return wayTypeEnumMap.get(type);
	}

	@Override
	public void doneAdding() {
		return; //Does nothing in this model, but is needed for more complex models.
	}

	private void initializeWaysEnumMap() {
		wayTypeEnumMap = new EnumMap<>(WayType.class);
		for (WayType type : WayType.values()) {
			wayTypeEnumMap.put(type, new ArrayList<>());
		}
	}
}
