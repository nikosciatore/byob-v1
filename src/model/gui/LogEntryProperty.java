package model.gui;

import javafx.beans.property.SimpleStringProperty;
import model.URLEntry;
import model.client.LogEntry;

/**
 * Classe necessaria per riempire le righe della TabelView 
 * presente nell'interfaccia grafica
 */
public class LogEntryProperty extends URLEntryProperty{
	
	private final SimpleStringProperty timestamp;
	private final SimpleStringProperty contactNumber;;

	public LogEntryProperty(LogEntry logEntry, Integer contactNumber) {
		super(new URLEntry(logEntry.getID(), 
						   logEntry.getURL(), 
						   logEntry.getPeriod(), 
						   logEntry.getMaxContactNumber(), 
						   logEntry.getSleepMode(), 
						   logEntry.getUserAgent(), 
						   logEntry.getProxy()));
		
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
