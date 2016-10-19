package byobv1.model;

import java.net.URL;

public class URLEntry {
	protected URL URL;
	protected Range periodicRangeSec;
	protected Integer maxContactNumber;
	protected SleepMode sleepMode;
	protected String userAgent;
	protected String proxy;

	public URLEntry(URL URL, Range periodicRangeSec, Integer maxContactNumber, 
					SleepMode sleepMode, String userAgent, String proxy) {
		this.URL = URL;
		this.periodicRangeSec = periodicRangeSec;
		this.maxContactNumber = maxContactNumber;
		this.sleepMode = sleepMode;
		this.userAgent = userAgent;
		this.proxy = proxy;
	}
	
	public URLEntry() {
		URL = null;
		periodicRangeSec = null;
		maxContactNumber = null;
		sleepMode = null;
		userAgent = null;
		proxy = null;		
	}
	
	public URL getURL() {
		return URL;
	}

	public void setURL(URL uRL) {
		URL = uRL;
	}

	public Range getPeriodicRangeSec() {
		return periodicRangeSec;
	}

	public void setPeriodicRangeSec(Range periodicRangeSec) {
		this.periodicRangeSec = periodicRangeSec;
	}

	public Integer getMaxContactNumber() {
		return maxContactNumber;
	}

	public void setMaxContactNumber(Integer maxContactNumber) {
		this.maxContactNumber = maxContactNumber;
	}

	public SleepMode getSleepMode() {
		return sleepMode;
	}

	public void setSleepMode(SleepMode sleepMode) {
		this.sleepMode = sleepMode;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

}

