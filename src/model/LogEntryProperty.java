package model;

import javafx.beans.property.SimpleStringProperty;

public class LogEntryProperty extends URLEntryProperty{
	
	private final SimpleStringProperty timestamp;

	public LogEntryProperty(LogEntry logEntry) {
		super(new URLEntry(logEntry.ID, 
						   logEntry.URL, 
						   logEntry.period, 
						   logEntry.maxContactNumber, 
						   logEntry.sleepMode, 
						   logEntry.userAgent, 
						   logEntry.proxy));
		
		this.timestamp = new SimpleStringProperty(logEntry.getTimestamp().toString());
	}
	
	public String getTimestamp() {
		return timestamp.get();
	}
	
	public void setTimestamp(String iD) {
		timestamp.set(iD);
	}


}
