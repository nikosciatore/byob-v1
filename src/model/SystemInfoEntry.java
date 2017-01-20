package model;

import java.io.Serializable;

/**
 * Classe che rappresenta una coppia chiave-valore 
 * relativamente alle informazioni di sistema
 */
public class SystemInfoEntry  implements Serializable{

	private static final long serialVersionUID = 2914873971280963555L;
	
	private String property;
	private String value;
	
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
