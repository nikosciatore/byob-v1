package control;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import model.SortMode;

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
	
	
	public static String getMacAddress() {
		String returnValue = null;
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

			for (NetworkInterface networkInterface : Collections.list(networkInterfaces)){
		
				byte[] mac = networkInterface.getHardwareAddress();

				if(mac==null){
					continue;
				}

				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < mac.length; j++) {
					sb.append(String.format("%02X%s", mac[j], (j < mac.length - 1) ? "-" : ""));
				}
				returnValue = sb.toString();
				
				break;
			}
				
		} catch (SocketException e) {
			e.printStackTrace();
		}
				return returnValue;
	}
	
	/**
	 * Generate bot id
	 * @param mode <br>
	 * none: return mac address <br>
	 * sort: return sorted mac address <br>
	 * shuffle: return shuffled mac address
	 * */
	public static String generateID(SortMode sortMode) {
		
		String macAddString = Utility.getMacAddress();
        macAddString = macAddString.replace("-", "");
        char[] macAddChars = macAddString.toCharArray();
        
        if(sortMode.equals(SortMode.ASCENDING)){
            Arrays.sort(macAddChars);
            String macAddSorted = new String(macAddChars);		
            return  macAddSorted;
        }else if(sortMode.equals(SortMode.RANDOM)){
        	java.util.List<String> letters = Arrays.asList(macAddString.split(""));
			Collections.shuffle(letters);
			String macAddShuffled = "";
			for (String letter : letters) {
				macAddShuffled += letter;
			}
            System.out.println(macAddShuffled);
            return  macAddShuffled;   
        }else if(sortMode.equals(SortMode.NONE)){
        	return macAddString;
        }else{
        	return null;
        }
	}
}
