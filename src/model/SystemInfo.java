package model;

import java.util.ArrayList;

public class SystemInfo {
	 ArrayList<SystemInfoEntry> systemInfoEntryList;

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
