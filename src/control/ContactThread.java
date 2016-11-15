package control;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import model.LogEntry;
import model.URLEntry;

public class ContactThread extends TimerTask{

	URLEntry urlEntry;
	LogEntry logEntry;
	Log log;
	Timer timer;
	Integer contactNumber;
	
	
	public int getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(int contactNumber) {
		this.contactNumber = contactNumber;
	}

	public ContactThread(URLEntry urlEntry, Timer timer) {
		this.urlEntry = urlEntry;
		this.timer = timer;
		this.contactNumber = 0;
		this.log = new Log();
	}

	public ContactThread(URLEntry urlEntry, Timer timer, Integer contactNumber) {
		this.urlEntry = urlEntry;
		this.timer = timer;
		this.contactNumber = contactNumber;
		this.log = new Log();
	}
	
	@Override
	public void run() {
        Integer periodTime = urlEntry.generatePeriod();
        int maxContactNumber;
        
        if(urlEntry.isNowActiveTime()){
            httpGET();
            
            contactNumber++;
            
            maxContactNumber = urlEntry.getMaxContactNumber();
            if(contactNumber < maxContactNumber){
                timer.schedule(new ContactThread(urlEntry, timer, contactNumber), periodTime * 1000);
            }
        }

        
	}

	private void httpGET() {
//		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		
		logEntry = new LogEntry(urlEntry, date);
		log.writeLogFile(logEntry);
		
		

	}

	
}
