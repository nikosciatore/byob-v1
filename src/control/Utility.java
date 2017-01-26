package control;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import model.SortMode;

/**
 * Classe contenente metodi di utilità generale
 */
public class Utility {
	
	/**
	 * Genera l'ID del bot in base al parametro sortMode
	 * @param sortMode <br>
	 * none: ritorna l'indirizzo MAC<br>
	 * ascending: ritorna l'indirizzo MAC dopo aver ordinato i caratteri in ordine crescente<br>
	 * random: ritorna l'indirizzo MAC dopo aver mischiato i caratteri
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
	
	/**
	 * Restituisce l'indirizzo MAC della prima scheda di rete trovata
	 * @return indirizzo MAC
	 */
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
	 * Divide la stringa in base al separatore specificato
	 * 
	 * @param string stringa contenente gli elementi da separare
	 * @param separator carattere rispetto al quale effettuare la separazione
	 * @return lista degli elementi separati
	 */
	public static ArrayList<String> splitInArrayList(String string, String separator) {
		ArrayList<String> returnValue = new ArrayList<String>();
		String [] strings;
		strings = string.split(separator);
		for (int i = 0; i < strings.length; i++) {
			returnValue.add(strings[i]);
		}
		return returnValue;
	}

	/**
	 * Stampa una lista di stringhe con il seguente formato
	 * "name: elemento1, elemento2, ..., elementoN"
	 * @param list
	 * @param name
	 */
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
