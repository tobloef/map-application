package bfst19.osmdrawing.model;

import bfst19.osmdrawing.view.WayType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.*;

public class KDTreeModel implements DrawableModel {
	Map<WayType, List<Drawable>> wayTypeEnumMap = DrawableModel.initializeWaysEnumMap();
	Map<WayType, KDTree> wayTypeToKDTreeRoot;
	Rectangle modelBounds;
	static Random r = new Random();
	private final static int MAXNODESPERLEAF = 100; //Benchmark some different values


	private class KDTree implements Serializable {
		Drawable axis; //Muligvis bare gem kordinat.
		List<Drawable> elements;
		KDTree lower;
		KDTree higher;

		private KDTree(List<Drawable> drawables, boolean odd){
			if (drawables.size() < MAXNODESPERLEAF){
				this.elements = drawables;
			}
			else {
				axis = quickMedian(drawables, odd);
				drawables.remove(axis);
				List<Drawable> lowerDrawables = new ArrayList<>();
				List<Drawable> higherDrawables = new ArrayList<>();
				for (Drawable drawable : drawables){
					if (drawableLessThen(drawable, axis, odd)){
						lowerDrawables.add(drawable);
					}
					else {
						higherDrawables.add(drawable);
					}
				}
				lower = new KDTree(lowerDrawables, !odd);
				higher = new KDTree(higherDrawables, !odd);
			}
		}

		private List<Drawable> rangeQuery(Rectangle box, boolean odd){
			if(axis == null){
				return elements;
			}
			else {
				List<Drawable> drawables = new ArrayList<>();
				if (odd) {
					if (box.xmax > axis.getCenterX()) {
						drawables.addAll(higher.rangeQuery(box, !odd));
					}
					if (box.xmin < axis.getCenterX()) {
						drawables.addAll(lower.rangeQuery(box, !odd));
					}
				}
				else {
					if (box.ymax > axis.getCenterY()) {
						drawables.addAll(higher.rangeQuery(box, !odd));
					}
					if (box.ymin < axis.getCenterY()) {
						drawables.addAll(lower.rangeQuery(box, !odd));
					}
				}
				return drawables;
			}
		}


	}

	public KDTreeModel(){

	}

	@Override
	public void add(WayType type, Drawable drawable) {
		wayTypeEnumMap.get(type).add(drawable);
	}

	@Override
	public Iterable<Drawable> getDrawablesOfType(WayType type, Rectangle bounds) {
		if (wayTypeToKDTreeRoot.containsKey(type))
			return wayTypeToKDTreeRoot.get(type).rangeQuery(bounds, true);
		else {
			return new ArrayList<>();
		}
	}

	@Override
	public void doneAdding() {
		initializeKDTree();
		return;
	}

	private void initializeKDTree(){
		wayTypeToKDTreeRoot = new HashMap<>();
		for (WayType wayType : WayType.values()){
			List<Drawable> drawables = wayTypeEnumMap.get(wayType);
			if (drawables.size() > 0) {
				KDTree newTree = new KDTree(drawables, true); //Its true so that the first node splits in x;
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

	private static Drawable quickMedian(List<Drawable> list, boolean isCheckingForX){
		return quickSelect(list, 0, list.size()-1, list.size()/2, isCheckingForX);
	}

	//These are both taken from wikipedia, They could possibly be more efficient, runs pretty fast right now through
	private static Drawable quickSelect(List<Drawable> list, int left, int right, int k, boolean isCheckingForX){
		if (left == right){
			return list.get(left);
		}
		int pivotIndex = left + (int)Math.floor(r.nextDouble() % (right - left + 1));
		pivotIndex = partition(list, left, right, pivotIndex, isCheckingForX);
		if (k == pivotIndex){
			return list.get(k);
		}
		else if( k < pivotIndex){
			return quickSelect(list, left, pivotIndex - 1, k, isCheckingForX);
		}
		else {
			return quickSelect(list, pivotIndex + 1, right, k, isCheckingForX);
		}
	}

	private static int partition(List<Drawable> list, int left, int right, int pivotIndex, boolean isCheckingForX){
		Drawable pivotElement = list.get(pivotIndex);
		swap(list, pivotIndex, right);
		int storeIndex = left;
		for (int i = left; i < right; i++){
			if (drawableLessThen(list.get(i), pivotElement, isCheckingForX)){
				swap(list, storeIndex, i);
				storeIndex++;
			}
		}
		swap(list, right, storeIndex);
		return storeIndex;
	}

	public static final <T> void swap (List<T> a, int i, int j) {
		T t = a.get(i);
		a.set(i, a.get(j));
		a.set(j, t);
	}

	public static boolean drawableLessThen(Drawable left, Drawable right, boolean isCheckingForX){
		if (isCheckingForX){
			return left.getCenterX() < right.getCenterX();
		}
		else {
			return left.getCenterY() < right.getCenterY();
		}
	}

	private static int smallerElements(List<Drawable> list, Drawable element, boolean isCheckingForX){
		int numberSmaller = 0;
		for (Drawable comp : list){
			if (drawableLessThen(comp, element, isCheckingForX)){
				numberSmaller++;
			}
		}
		return numberSmaller;
	}

	private void testQuickSelect() {
		System.out.println(modelBounds);
		for (WayType wayType : WayType.values()){
			List<Drawable> list = wayTypeEnumMap.get(wayType);
			if (list.size() > 0) {
				Drawable middle = quickSelect(list, 0, list.size() - 1, list.size() / 2, true);
				System.out.println(wayType.name() + "'s middle x: " + middle.getCenterX() + " y: " + middle.getCenterY());
				System.out.println("Its smaller then " + smallerElements(list, middle, true) + " out of " + list.size());
			}
		}
	}

}
