package bfst19.osmdrawing.model;

import bfst19.osmdrawing.view.WayType;
import java.util.*;

public class KDTreeDrawableModel implements DrawableModel {
	Map<WayType, List<Drawable>> wayTypeEnumMap = DrawableModel.initializeWaysEnumMap();
	Map<WayType, KDTree> wayTypeToKDTreeRoot;
	Rectangle modelBounds;

	public KDTreeDrawableModel(){

	}

	@Override
	public void add(WayType type, Drawable drawable) {
		wayTypeEnumMap.get(type).add(drawable);
	}

	@Override
	public Iterable<Drawable> getDrawablesOfType(WayType type, Rectangle bounds) {
		if (wayTypeToKDTreeRoot.containsKey(type))
			if (!type.alwaysDraw())
				return wayTypeToKDTreeRoot.get(type).rangeQuery(bounds, new ArrayList<Drawable>());
			else {
				return wayTypeToKDTreeRoot.get(type).getContent(new ArrayList<Drawable>());
			}
		else {
			return new ArrayList<>();
		}
	}

	@Override
	public void doneAdding() {
		initializeKDTree();
		wayTypeEnumMap = null;
		return;
	}

	private void initializeKDTree(){
		wayTypeToKDTreeRoot = new HashMap<>();
		for (WayType wayType : WayType.values()){
			List<Drawable> drawables = wayTypeEnumMap.get(wayType);
			if (drawables.size() > 0) {
				KDTree newTree = new KDTree(drawables, getModelBounds());
				wayTypeToKDTreeRoot.put(wayType, newTree);
			}
		}
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
