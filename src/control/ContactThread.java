package control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import application.ProgramLog;
import javafx.application.Platform;
import model.LogEntry;
import model.URLEntry;

public class ContactThread extends TimerTask{

	URLEntry urlEntry;
	LogEntry logEntry;
	Log log;
	ProgramLog programlog;
	Timer timer;
	Integer contactNumber;
	
	static boolean READ_RESPONSE = false;
	
	public int getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(int contactNumber) {
		this.contactNumber = contactNumber;
	}

	public ContactThread(URLEntry urlEntry, Timer timer) {
		this.urlEntry = urlEntry;
		this.timer = timer;
		this.contactNumber = 1;
		this.log = new Log();
		this.programlog = ProgramLog.getProgramLog();
		
	}

	public ContactThread(URLEntry urlEntry, Timer timer, Integer contactNumber) {
		this.urlEntry = urlEntry;
		this.timer = timer;
		this.contactNumber = contactNumber;
		this.log = new Log();
		this.programlog = ProgramLog.getProgramLog();
	}
	
	@Override
	public void run() {
        Integer periodTime = urlEntry.generatePeriod();
        int maxContactNumber;
        
        if(urlEntry.isNowActiveTime()){
            
        	if(HTTPGet()){
                contactNumber++;
                
                maxContactNumber = urlEntry.getMaxContactNumber();
                if(contactNumber <= maxContactNumber){
                    timer.schedule(new ContactThread(urlEntry, timer, contactNumber), periodTime * 1000);
                }else{
                	this.cancel();
                }
        	}
            
        }

        
	}


	/**
	 * The method send the HTTP request 
	 *
	 * @return	true o false based on the success of request
	 */
	private boolean HTTPGet() {
		boolean returnValue = true;
//		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		try {
			StringBuilder result = new StringBuilder();
			HttpURLConnection conn;
			if(!urlEntry.getProxy().equals("")){
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(urlEntry.getProxy(), 8080));
				conn = (HttpURLConnection) urlEntry.getURL().openConnection(proxy);
			}else{
				conn = (HttpURLConnection) urlEntry.getURL().openConnection();
			}
			
			conn.setRequestMethod("GET");
			
			if(!urlEntry.getUserAgent().equals("")){
				conn.setRequestProperty("User-Agent", urlEntry.getUserAgent());
			}
  
			/*in questo punto viene effettuata la richiesta http*/
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			if(READ_RESPONSE){
				String line;
				while ((line = rd.readLine()) != null) {
				   result.append(line);
				}			  
				rd.close();
				System.out.println(result.toString());
			}

			logEntry = new LogEntry(urlEntry, date);
			log.writeLogFile(logEntry, contactNumber);

		} catch (UnknownHostException e) {
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					programlog.addWarning("Unknow Host: " + e.getMessage() + " for ID=" + urlEntry.getID());
				}
			});
			
			returnValue = false;
		} catch (Exception e) {
			e.printStackTrace();
		} 		
		return returnValue;
	}
}
