package bfst19.osmdrawing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTree implements Serializable {
	Drawable axis;
	Rectangle bbox; //Bounding box could be saved in just float so save the memory overhead.
	List<Drawable> elements;
	KDTree lower;
	KDTree higher;
	private final static int MAXNODESPERLEAF = 50; //TODO: Benchmark some different values
	private final static Random random = new Random();

	public KDTree(List<Drawable> drawables, boolean odd, Rectangle bbox){
		this.bbox = bbox;
		//TODO: Grow the bounding to box to contain all its children.
		if (drawables.size() < MAXNODESPERLEAF){
			this.elements = drawables;
		}
		else {
			axis = quickMedian(drawables, odd);
			drawables.remove(axis);
			//TODO: Move creation of children into function, it appears to have code duplication.
			Rectangle lowerBBox = new Rectangle(bbox);
			Rectangle higherBBox = new Rectangle(bbox);
			if (odd){
				lowerBBox.xMax = axis.getCenterX();
				higherBBox.xMin = axis.getCenterX();
			}
			else {
				lowerBBox.yMax = axis.getCenterY();
				higherBBox.yMin = axis.getCenterY();
			}
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
			lower = new KDTree(lowerDrawables, !odd, lowerBBox);
			higher = new KDTree(higherDrawables, !odd, higherBBox);
		}
	}

	private List<Drawable> getContent(List<Drawable> drawables){
		if (axis == null){
			drawables.addAll(elements);
		}
		else {
			higher.getContent(drawables);
			lower.getContent(drawables);
		}
		return drawables;
	}

	public List<Drawable> rangeQuery(Rectangle queryBox, boolean odd, List<Drawable> drawables){
		if (axis == null){
			drawables.addAll(elements);
			return drawables;
		}
		if(queryBox.intersect(lower.bbox)){
			lower.rangeQuery(queryBox, !odd, drawables);
		}
		if(queryBox.intersect(higher.bbox)){
			higher.rangeQuery(queryBox, !odd, drawables);
		}
		return drawables;
	}

	private static Drawable quickMedian(List<Drawable> list, boolean isCheckingForX){
		return quickSelect(list, 0, list.size()-1, list.size()/2, isCheckingForX);
	}

	//Quickselect and Partition are taken from wikipedia, They could possibly be more efficient, runs pretty fast right now through
	private static Drawable quickSelect(List<Drawable> list, int left, int right, int k, boolean isCheckingForX){
		if (left == right){
			return list.get(left);
		}
		int pivotIndex = left + (int)Math.floor(random.nextDouble() % (right - left + 1));
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

	private static final <T> void swap (List<T> a, int i, int j) {
		T t = a.get(i);
		a.set(i, a.get(j));
		a.set(j, t);
	}

	private static boolean drawableLessThen(Drawable left, Drawable right, boolean isCheckingForX){
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

}