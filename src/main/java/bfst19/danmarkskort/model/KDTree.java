package bfst19.danmarkskort.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTree<T extends SpatialIndexable> implements Serializable {
	T splitElement;
	Rectangle bbox;
	List<T> leafElements;
	KDTree lower;
	KDTree higher;
	private final static int MAX_NODES_PER_LEAF = 250;
	private final static int MAX_NODES_BEFORE_REBALANCE = (MAX_NODES_PER_LEAF * 3) / 2;
	private final static Random random = new Random();

	public KDTree(List<T> inputElements){
		this(inputElements, true);
	}

	private KDTree(List<T> inputElements, boolean odd){
		generateKDTree(inputElements, odd);
	}

	private void generateKDTree(List<T> inputElements, boolean odd) {
		this.setBbox(new Rectangle());
		if (inputElements.size() < MAX_NODES_PER_LEAF){
			this.leafElements = inputElements;
			growToEncompassLeafElements(inputElements);
		}
		else {
			splitElement = quickMedian(inputElements, odd);
			inputElements.remove(splitElement);
			this.bboxGrowToEncompass(splitElement.getMinimumBoundingRectangle());
			makeSubTrees(inputElements, odd);
		}
	}

	private void makeSubTrees(List<T> inputElements, boolean odd) {
		makeLowerTree(odd, inputElements);
		makeHigherTree(odd, inputElements);

	}

	private void makeHigherTree(boolean odd, List<T> inputElements) {
		List<T> higherElements = new ArrayList<>();
		for (T element : inputElements){
			if (!spatialLessThen(element, this.splitElement, odd)){
				higherElements.add(element);
			}
		}
		higher = new KDTree<T>(higherElements, !odd);
		this.bboxGrowToEncompass(higher.getBbox());
	}

	private void makeLowerTree(boolean odd, List<T> inputElements) {
		List<T> lowerElements = new ArrayList<>();
		for (T element : inputElements){
			if (spatialLessThen(element, this.splitElement, odd)){
				lowerElements.add(element);
			}
		}
		lower = new KDTree<T>(lowerElements, !odd);
		this.bboxGrowToEncompass(lower.getBbox());
	}

	private void bboxGrowToEncompass(Rectangle otherBbox) {
		Rectangle bbox = this.getBbox();
		bbox.growToEncompass(otherBbox);
		setBbox(bbox);
	}

	private void setBbox(Rectangle bbox) {
		this.bbox = bbox;
	}

	private Rectangle getBbox() {
		return bbox;
	}



	public T getNearestNeighbor(float x, float y){
		return getNearestNeighbor(x, y, Float.POSITIVE_INFINITY);
	}

	private T getNearestNeighbor(float x, float y, float distance){
		if (splitElement == null){
			return getClosestElement(x, y, distance);
		}
		T closestElement = null;
		if (lower.bbox.euclideanDistanceSquaredTo(x,y) < distance){
			T lowerClosestElement = (T) lower.getNearestNeighbor(x, y, distance);
			if (lowerClosestElement != null && lowerClosestElement.euclideanDistanceSquaredTo(x, y) < distance) {
				closestElement = lowerClosestElement;
				distance = closestElement.euclideanDistanceSquaredTo(x, y);
			}
		}
		float splitElementDistance = splitElement.euclideanDistanceSquaredTo(x ,y);
		if (splitElementDistance < distance){
			distance = splitElementDistance;
			closestElement = splitElement;
		}
		if (higher.bbox.euclideanDistanceSquaredTo(x,y) < distance){
			T higherClosestElement = (T) higher.getNearestNeighbor(x, y, distance);
			if (higherClosestElement != null && higherClosestElement.euclideanDistanceSquaredTo(x, y) < distance) {
				closestElement = higherClosestElement;
				distance = closestElement.euclideanDistanceSquaredTo(x, y);
			}
		}
		return closestElement;
	}

	private T getClosestElement(float x, float y, float distance) {
		T closestElement = null;
		for (T element : leafElements){
			float tempDistance = element.euclideanDistanceSquaredTo(x, y);
			if (tempDistance < distance){
				distance = tempDistance;
				closestElement = element;
			}
		}
		return closestElement;
	}

	private void growToEncompassLeafElements(List<T> inputElements) {
		for (T inputElement: inputElements){
			this.bboxGrowToEncompass(inputElement.getMinimumBoundingRectangle());
		}
	}

	public void insert(T insertionElement) {
		insert(insertionElement, true);
		if(treeIsUnbalanced()){
			rebalanceKDTree();
		}
	}

	private void rebalanceKDTree() {
		List<T> oldElements = new ArrayList<>();
		generateKDTree(this.getContent(oldElements), true);
	}

	private boolean treeIsUnbalanced() {
		if (splitElement != null){
			return lower.treeIsUnbalanced() || higher.treeIsUnbalanced();
		}
		if (leafElements != null){
			return leafElements.size() > MAX_NODES_BEFORE_REBALANCE;
		}
		return false;
	}

	private void insert(T insertionElement, boolean odd){
		if (splitElement == null){
			leafElements.add(insertionElement);
		}
		else {
			if (spatialLessThen(insertionElement, splitElement, odd)){
				lower.insert(insertionElement, !odd);
			}
			else {
				higher.insert(insertionElement, !odd);
			}
		}
	}

	public List<T> getContent(List<T> returnElements){
		if (splitElement == null){
			returnElements.addAll(leafElements);
		}
		else {
			returnElements.add(splitElement);
			higher.getContent(returnElements);
			lower.getContent(returnElements);
		}
		return returnElements;
	}

	public List<T> rangeQuery(Rectangle queryBox, List<T> returnElements){
		return rangeQuery(queryBox, true, returnElements);
	}

	private List<T> rangeQuery(Rectangle queryBox, boolean odd, List<T> returnElements){
		if (splitElement == null){
			for (T leafElement : leafElements){
				if (leafElement.getMinimumBoundingRectangle().intersect(queryBox)){
					returnElements.add(leafElement);
				}
			}
			return returnElements;
		}
		if (splitElement.getMinimumBoundingRectangle().intersect(queryBox)){
			returnElements.add(splitElement);
		}
		if(queryBox.intersect(lower.getBbox())){
			lower.rangeQuery(queryBox, !odd, returnElements);
		}
		if(queryBox.intersect(higher.getBbox())){
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