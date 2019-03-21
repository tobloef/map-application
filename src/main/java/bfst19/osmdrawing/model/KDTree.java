package bfst19.osmdrawing.model;

import bfst19.osmdrawing.view.WayType;

import java.io.Serializable;
import java.util.*;

public class KDTree<T extends SpatialIndexable> implements Serializable {
	T element;
	//Bounding box could be saved in just float so save the memory overhead.
	private Rectangle bbox;
	List<T> leafElements;
	KDTree lower;
	KDTree higher;
	private final static int MAX_NODES_PER_LEAF = 5; //TODO: Benchmark some different values
	private final static Random random = new Random();

	public KDTree(List<T> inputElements, Rectangle bbox){
		this(inputElements, true, bbox);
	}

	private KDTree(List<T> inputElements, boolean odd, Rectangle bbox){
		this.bbox = new Rectangle(bbox);
		if (inputElements.size() < MAX_NODES_PER_LEAF){
			this.leafElements = inputElements;
			growToEncompassLeafElements(inputElements);
		}
		else {
			element = quickMedian(inputElements, odd);
			inputElements.remove(element);
			makeSubTrees(inputElements, odd, bbox);
		}
	}

	private void makeSubTrees(List<T> inputElements, boolean odd, Rectangle bbox) {
		makeLowerTree(odd, bbox, inputElements);
		makeHigherTree(odd, bbox, inputElements);

	}

	private void makeHigherTree(boolean odd, Rectangle bbox, List<T> inputElements) {
		Rectangle higherBBox = new Rectangle(bbox);
		if (odd){
			higherBBox.xMin = element.getRepresentativeX();
		}
		else {
			higherBBox.yMin = element.getRepresentativeY();
		}
		List<T> higherElements = new ArrayList<>();
		for (T element : inputElements){
			if (!spatialLessThen(element, this.element, odd)){
				higherElements.add(element);
			}
		}
		higher = new KDTree<T>(higherElements, !odd, higherBBox);
		this.bbox.growToEncompass(higher.bbox);
	}

	private void makeLowerTree(boolean odd, Rectangle bbox, List<T> inputElements) {
		Rectangle lowerBBox = new Rectangle(bbox);
		if (odd){
			lowerBBox.xMax = element.getRepresentativeX();
		}
		else {
			lowerBBox.yMax = element.getRepresentativeY();
		}
		List<T> lowerElements = new ArrayList<>();
		for (T element : inputElements){
			if (spatialLessThen(element, this.element, odd)){
				lowerElements.add(element);
			}
		}
		lower = new KDTree<T>(lowerElements, !odd, lowerBBox);
		this.bbox.growToEncompass(lower.bbox);
	}

	private void growToEncompassLeafElements(List<T> inputElements) {
		for (T inputElement: inputElements){
			this.bbox.growToEncompass(inputElement.getMinimumBoundingRectangle());
		}
	}

	public List<T> getContent(List<T> returnElements){
		if (element == null){
			returnElements.addAll(leafElements);
		}
		else {
			returnElements.add(element);
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
			for (T leafElement : leafElements){
				if (leafElement.getMinimumBoundingRectangle().intersect(queryBox)){
					returnElements.add(leafElement);
				}
			}
			return returnElements;
		}
		if (element.getMinimumBoundingRectangle().intersect(queryBox)){
			returnElements.add(element);
		}
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
		T partitionElement = list.get(partitionIndex);
		swap(list, partitionIndex, right);
		int storeIndex = left;
		for (int i = left; i < right; i++){
			if (spatialLessThen(list.get(i), partitionElement, isCheckingForX)){
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