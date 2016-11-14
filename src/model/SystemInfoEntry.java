package model;

public class SystemInfoEntry {
	String property;
	String value;
	
	public SystemInfoEntry(String property, String value) {
		this.property = property;
		this.value = value;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
