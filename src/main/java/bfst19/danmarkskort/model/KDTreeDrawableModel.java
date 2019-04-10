package bfst19.danmarkskort.model;

import bfst19.danmarkskort.utils.EnumHelper;
import java.util.*;

public class KDTreeDrawableModel implements DrawableModel {
	Map<WayType, List<Drawable>> wayTypeEnumMap = EnumHelper.createWayTypeDrawablesMap();
	Map<WayType, KDTree> wayTypeToKDTreeRoot;
	Rectangle modelBounds;

	public KDTreeDrawableModel(){

	}

	@Override
	public void add(WayType type, Drawable drawable) {
		wayTypeEnumMap.get(type).add(drawable);
	}

	@Override
	public Iterable<Drawable> getDrawablesOfTypeInBounds(WayType type, Rectangle bounds) {
		if (wayTypeToKDTreeRoot.containsKey(type))
			return wayTypeToKDTreeRoot.get(type).rangeQuery(bounds, new ArrayList<Drawable>());
		else {
			return new ArrayList<>();
		}
	}

	@Override
	public Iterable<Drawable> getAllDrawablesOfType(WayType type) {
		if (wayTypeToKDTreeRoot.containsKey(type)){
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
		//countFloats();
		wayTypeToKDTreeRoot = new HashMap<>();
		for (WayType wayType : WayType.values()){
			List<Drawable> drawables = wayTypeEnumMap.get(wayType);
			if (drawables.size() > 0) {
				KDTree newTree = new KDTree(drawables, getModelBounds());
				wayTypeToKDTreeRoot.put(wayType, newTree);
			}
		}
	}

	private void countFloats() {
		System.out.println("Number of floats for each type");
		for (WayType wayType : WayType.values()){
			long numOfFloats = 0;
			for (Drawable drawable : wayTypeEnumMap.get(wayType)){
				numOfFloats += drawable.getNumOfFloats();
			}
			System.out.println(wayType.name() + ": " + numOfFloats);
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
