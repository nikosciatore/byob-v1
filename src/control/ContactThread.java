package control;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import model.URLEntry;

public class ContactThread extends TimerTask{

	URLEntry urlEntry;
	Timer timer;
	static int contactNumber;
	
	
	public ContactThread(URLEntry urlEntry, Timer timer) {
		this.urlEntry = urlEntry;
		this.timer = timer;
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
                timer.schedule(new ContactThread(urlEntry, timer), periodTime * 1000);
            }
        }

        
	}

	private void httpGET() {
        /*TODO log httpget*/
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		
		System.out.println(urlEntry.getID() + " " + dateFormat.format(date) + " " + urlEntry.getURL());

	}

	
}
