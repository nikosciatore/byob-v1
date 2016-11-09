package model;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class URLEntry {
	protected Integer ID;
	protected URL URL;
	protected Range period;
	protected Integer maxContactNumber;
	protected SleepMode sleepMode;
	protected String userAgent;
	protected URL proxy;

	public URLEntry(Integer ID, URL URL, Range periodicRangeSec, Integer maxContactNumber, 
					SleepMode sleepMode, String userAgent, URL proxy) {
		this.ID = ID;
		this.URL = URL;
		this.period = periodicRangeSec;
		this.maxContactNumber = maxContactNumber;
		this.sleepMode = sleepMode;
		this.userAgent = userAgent;
		this.proxy = proxy;
	}
	
	public URLEntry() {
		URL = null;
		period = null;
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
		return period;
	}

	public void setPeriod(Range periodicRangeSec) {
		this.period = periodicRangeSec;
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
		period = this.period.toString();
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
		period = this.period.toString();
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

	public Integer generatePeriod(){
		Integer returnValue, range, randomValueInteger;
		Double randomValueDouble;
		range = period.getRange();
		
		randomValueDouble = Math.random()*range;
		if(randomValueDouble - randomValueDouble.intValue() < 0.5){
			randomValueInteger = randomValueDouble.intValue();
		}else{
			randomValueInteger = randomValueDouble.intValue() + 1;			
		}
		returnValue = period.getMin() + randomValueInteger;
		
		return returnValue;
	}

	public void setURL(String URLString) {
		try {
			this.URL = new URL(URLString);
		} catch (MalformedURLException e) {
			try {
				this.URL = new URL("http://www.example.com");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void setPeriod(String periodString) {
		String [] strings;
		Integer min,max;
		strings = periodString.split("-");

		try {
			min = Integer.parseInt(strings[0]);
			max = Integer.parseInt(strings[1]);			
		} catch (Exception e) {
			min = new Integer(1);
			max = new Integer(1);
		}
		
		this.period = new Range(min, max);
	}

	public void setProxy(String proxyString) {
		try {
			this.proxy = new URL(proxyString);
		} catch (MalformedURLException e) {
			try {
				this.proxy = new URL("http://www.example.com");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void setMaxContactNumber(String maxContactNumberString) {
		try {
			this.maxContactNumber = Integer.parseInt(maxContactNumberString);
		} catch (NumberFormatException e) {
			this.maxContactNumber = new Integer(0);
		}
	}

	public void setSleepMode(String sleepModeString) {
		String [] strings;
		strings = sleepModeString.split("-");
		this.sleepMode = new SleepMode(strings[0], strings[1], strings[2]);
	}
	
}

