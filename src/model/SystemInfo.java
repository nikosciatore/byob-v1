package model;

import java.nio.file.Path;
import java.util.ArrayList;

import control.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SystemInfo {
	
	ObservableList<SystemInfoEntryProperty> systemInfoEntryObservableList;

	public SystemInfo() {
		systemInfoEntryObservableList = FXCollections.observableArrayList();
	}

////	public Log() {
//		
//	}

//	public static ObservableList<LogEntryProperty> getLogEntryObservableList() {
////		return logEntryObservableList;
//	}
}
