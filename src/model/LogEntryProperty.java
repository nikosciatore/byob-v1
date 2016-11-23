package model;

import javafx.beans.property.SimpleStringProperty;

public class LogEntryProperty extends URLEntryProperty{
	
	private final SimpleStringProperty timestamp;
	private final SimpleStringProperty contactNumber;;



	public LogEntryProperty(LogEntry logEntry, Integer contactNumber) {
		super(new URLEntry(logEntry.ID, 
						   logEntry.URL, 
						   logEntry.period, 
						   logEntry.maxContactNumber, 
						   logEntry.sleepMode, 
						   logEntry.userAgent, 
						   logEntry.proxy));
		
		this.contactNumber = new SimpleStringProperty(contactNumber.toString());
		this.timestamp = new SimpleStringProperty(logEntry.getTimestamp().toString());
	}
	
	public String getTimestamp() {
		return timestamp.get();
	}
	
	public void setTimestamp(String iD) {
		timestamp.set(iD);
	}

	public SimpleStringProperty getContactNumber() {
		return contactNumber;
	}

	@Override
	public String getMaxContactNumber() {
		return contactNumber.get() + "/" + super.getMaxContactNumber();
	}
}
