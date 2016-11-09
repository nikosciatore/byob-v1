package model;

import java.util.Date;

public class LogEntry extends URLEntry {

	Date timestamp;
	
	public LogEntry(URLEntry urlEntry, Date timestamp) {
		super(urlEntry.ID, urlEntry.URL, urlEntry.period, urlEntry.maxContactNumber, 
			  urlEntry.sleepMode, urlEntry.userAgent, urlEntry.proxy);
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return timestamp.toString() + "\n" + super.toStringForLog();
	}
}
