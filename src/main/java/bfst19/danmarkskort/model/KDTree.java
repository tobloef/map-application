package bfst19.danmarkskort.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTree<T extends SpatialIndexable> implements Serializable {
	T splitElement;
	private float xMin, yMin, xMax, yMax;
	List<T> leafElements;
	KDTree lower;
	KDTree higher;
	private final static int MAX_NODES_PER_LEAF = 250; //TODO: Benchmark some different values
	private final static Random random = new Random();

	public KDTree(List<T> inputElements, Rectangle bbox){
		this(inputElements, true, new Rectangle(bbox));
	}

	private KDTree(List<T> inputElements, boolean odd, Rectangle bbox){
		this.setBbox(bbox);
		if (inputElements.size() < MAX_NODES_PER_LEAF){
			this.leafElements = inputElements;
			growToEncompassLeafElements(inputElements);
		}
		else {
			splitElement = quickMedian(inputElements, odd);
			inputElements.remove(splitElement);
			this.bboxGrowToEncompass(splitElement.getMinimumBoundingRectangle());
			makeSubTrees(inputElements, odd, this.getBbox());
		}
	}

	private void makeSubTrees(List<T> inputElements, boolean odd, Rectangle bbox) {
		makeLowerTree(odd, bbox, inputElements);
		makeHigherTree(odd, bbox, inputElements);

	}

	private void makeHigherTree(boolean odd, Rectangle bbox, List<T> inputElements) {
		Rectangle higherBBox = new Rectangle(bbox);
		if (odd){
			higherBBox.xMin = splitElement.getRepresentativeX();
		}
		else {
			higherBBox.yMin = splitElement.getRepresentativeY();
		}
		List<T> higherElements = new ArrayList<>();
		for (T element : inputElements){
			if (!spatialLessThen(element, this.splitElement, odd)){
				higherElements.add(element);
			}
		}
		higher = new KDTree<T>(higherElements, !odd, higherBBox);
		this.bboxGrowToEncompass(higher.getBbox());
	}

	private void bboxGrowToEncompass(Rectangle otherBbox) {
		Rectangle bbox = this.getBbox();
		bbox.growToEncompass(otherBbox);
		setBbox(bbox);
	}

	private void setBbox(Rectangle bbox) {
		this.xMin = bbox.xMin;
		this.yMin = bbox.yMin;
		this.xMax = bbox.xMax;
		this.yMax = bbox.yMax;
	}

	private Rectangle getBbox() {
		return new Rectangle(xMin, yMin, xMax, yMax);
	}

	private void makeLowerTree(boolean odd, Rectangle bbox, List<T> inputElements) {
		Rectangle lowerBBox = new Rectangle(bbox);
		if (odd){
			lowerBBox.xMax = splitElement.getRepresentativeX();
		}
		else {
			lowerBBox.yMax = splitElement.getRepresentativeY();
		}
		List<T> lowerElements = new ArrayList<>();
		for (T element : inputElements){
			if (spatialLessThen(element, this.splitElement, odd)){
				lowerElements.add(element);
			}
		}
		lower = new KDTree<T>(lowerElements, !odd, lowerBBox);
		this.bboxGrowToEncompass(lower.getBbox());
	}

	private void growToEncompassLeafElements(List<T> inputElements) {
		for (T inputElement: inputElements){
			this.bboxGrowToEncompass(inputElement.getMinimumBoundingRectangle());
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