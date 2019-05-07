package bfst19.danmarkskort.model.drawableModel;

import bfst19.danmarkskort.model.drawables.DrawableType;
import bfst19.danmarkskort.model.drawables.Drawable;
import bfst19.danmarkskort.utils.EnumHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KDTreeDrawableModel implements DrawableModel {
    Map<DrawableType, List<Drawable>> drawableTypeEnumMap = EnumHelper.createDrawableTypeDrawablesMap();
    Map<DrawableType, KDTree> drawableTypeToKDTreeRoot;
    Rectangle modelBounds;

    public KDTreeDrawableModel() {

    }

    private void initializeKDTree() {
        countFloats();
        drawableTypeToKDTreeRoot = new HashMap<>();
        for (DrawableType drawableType : DrawableType.values()) {
            List<Drawable> drawables = drawableTypeEnumMap.get(drawableType);
            if (drawables.size() > 0) {
                KDTree newTree = new KDTree(drawables);
                drawableTypeToKDTreeRoot.put(drawableType, newTree);
            }
        }
    }

    @Override
    public void add(DrawableType type, Drawable drawable) {
        if (!drawableTypeEnumMap.containsKey(type)) {
            drawableTypeEnumMap.put(type, new ArrayList<>());
        }
        drawableTypeEnumMap.get(type).add(drawable);
    }

    @Override
    public void doNewDataSet() {
        if (drawableTypeEnumMap == null) {
            drawableTypeEnumMap = EnumHelper.createDrawableTypeDrawablesMap();
        }
        initializeKDTree();
    }

    @Override
    public Iterable<Drawable> getDrawablesOfTypeInBounds(DrawableType type, Rectangle bounds) {
        if (drawableTypeToKDTreeRoot == null) {
            throw new RuntimeException("The KDTree has not been initialized yet");
        }
        if (drawableTypeToKDTreeRoot.containsKey(type))
            return drawableTypeToKDTreeRoot.get(type).rangeSearch(bounds, new ArrayList<Drawable>());
        else {
            return new ArrayList<>();
        }
    }

    @Override
    public Iterable<Drawable> getAllDrawablesOfType(DrawableType type) {
        if (drawableTypeToKDTreeRoot.containsKey(type)) {
            return drawableTypeToKDTreeRoot.get(type).getContent(new ArrayList<Drawable>());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void doneAdding() {
        initializeKDTree();
        drawableTypeEnumMap = null;
	}

    private void countFloats() {
        //System.out.println("Number of floats for each type");
        for (DrawableType drawableType : DrawableType.values()) {
            long numOfFloats = 0;
            for (Drawable drawable : drawableTypeEnumMap.get(drawableType)) {
                numOfFloats += drawable.getNumOfFloats();
            }
            //System.out.println( drawableTypeEnumMap.get(drawableType).size() + " of " + drawableType.name() + " having : " + numOfFloats + " floats");
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
        if (drawableTypeToKDTreeRoot.containsKey(type)) {
            SpatialIndexable drawable = drawableTypeToKDTreeRoot.get(type).getNearestNeighbor(x, y);
            if (drawable != null) {
                return (Drawable) drawable;
            }
        }
        return null;
    }

    @Override
    public void insert(DrawableType type, Drawable drawable) {
        if (!drawableTypeToKDTreeRoot.containsKey(type)) {
            drawableTypeToKDTreeRoot.put(type, new KDTree(new ArrayList<Drawable>()));
        }
        if (drawable instanceof SpatialIndexable) {
            drawableTypeToKDTreeRoot.get(type).insert((SpatialIndexable) drawable);
        } else {
            throw new IllegalArgumentException("tried inserting drawable which was not spatialindexable");
        }
    }

}
