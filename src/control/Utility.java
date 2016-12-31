package control;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

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
}
