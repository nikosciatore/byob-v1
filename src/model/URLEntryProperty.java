package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
public class URLEntryProperty {
	private final SimpleStringProperty ID;
	private final SimpleStringProperty URL;
	private final SimpleStringProperty period;
	private final SimpleIntegerProperty maxContactNumber;
	private final SimpleStringProperty sleepMode;
	private final SimpleStringProperty userAgent;
	private final SimpleStringProperty proxy;
	
//	public URLEntryProperty() {
//		this.ID = new SimpleStringProperty("");
//		this.URL = new SimpleStringProperty("");
//		this.periodicRangeSec = new SimpleStringProperty("");
//		this.maxContactNumber = new SimpleIntegerProperty();
//		this.sleepMode = new SimpleStringProperty("");
//		this.userAgent = new SimpleStringProperty("");
//		this.proxy = new SimpleStringProperty("");
//	}
	
	public URLEntryProperty(URLEntry urlEntry) {
		this.ID = new SimpleStringProperty(urlEntry.getID().toString());
		this.URL = new SimpleStringProperty(urlEntry.getURL().toString());
		this.period = new SimpleStringProperty(urlEntry.getPeriodicRangeSec().toString());
		this.maxContactNumber = new SimpleIntegerProperty(urlEntry.getMaxContactNumber());
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
	public Integer getMaxContactNumber() {
		return maxContactNumber.get();
	}
	public void setMaxContactNumber(Integer maxContactNumber) {
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
