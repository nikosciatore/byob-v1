package model;

import javafx.beans.property.SimpleStringProperty;

public class SystemInfoEntryProperty {

	private final SimpleStringProperty property;
	private final SimpleStringProperty value;
	

	public SystemInfoEntryProperty(SystemInfoEntry systemInfoEntry) {
		this.property = new SimpleStringProperty(systemInfoEntry.getProperty());
		this.value = new SimpleStringProperty(systemInfoEntry.getValue());
	}
	
	public String getProperty() {
		return property.get();
	}
	
	public void setProperty(String iD) {
		property.set(iD);
	}

	public String getValue() {
		return value.get();
	}
	
	public void setValue(String iD) {
		value.set(iD);
	}
}
