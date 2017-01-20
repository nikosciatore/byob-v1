package model;

import java.util.ArrayList;

/**
 * Classe che rappresenta le informazioni relative al sistema
 * attraverso una lista di coppie chiave valore
 * es: OS Name: Linux
 * 	   OS Arch: x64
 */
public class SystemInfo {
	private ArrayList<SystemInfoEntry> systemInfoEntryList;

	public SystemInfo(ArrayList<SystemInfoEntry> systemInfoEntryList) {
		this.systemInfoEntryList = systemInfoEntryList;
	} 
	 
	public ArrayList<SystemInfoEntry> getSystemInfoEntryList() {
		return systemInfoEntryList;
	}

	public void setSystemInfoEntryList(ArrayList<SystemInfoEntry> systemInfoEntryList) {
		this.systemInfoEntryList = systemInfoEntryList;
	}
}
