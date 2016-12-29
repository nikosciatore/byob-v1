package control;

import java.util.ArrayList;

public class Utility {

	public static ArrayList<String> splitInArrayList(String string) {
		ArrayList<String> returnValue = new ArrayList<String>();
		String [] strings;
		strings = string.split(" ");
		for (int i = 0; i < strings.length; i++) {
			returnValue.add(strings[i]);
		}
		return returnValue;
	}

	public static ArrayList<String> splitInArrayList(String string, String separator) {
		ArrayList<String> returnValue = new ArrayList<String>();
		String [] strings;
		strings = string.split(separator);
		for (int i = 0; i < strings.length; i++) {
			returnValue.add(strings[i]);
		}
		return returnValue;
	}

	
	public static void printArrayListOfString(ArrayList<String> list, String name){
		int i;
		System.out.print(name + ": ");
		for (i = 0; i < list.size() - 1; i++) {
			System.out.print(list.get(i)+",");
		}
		System.out.print(list.get(i)+"\n");
	}

	public static Integer parseInt(String value) {
		Integer returnValue;
		try {
			returnValue = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			returnValue = -1;
		}
		return returnValue;
	}
}
