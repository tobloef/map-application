package bfst19.osmdrawing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTree<T extends SpatialIndexable> implements Serializable {
	T axis;
	Rectangle bbox; //Bounding box could be saved in just float so save the memory overhead.
	List<T> elements;
	KDTree lower;
	KDTree higher;
	private final static int MAXNODESPERLEAF = 50; //TODO: Benchmark some different values
	private final static Random random = new Random();

	public KDTree(List<T> inputElements, Rectangle bbox){
		this(inputElements, true, bbox);
	}

	private KDTree(List<T> inputElements, boolean odd, Rectangle bbox){
		this.bbox = bbox;
		//TODO: Grow the bounding to box to contain all its children.
		if (inputElements.size() < MAXNODESPERLEAF){
			this.elements = inputElements;
		}
		else {
			axis = quickMedian(inputElements, odd);
			inputElements.remove(axis);
			//TODO: Move creation of children into function, it appears to have code duplication.
			Rectangle lowerBBox = new Rectangle(bbox);
			Rectangle higherBBox = new Rectangle(bbox);
			if (odd){
				lowerBBox.xMax = axis.getRepresentativeX();
				higherBBox.xMin = axis.getRepresentativeX();
			}
			else {
				lowerBBox.yMax = axis.getRepresentativeY();
				higherBBox.yMin = axis.getRepresentativeY();
			}
			List<T> lowerElements = new ArrayList<>();
			List<T> higherElements = new ArrayList<>();
			for (T element : inputElements){
				if (spatialLessThen(element, axis, odd)){
					lowerElements.add(element);
				}
				else {
					higherElements.add(element);
				}
			}
			lower = new KDTree<T>(lowerElements, !odd, lowerBBox);
			higher = new KDTree<T>(higherElements, !odd, higherBBox);
		}
	}

	private List<T> getContent(List<T> returnElements){
		if (axis == null){
			returnElements.addAll(elements);
		}
		else {
			higher.getContent(returnElements);
			lower.getContent(returnElements);
		}
		return returnElements;
	}

	public List<T> rangeQuery(Rectangle queryBox, List<T> returnElements){
		return rangeQuery(queryBox, true, returnElements);
	}

	private List<T> rangeQuery(Rectangle queryBox, boolean odd, List<T> returnElements){
		if (axis == null){
			returnElements.addAll(elements);
			return returnElements;
		}
		returnElements.add(axis); //TODO: Check if its faster to draw the element or check if it should be drawn.
		if(queryBox.intersect(lower.bbox)){
			lower.rangeQuery(queryBox, !odd, returnElements);
		}
		if(queryBox.intersect(higher.bbox)){
			higher.rangeQuery(queryBox, !odd, returnElements);
		}
		return returnElements;
	}

	private T quickMedian(List<T> list, boolean isCheckingForX){
		return quickSelect(list, 0, list.size()-1, list.size()/2, isCheckingForX);
	}

	//Quickselect and Partition are taken from wikipedia, They could possibly be more efficient, runs pretty fast right now through
	private T quickSelect(List<T> list, int left, int right, int k, boolean isCheckingForX){
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

	private int partition(List<T> list, int left, int right, int pivotIndex, boolean isCheckingForX){
		T pivotElement = list.get(pivotIndex);
		swap(list, pivotIndex, right);
		int storeIndex = left;
		for (int i = left; i < right; i++){
			if (spatialLessThen(list.get(i), pivotElement, isCheckingForX)){
				swap(list, storeIndex, i);
				storeIndex++;
			}
		}
		swap(list, right, storeIndex);
		return storeIndex;
	}

	private final <T> void swap (List<T> a, int i, int j) {
		T t = a.get(i);
		a.set(i, a.get(j));
		a.set(j, t);
	}

	private boolean spatialLessThen(T left, T right, boolean isCheckingForX){
		if (isCheckingForX){
			return left.getRepresentativeX() < right.getRepresentativeX();
		}
		else {
			return left.getRepresentativeY() < right.getRepresentativeY();
		}
	}

	private int smallerElements(List<T> list, T element, boolean isCheckingForX){
		int numberSmaller = 0;
		for (T comp : list){
			if (spatialLessThen(comp, element, isCheckingForX)){
				numberSmaller++;
			}
		}
		return numberSmaller;
	}

}