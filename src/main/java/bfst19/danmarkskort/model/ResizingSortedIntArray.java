package bfst19.danmarkskort.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

public class ResizingSortedIntArray implements Iterable<Integer>, Serializable {
	private int[] array;
	boolean sorted = false;
	private int n;

	public ResizingSortedIntArray(int n){
		array = new int[n];
	}

	public ResizingSortedIntArray(){
		this(1);
	}

	public void add(int value) {
		if (array.length == n){
			resize(n*2);
		}
		array[n++] = value;
	}

	private void resize(int i) {
		int[] tempArray = new int[i];
		for (int j = 0; j < n; j++){
			tempArray[j] = array[j];
		}
		array = tempArray;
	}

	public boolean contains(int value) {
		if (!sorted){
			Arrays.sort(array);
		}
		return Arrays.binarySearch(array, value) != -1;
	}

	@Override
	public Iterator<Integer> iterator() {
		return Arrays.stream(array).iterator();
	}
}
