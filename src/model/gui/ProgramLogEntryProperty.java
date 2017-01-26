package model.gui;

import javafx.beans.property.SimpleStringProperty;
import model.server.ProgramLogEntry;

/**
 * Classe necessaria per riempire le righe della TabelView 
 * presente nell'interfaccia grafica
 */
public class ProgramLogEntryProperty {
	
	private final SimpleStringProperty type;
	private final SimpleStringProperty timestamp;
	private final SimpleStringProperty message;
	
	public ProgramLogEntryProperty(ProgramLogEntry programLogEntry) {
		this.type = new SimpleStringProperty(programLogEntry.getType());
		this.timestamp = new SimpleStringProperty(programLogEntry.getTimestamp());
		this.message = new SimpleStringProperty(programLogEntry.getMessage());
	}
	
	public String getType() {
		return type.get();
	}
	
	public void setType(String type) {
		this.type.set(type);
	}

	public String getTimestamp() {
		return timestamp.get();
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp.set(timestamp);
	}

	public String getMessage() {
		return message.get();
	}
	
	public void setMessage(String message) {
		this.message.set(message);
	}
}
