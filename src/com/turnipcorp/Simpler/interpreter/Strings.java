package com.turnipcorp.Simpler.interpreter;

import java.util.ArrayList;

/**
 * A class of static methods for working with arrays, among many other things.
 */
public class Strings {
	
	public static String[] slice(String[] arr, int begin, int end) {
		ArrayList<String> result = new ArrayList<>();
		for(int i = begin; i < end; i++) {
			result.add(arr[i]);
		}
		return toStringArray(result.toArray());
	}
	
	public static char[] slice(char[] arr, int begin, int end) {
		ArrayList<Character> result = new ArrayList<>();
		char[] toReturn;
		for(int i = begin; i < end; i++) {
			result.add(arr[i]);
		}
		toReturn = new char[result.size()];
		for(int i = 0; i < result.size(); i++) toReturn[i] = (Character) result.get(i);
		return toReturn;
	}
	
	public static String[] pop(String[] arr) {
		String[] result = new String[arr.length - 1];
		for(int i = 0; i < result.length; i++) {
			result[i] = arr[i];
		}
		return result;
	}
	
	public static boolean isNumeric(String str) {
		try {
			int num = Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static String[] toStringArray(Object[] arr) {
		String[] result = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			result[i] = (String) arr[i];
		}
		return result;
	}
	
	public static void printStringArray(String[] arr) {
		for (String str : arr) {
			System.out.println(str);
		}
	}
	
	public static void printArray(Object[] arr) {
		for (Object obj : arr) {
			System.out.println(obj);
		}
	}
	
	public static boolean arrayContains(String[] arr, String toFind) {
		for (String str : arr) 
			if (str.equals(toFind)) return true;
		return false;
	}
	
	public static String strip(String str) {
		return str.substring(1, str.length() - 1);
	}
}
