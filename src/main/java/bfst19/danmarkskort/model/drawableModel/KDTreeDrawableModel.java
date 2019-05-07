package bfst19.danmarkskort.model.drawableModel;

import bfst19.danmarkskort.model.drawables.DrawableType;
import bfst19.danmarkskort.model.drawables.Drawable;
import bfst19.danmarkskort.utils.EnumHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KDTreeDrawableModel implements DrawableModel {
    Map<DrawableType, List<Drawable>> wayTypeEnumMap = EnumHelper.createWayTypeDrawablesMap();
    Map<DrawableType, KDTree> wayTypeToKDTreeRoot;
    Rectangle modelBounds;

    public KDTreeDrawableModel() {

    }

    private void initializeKDTree() {
        countFloats();
        wayTypeToKDTreeRoot = new HashMap<>();
        for (DrawableType drawableType : DrawableType.values()) {
            List<Drawable> drawables = wayTypeEnumMap.get(drawableType);
            if (drawables.size() > 0) {
                KDTree newTree = new KDTree(drawables);
                wayTypeToKDTreeRoot.put(drawableType, newTree);
            }
        }
    }

    @Override
    public void add(DrawableType type, Drawable drawable) {
        wayTypeEnumMap.get(type).add(drawable);
    }

    @Override
    public Iterable<Drawable> getDrawablesOfTypeInBounds(DrawableType type, Rectangle bounds) {
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
    public Iterable<Drawable> getAllDrawablesOfType(DrawableType type) {
        if (wayTypeToKDTreeRoot.containsKey(type)) {
            return wayTypeToKDTreeRoot.get(type).getContent(new ArrayList<Drawable>());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void doneAdding() {
        initializeKDTree();
        wayTypeEnumMap = null;
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
        for (DrawableType drawableType : DrawableType.values()) {
            long numOfFloats = 0;
            for (Drawable drawable : wayTypeEnumMap.get(drawableType)) {
                numOfFloats += drawable.getNumOfFloats();
            }
            //System.out.println( wayTypeEnumMap.get(drawableType).size() + " of " + drawableType.name() + " having : " + numOfFloats + " floats");
        }
    }

    @Override
    public Rectangle getModelBounds() {
        return modelBounds;
    }

    @Override
    public void setModelBounds(Rectangle bounds) {
        modelBounds = bounds;
    }

    @Override
    public Drawable getNearestNeighbor(DrawableType type, float x, float y) {
        if (wayTypeToKDTreeRoot.containsKey(type)) {
            SpatialIndexable drawable = wayTypeToKDTreeRoot.get(type).getNearestNeighbor(x, y);
            if (drawable != null) {
                return (Drawable) drawable;
            }
        }
        return null;
    }

    @Override
    public void insert(DrawableType type, Drawable drawable) {
        if (!wayTypeToKDTreeRoot.containsKey(type)) {
            wayTypeToKDTreeRoot.put(type, new KDTree(new ArrayList<Drawable>()));
        }
        if (drawable instanceof SpatialIndexable) {
            wayTypeToKDTreeRoot.get(type).insert((SpatialIndexable) drawable);
        } else {
            throw new IllegalArgumentException("tried inserting drawable which was not spatialindexable");
        }
    }

}
