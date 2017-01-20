package model;

import java.util.Date;

/**
 * Classe che rappresenta una riga del file di log
 * Estende la classe URLEntry in quanto contiene tutti i campi 
 * contenuti in quest'ultima.
 */
public class LogEntry extends URLEntry {

	private static final long serialVersionUID = -746438041986727131L;

	private Date timestamp;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public LogEntry(URLEntry urlEntry, Date timestamp) {
		super(urlEntry.getID(), urlEntry.getURL(), urlEntry.getPeriod(), urlEntry.getMaxContactNumber(), 
			  urlEntry.getSleepMode(), urlEntry.getUserAgent(), urlEntry.getProxy());
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return timestamp.toString() + "\n" + super.toStringForLog();
	}
}
