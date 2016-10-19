package byobv1.model;

import java.util.Date;

public class LogEntry extends URLEntry {

	Date timestamp;
	
	public LogEntry(URLEntry urlEntry, Date timestamp) {
		super(urlEntry.URL, urlEntry.periodicRangeSec, urlEntry.maxContactNumber, 
			  urlEntry.sleepMode, urlEntry.userAgent, urlEntry.proxy);
		this.timestamp = timestamp;
	}
	
}
