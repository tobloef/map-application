package bfst19.osmdrawing.model;

import bfst19.osmdrawing.view.WayType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class KDTree<T extends SpatialIndexable> implements Serializable {
	T element;
	Rectangle bbox; //Bounding box could be saved in just float so save the memory overhead.
	List<T> leafElements;
	KDTree lower;
	KDTree higher;
	private static Map<WayType, List<Rectangle>> minimumBoundingRectangles;
	private final static int MAX_NODES_PER_LEAF = 50; //TODO: Benchmark some different values
	private final static Random random = new Random();

	public KDTree(List<T> inputElements, Rectangle bbox){
		this(inputElements, true, bbox);
	}

	private KDTree(List<T> inputElements, boolean odd, Rectangle bbox){
		this.bbox = bbox;
		//TODO: Grow the bounding to box to contain all its children.
		if (inputElements.size() < MAX_NODES_PER_LEAF){
			this.leafElements = inputElements;
		}
		else {
			element = quickMedian(inputElements, odd);
			inputElements.remove(element);
			//TODO: Move creation of children into function, it appears to have code duplication.
			Rectangle lowerBBox = new Rectangle(bbox);
			Rectangle higherBBox = new Rectangle(bbox);
			if (odd){
				lowerBBox.xMax = element.getRepresentativeX();
				higherBBox.xMin = element.getRepresentativeX();
			}
			else {
				lowerBBox.yMax = element.getRepresentativeY();
				higherBBox.yMin = element.getRepresentativeY();
			}
			List<T> lowerElements = new ArrayList<>();
			List<T> higherElements = new ArrayList<>();
			for (T element : inputElements){
				if (spatialLessThen(element, this.element, odd)){
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
		if (element == null){
			returnElements.addAll(leafElements);
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
		if (element == null){
			returnElements.addAll(leafElements);
			return returnElements;
		}
		returnElements.add(element); //TODO: Check if its faster to draw the element or check if it should be drawn.
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
		int partitionIndex = left + (int)Math.floor(random.nextDouble() % (right - left + 1));
		partitionIndex = partition(list, left, right, partitionIndex, isCheckingForX);
		if (k == partitionIndex){
			return list.get(k);
		}
		else if( k < partitionIndex){
			return quickSelect(list, left, partitionIndex - 1, k, isCheckingForX);
		}
		else {
			return quickSelect(list, partitionIndex + 1, right, k, isCheckingForX);
		}
	}

	private int partition(List<T> list, int left, int right, int partitionIndex, boolean isCheckingForX){
		T pivotElement = list.get(partitionIndex);
		swap(list, partitionIndex, right);
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

}