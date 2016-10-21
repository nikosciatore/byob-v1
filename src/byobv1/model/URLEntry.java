package byobv1.model;

import java.net.URL;

public class URLEntry {
	protected Integer ID;
	protected URL URL;
	protected Range periodicRangeSec;
	protected Integer maxContactNumber;
	protected SleepMode sleepMode;
	protected String userAgent;
	protected URL proxy;

	public URLEntry(URL URL, Range periodicRangeSec, Integer maxContactNumber, 
					SleepMode sleepMode, String userAgent, URL proxy) {
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
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public URL getURL() {
		return URL;
	}

	public void setURL(URL URL) {
		this.URL = URL;
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

	public URL getProxy() {
		return proxy;
	}

	public void setProxy(URL proxy) {
		this.proxy = proxy;
	}
	
	@Override
	public String toString() {

		String url, period, maxcontact, sleepmode, useragent, proxy;

		url = this.URL.toString();
		period = this.periodicRangeSec.toString();
		maxcontact = this.maxContactNumber.toString();
		sleepmode = this.sleepMode.toString();
		useragent = this.userAgent;
		proxy = this.proxy.toString();

	  return "--url " + url + 
			 " --period " + period + 
			 " --maxcontact " + maxcontact + 
			 " --sleepmode " + sleepmode + 
			 " --useragent " + useragent + 
			 " --proxy " + proxy;
	}
	
	public String toStringForLog() {

		String url, period, maxcontact, sleepmode, useragent, proxy;

		url = this.URL.toString();
		period = this.periodicRangeSec.toString();
		maxcontact = this.maxContactNumber.toString();
		sleepmode = this.sleepMode.toString();
		useragent = this.userAgent;
		proxy = this.proxy.toString();

	  return url + "\n" + period + " " + maxcontact + " " + sleepmode + "\n" + useragent + " " + proxy;
	}

}

