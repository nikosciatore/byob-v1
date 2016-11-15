package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SystemInfo {
	
	static ObservableList<SystemInfoEntryProperty> systemInfoEntryObservableList;

	public SystemInfo() {
		systemInfoEntryObservableList = FXCollections.observableArrayList();
	}

	public static ObservableList<SystemInfoEntryProperty> getSystemInfoEntryObservableList() {
		return systemInfoEntryObservableList;
	}

	public void gatherInfo() {
		systemInfoEntryObservableList.add(new SystemInfoEntryProperty(new SystemInfoEntry("os.name", System.getProperty("os.name"))));
	}
}
