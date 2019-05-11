package bfst19.danmarkskort.model.drawableModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTree<T extends SpatialIndexable> implements Serializable {
    //The value 250 was chosen as a good middle ground between memory and performance.
    //A larger value will decrease memory footprint a bit and decrease performance as well.
    private final static int MAX_NODES_PER_LEAF = 250;
    //The value before rebalance is 150% of MAX_NODES_PER_LEAF, as until that point has a small impact on performance
    //And if its less, then it rebalances too often.
    private final static int MAX_NODES_BEFORE_REBALANCE = (MAX_NODES_PER_LEAF * 3) / 2;
    private final static Random random = new Random();
    T splitElement;
    Rectangle bbox;
    List<T> leafElements;
    KDTree<T> lower;
    KDTree<T> higher;

    public KDTree(List<T> inputElements) {
        this(inputElements, true);
    }

    private KDTree(List<T> inputElements, boolean odd) {
        generateKDTree(inputElements, odd);
    }

    private void generateKDTree(List<T> inputElements, boolean odd) {
        initbbox();
        if (inputElements.size() < MAX_NODES_PER_LEAF) { // Kontrol punkt 1
            this.leafElements = inputElements;
            growToEncompassLeafElements(inputElements);
        } else { // Kontrol punkt 2
            splitElement = quickMedian(inputElements, odd);
            inputElements.remove(splitElement);
            this.bboxGrowToEncompass(splitElement.getMinimumBoundingRectangle());
            makeSubTrees(inputElements, odd);
        }
    }

    private void initbbox() {
        Rectangle bbox = new Rectangle();
        bbox.xMin = Float.POSITIVE_INFINITY;
        bbox.yMin = Float.POSITIVE_INFINITY;
        bbox.xMax = Float.NEGATIVE_INFINITY;
        bbox.yMax = Float.NEGATIVE_INFINITY;
        this.setBbox(bbox);
    }

    private void makeSubTrees(List<T> inputElements, boolean odd) {
        makeLowerTree(odd, inputElements);
        makeHigherTree(odd, inputElements);

    }

    private void makeHigherTree(boolean odd, List<T> inputElements) {
        List<T> higherElements = new ArrayList<>();
        for (T element : inputElements) {
            if (!spatialLessThen(element, this.splitElement, odd)) {
                higherElements.add(element);
            }
        }
        higher = new KDTree<>(higherElements, !odd);
        this.bboxGrowToEncompass(higher.getBbox());
    }

    private void makeLowerTree(boolean odd, List<T> inputElements) {
        List<T> lowerElements = new ArrayList<>();
        for (T element : inputElements) {
            if (spatialLessThen(element, this.splitElement, odd)) { // Kontrol punkt 6 fejler her
                lowerElements.add(element);
            }
        }
        lower = new KDTree<>(lowerElements, !odd);
        this.bboxGrowToEncompass(lower.getBbox());
    }

    private void bboxGrowToEncompass(Rectangle otherBbox) {
        Rectangle bbox = this.getBbox();
        bbox.growToEncompass(otherBbox);
        setBbox(bbox);
    }

    private Rectangle getBbox() {
        return bbox;
    }

    private void setBbox(Rectangle bbox) {
        this.bbox = bbox;
    }

    public T getNearestNeighbor(float x, float y) {
        if (splitElement == null)
            return getClosestElement(x, y);
        NearestNeighbor tempNearestNeighbor = new NearestNeighbor(splitElement, splitElement.euclideanDistanceSquaredTo(x, y));
        return getNearestNeighbor(x, y, tempNearestNeighbor).element;
    }

    private NearestNeighbor getNearestNeighbor(float x, float y, NearestNeighbor nearestNeighbor) {
        if (splitElement == null) {
            T closestElement = getClosestElement(x, y);
            if (closestElement.euclideanDistanceSquaredTo(x,y) < nearestNeighbor.distance) {
                nearestNeighbor.element = closestElement;
                nearestNeighbor.distance = closestElement.euclideanDistanceSquaredTo(x, y);
            }
            return nearestNeighbor;
        }
        checkIfSplitElementIsNearestNeighbor(x, y, nearestNeighbor);
        getNearestNeighborInSubTree(x, y, nearestNeighbor, closestSubTreeToPoint(x, y));
        getNearestNeighborInSubTree(x, y, nearestNeighbor, furthestSubTreeFromPoint(x, y));
        return nearestNeighbor;
    }

    private void checkIfSplitElementIsNearestNeighbor(float x, float y, NearestNeighbor nearestNeighbor) {
        float splitElementDistance = splitElement.euclideanDistanceSquaredTo(x, y);
        if (splitElementDistance < nearestNeighbor.distance) {
            nearestNeighbor.distance = splitElementDistance;
            nearestNeighbor.element = splitElement;
        }
    }

    private KDTree<T> closestSubTreeToPoint(float x, float y) {
        if (higher.bbox.euclideanDistanceSquaredTo(x, y) < lower.bbox.euclideanDistanceSquaredTo(x, y)) {
            return higher;
        } else return lower;
    }

    private KDTree<T> furthestSubTreeFromPoint(float x, float y) {
        if (higher.bbox.euclideanDistanceSquaredTo(x, y) > lower.bbox.euclideanDistanceSquaredTo(x, y)) {
            return higher;
        } else return lower;
    }

    private void getNearestNeighborInSubTree(float x, float y, NearestNeighbor nearestNeighbor, KDTree<T> subTree) {
        if (subTree.bbox.euclideanDistanceSquaredTo(x, y) < nearestNeighbor.distance) {
            subTree.getNearestNeighbor(x, y, nearestNeighbor);
        }
    }

    private T getClosestElement(float x, float y) {
        T closestElement = null;
        float distance = Float.POSITIVE_INFINITY;
        for (T element : leafElements) {
            float tempDistance = element.euclideanDistanceSquaredTo(x, y);
            if (tempDistance < distance) {
                distance = tempDistance;
                closestElement = element;
            }
        }
        return closestElement;
    }

    private void growToEncompassLeafElements(List<T> inputElements) {
        for (T inputElement : inputElements) {
            this.bboxGrowToEncompass(inputElement.getMinimumBoundingRectangle());
        }
    }

    public void insert(T insertionElement) {
        insert(insertionElement, true);
        if (treeIsUnbalanced()) { // Kontrol punkt 2
            rebalanceKDTree();
        }
    }

    private void rebalanceKDTree() {
        List<T> oldElements = new ArrayList<>();
        generateKDTree(this.getContent(oldElements), true);
    }

    private boolean treeIsUnbalanced() {
        if (splitElement != null) { // Kontrol punkt 2
            return lower.treeIsUnbalanced() || higher.treeIsUnbalanced();
        }
        if (leafElements == null) { // Kontrol punkt 2
            return false;
        }
        return leafElements.size() > MAX_NODES_BEFORE_REBALANCE; // Kontrol punkt 1
    }

    private void insert(T insertionElement, boolean odd) {
        if (splitElement == null) { // Kontrol Punkt 1
            leafElements.add(insertionElement);
            bboxGrowToEncompass(insertionElement.getMinimumBoundingRectangle()); // Kontrol punkt 5
        } else { // Kontrol punkt 2
            if (spatialLessThen(insertionElement, splitElement, odd)) {  // Kontrol punkt 3
                lower.insert(insertionElement, !odd);
                bboxGrowToEncompass(lower.getBbox()); // Kontrol punkt 5
            } else { // Kontrol punkt 4
                higher.insert(insertionElement, !odd);
                bboxGrowToEncompass(higher.getBbox()); // Kontrol punkt 5
            }
        }

    }

    public List<T> getContent(List<T> returnElements) {
        if (splitElement == null) {
            returnElements.addAll(leafElements);
        } else {
            returnElements.add(splitElement);
            higher.getContent(returnElements);
            lower.getContent(returnElements);
        }
        return returnElements;
    }

    public List<T> rangeSearch(Rectangle queryBox, List<T> returnElements) {
        return rangeSearch(queryBox, true, returnElements);
    }

    private List<T> rangeSearch(Rectangle queryBox, boolean odd, List<T> returnElements) {
        if (splitElement == null) {
            for (T leafElement : leafElements) {
                if (leafElement.getMinimumBoundingRectangle().intersect(queryBox)) {
                    returnElements.add(leafElement);
                }
            }
            return returnElements;
        }
        if (splitElement.getMinimumBoundingRectangle().intersect(queryBox)) {
            returnElements.add(splitElement);
        }
        if (queryBox.intersect(lower.getBbox())) {
            lower.rangeSearch(queryBox, !odd, returnElements);
        }
        if (queryBox.intersect(higher.getBbox())) {
            higher.rangeSearch(queryBox, !odd, returnElements);
        }
        return returnElements;
    }

    private T quickMedian(List<T> list, boolean isCheckingForX) {
        return quickSelect(list, 0, list.size() - 1, list.size() / 2, isCheckingForX);
    }

    //Quickselect and Partition are taken from wikipedia, They could possibly be more efficient, runs pretty fast right now through
    private T quickSelect(List<T> list, int left, int right, int k, boolean isCheckingForX) {
        if (left == right) {
            return list.get(left);
        }
        int partitionIndex = left + (int) Math.floor(random.nextDouble() % (right - left + 1));
        partitionIndex = partition(list, left, right, partitionIndex, isCheckingForX);
        if (k == partitionIndex) {
            return list.get(k);
        } else if (k < partitionIndex) {
            return quickSelect(list, left, partitionIndex - 1, k, isCheckingForX);
        } else {
            return quickSelect(list, partitionIndex + 1, right, k, isCheckingForX);
        }
    }

    private int partition(List<T> list, int left, int right, int partitionIndex, boolean isCheckingForX) {
        T partitionElement = list.get(partitionIndex);
        swap(list, partitionIndex, right);
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (spatialLessThen(list.get(i), partitionElement, isCheckingForX)) {
                swap(list, storeIndex, i);
                storeIndex++;
            }
        }
        swap(list, right, storeIndex);
        return storeIndex;
    }

    private <T> void swap(List<T> a, int i, int j) {
        T t = a.get(i);
        a.set(i, a.get(j));
        a.set(j, t);
    }

    private boolean spatialLessThen(T left, T right, boolean isCheckingForX) {
        if (isCheckingForX) {
            return left.getRepresentativeX() < right.getRepresentativeX();
        } else {
            return left.getRepresentativeY() < right.getRepresentativeY();
        }
    }

    private class NearestNeighbor {
        private T element;
        private float distance;

        NearestNeighbor(T element, float distance) {
            this.element = element;
            this.distance = distance;
        }
    }

}