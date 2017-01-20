package model;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe che rappresenta una riga del file di configurazione.
 */
public class URLEntry  implements Serializable{

	private static final long serialVersionUID = -7758601941231295030L;

	private Integer ID;
	private URL URL;
	private Range period;
	private Integer maxContactNumber;
	private SleepMode sleepMode;
	private String userAgent;
	private String proxy;



	public URLEntry(Integer ID, URL URL, Range periodicRangeSec, Integer maxContactNumber, 
					SleepMode sleepMode, String userAgent, String proxy) {
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
		userAgent = "";
		proxy = "";
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

	public Range getPeriod() {
		return period;
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

	public Integer getMaxContactNumber() {
		return maxContactNumber;
	}

	public void setMaxContactNumber(String maxContactNumberString) {
		try {
			this.maxContactNumber = Integer.parseInt(maxContactNumberString);
		} catch (NumberFormatException e) {
			this.maxContactNumber = new Integer(0);
		}
	}

	public SleepMode getSleepMode() {
		return sleepMode;
	}

	public void setSleepMode(String sleepModeString) {
		String [] strings;
		strings = sleepModeString.split("-");
		this.sleepMode = new SleepMode(strings[0], strings[1], strings[2]);
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
	
	/**
	 * Override del metodo toString() per le scrittura di un oggetto 
	 * di tipo URLEntry in base al formato definito per il file di
	 * configurazione
	 */
	@Override
	public String toString() {

		String url, period, maxcontact, sleepmode, useragent, proxy;
		String urlPair, periodPair, maxcontactPair, sleepmodePair, useragentPair, proxyPair;

		url = this.URL.toString();
		period = this.period.toString();
		maxcontact = this.maxContactNumber.toString();
		sleepmode = this.sleepMode.toString();
		useragent = this.userAgent;
		proxy = this.proxy.toString();

		urlPair = (this.URL.toString().equals("")) ? "" : "--url " + url;
		periodPair = (this.period.toString().equals("")) ? "" : " --period " + period;
		maxcontactPair = (this.maxContactNumber.toString().equals("")) ? "" : " --maxcontact " + maxcontact;
		sleepmodePair = (this.sleepMode.toString().equals("")) ? "" : " --sleepmode " + sleepmode;
		useragentPair = (this.userAgent.toString().equals("")) ? "" : " --useragent " + useragent;
		proxyPair = (this.proxy.toString().equals("")) ? "" : " --proxy " + proxy;
		
		return urlPair + periodPair + maxcontactPair + sleepmodePair + useragentPair + proxyPair;
	}
	
	
	/**
	 * Metodo per la scrittura di un oggetto 
	 * di tipo URLEntry in base al formato definito per il file di
	 * di log
	 */
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

	/**
	 * Metodo per verificare se nell'istante attuale si debbano o meno
	 * effettuare le richieste sulla base del campo sleepmode del
	 * file di configurazione
	 * @return true se le richieste devono essere effettuate
	 * false altrimenti
	 */
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

	/**
	 * Genera in maniera casuale, un intero compreso nell'intervallo 
	 * specificato nel campo period del file di configurazione 
	 * @return es: se period è 5-10 il metodo genera un numero casuale 
	 * compreso tra 5 e 10
	 */
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
}

