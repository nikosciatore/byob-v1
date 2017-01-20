package model.gui;

import javafx.beans.property.SimpleStringProperty;
import model.URLEntry;

/**
 * Classe necessaria per riempire le righe della TabelView 
 * presente nell'interfaccia grafica
 */
public class URLEntryProperty {
	
	private final SimpleStringProperty ID;
	private final SimpleStringProperty URL;
	private final SimpleStringProperty period;
	private final SimpleStringProperty maxContactNumber;
	private final SimpleStringProperty sleepMode;
	private final SimpleStringProperty userAgent;
	private final SimpleStringProperty proxy;
	
	public URLEntryProperty(URLEntry urlEntry) {
		this.ID = new SimpleStringProperty(urlEntry.getID().toString());
		this.URL = new SimpleStringProperty(urlEntry.getURL().toString());
		this.period = new SimpleStringProperty(urlEntry.getPeriod().toString());
		this.maxContactNumber = new SimpleStringProperty(urlEntry.getMaxContactNumber().toString());
		this.sleepMode = new SimpleStringProperty(urlEntry.getSleepMode().toString());
		this.userAgent = new SimpleStringProperty(urlEntry.getUserAgent());
		this.proxy = new SimpleStringProperty(urlEntry.getProxy().toString());
	}

	public String getID() {
		return ID.get();
	}
	
	public void setID(String iD) {
		ID.set(iD);
	}
	
	public String getURL() {
		return URL.get();
	}
	
	public void setURL(String uRL) {
		URL.set(uRL);
	}
	
	public String getPeriod() {
		return period.get();
	}
	
	public void setPeriod(String periodicRangeSec) {
		this.period.set(periodicRangeSec);
	}
	
	public String getMaxContactNumber() {
		return maxContactNumber.get();
	}
	
	public void setMaxContactNumber(String maxContactNumber) {
		this.maxContactNumber.set(maxContactNumber);
	}
	
	public String getSleepMode() {
		return sleepMode.get();
	}
	
	public void setSleepMode(String sleepMode) {
		this.sleepMode.set(sleepMode);
	}
	
	public String getUserAgent() {
		return userAgent.get();
	}
	
	public void setUserAgent(String userAgent) {
		this.userAgent.set(userAgent);
	}
	
	public String getProxy() {
		return proxy.get();
	}
	
	public void setProxy(String proxy) {
		this.proxy.set(proxy);
	}
}
