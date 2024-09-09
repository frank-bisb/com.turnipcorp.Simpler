package com.turnipcorp.Simpler.interpreter;

public class Arrays {
	
	public static <T extends Comparable<T>> int indexOf(T[] arr, T toFind) {
		if (!isSorted(arr)) return -1;
		return binarySearch(arr, 0, arr.length, toFind);
	}
	
	public static <T extends Comparable<T>> boolean isSorted(T[] arr) {
		for (int i = 1; i < arr.length; i++) {
			if (arr[i].compareTo(arr[i - 1]) < 0) return false;
		}
		return true;
	}
	
	public static <T extends Comparable<T>> int binarySearch(T[] arr, int begin, int end, T toFind) {
		int avg = (begin + end) / 2;
		if (arr[avg].compareTo(toFind) == 0) return avg;
		else if (arr[avg].compareTo(toFind) < 0) return binarySearch(arr, avg, end, toFind);
		else if (arr[avg].compareTo(toFind) > 0) return binarySearch(arr, begin, avg, toFind);
		else return -1;
	}
}
