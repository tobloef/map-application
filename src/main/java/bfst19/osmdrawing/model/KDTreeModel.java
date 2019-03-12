package bfst19.osmdrawing.model;

import bfst19.osmdrawing.view.WayType;

import java.util.List;
import java.util.Map;

public class KDTreeModel implements DrawableModel {
	Map<WayType, List<Drawable>> wayTypeEnumMap;
	Map<WayType, KDTreeNode> wayTypeToKDTreeRoot;
	Rectangle modelBounds;

	private class KDTreeNode{

	}

	public KDTreeModel(){
		DrawableModel.initializeWaysEnumMap(wayTypeEnumMap);
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
		//Construct the tree
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
