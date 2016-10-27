package byobv1.model;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public void setID(Integer ID) {
		this.ID = ID;
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

	public boolean isNowActiveTime(){
		DateFormat monthFormat = new SimpleDateFormat("MM");
		DateFormat dayFormat = new SimpleDateFormat("dd");
		DateFormat hourFormat = new SimpleDateFormat("HH");
		
		Date date = new Date();
		
		Integer month,day,hour;
		month = Integer.parseInt(monthFormat.format(date));
		day = Integer.parseInt(dayFormat.format(date));
		hour = Integer.parseInt(hourFormat.format(date));
		
		SleepMode now = SleepMode.integersToSleepMode(month, day, hour);
		
		return sleepMode.isIncluded(now);
	}

	public Integer getPeriod(){
		Integer returnValue, range, randomValueInteger;
		Double randomValueDouble;
		range = periodicRangeSec.getRange();
		
		randomValueDouble = Math.random()*range;
		if(randomValueDouble - randomValueDouble.intValue() < 0.5){
			randomValueInteger = randomValueDouble.intValue();
		}else{
			randomValueInteger = randomValueDouble.intValue() + 1;			
		}
		returnValue = periodicRangeSec.getMin() + randomValueInteger;
		
		return returnValue;
	}
	

}

