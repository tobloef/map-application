package bfst19.danmarkskort.model;

import bfst19.danmarkskort.utils.EnumHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KDTreeDrawableModel implements DrawableModel {
	Map<WayType, List<Drawable>> wayTypeEnumMap = EnumHelper.createWayTypeDrawablesMap();
	Map<WayType, KDTree> wayTypeToKDTreeRoot;
	Rectangle modelBounds;

	public KDTreeDrawableModel(){

	}

	private void initializeKDTree(){
		countFloats();
		wayTypeToKDTreeRoot = new HashMap<>();
		for (WayType wayType : WayType.values()){
			List<Drawable> drawables = wayTypeEnumMap.get(wayType);
			if (drawables.size() > 0) {
				KDTree newTree = new KDTree(drawables);
				wayTypeToKDTreeRoot.put(wayType, newTree);
			}
		}
	}

	@Override
	public void add(WayType type, Drawable drawable) {
		wayTypeEnumMap.get(type).add(drawable);
	}

	@Override
	public Iterable<Drawable> getDrawablesOfTypeInBounds(WayType type, Rectangle bounds) {
		if (wayTypeToKDTreeRoot == null) {
			throw new RuntimeException("The KDTree has not been initialized yet");
		}
		if (wayTypeToKDTreeRoot.containsKey(type))
			return wayTypeToKDTreeRoot.get(type).rangeSearch(bounds, new ArrayList<Drawable>());
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

	@Override
	public void doNewDataSet() {
		if (wayTypeEnumMap == null) {
			wayTypeEnumMap = EnumHelper.createWayTypeDrawablesMap();
		}
		initializeKDTree();
	}


	private void countFloats() {
		//System.out.println("Number of floats for each type");
		for (WayType wayType : WayType.values()){
			long numOfFloats = 0;
			for (Drawable drawable : wayTypeEnumMap.get(wayType)){
				numOfFloats += drawable.getNumOfFloats();
			}
			//System.out.println( wayTypeEnumMap.get(wayType).size() + " of " + wayType.name() + " having : " + numOfFloats + " floats");
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

	@Override
	public Drawable getNearestNeighbor(WayType type, float x, float y) {
		if (wayTypeToKDTreeRoot.containsKey(type)) {
			SpatialIndexable drawable = wayTypeToKDTreeRoot.get(type).getNearestNeighbor(x, y);
			if (drawable != null) {
				return (Drawable) drawable;
			}
		}
		return null ;
	}

	@Override
	public void insert(WayType type,Drawable drawable){
		if(wayTypeToKDTreeRoot.containsKey(type)){
			if (drawable instanceof SpatialIndexable){
				wayTypeToKDTreeRoot.get(type).insert((SpatialIndexable)drawable);
			}
			else{
				throw new IllegalArgumentException("tried inserting drawable which was not spatialindexable");
			}
		}
		else {
			List<Drawable> tempList = new ArrayList<>();
			tempList.add(drawable);
			wayTypeToKDTreeRoot.put(type, new KDTree(tempList));
		}
	}

}
