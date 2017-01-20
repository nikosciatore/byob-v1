package control.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import control.server.ProgramLog;
import javafx.application.Platform;
import model.LogEntry;
import model.URLEntry;

/**
 * Thread periodico responsabile dell'invio delle richieste HTTP 
 * relativamente ad un contatto presente nel file di configurazione
 */
public class ContactThread extends TimerTask{

	URLEntry urlEntry;
	LogEntry logEntry;
	Log log;
	ProgramLog programlog;
	Timer timer;
	Integer contactNumber;
	
	static boolean READ_RESPONSE = false;
	
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
	
	public int getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(int contactNumber) {
		this.contactNumber = contactNumber;
	}

	/**
	 * Il metodo run() invia o meno una richiesta HTTP sulla base del campo sleepmode
	 * e se non è stato raggiunto il numero massimo di richieste posticipa la esecuzione
	 * avanti nel tempo sulla base del campo period.
	 */
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
	 * Metodo per l'invio di una richiesta HTTP
	 *
	 * @return	true o false in base all'esito della richiesta
	 */
	private boolean HTTPGet() {
		boolean returnValue = true;
//		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		try {
			StringBuilder response = new StringBuilder();
			HttpURLConnection connection;
			if(!urlEntry.getProxy().equals("")){
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(urlEntry.getProxy(), 8080));
				connection = (HttpURLConnection) urlEntry.getURL().openConnection(proxy);
			}else{
				connection = (HttpURLConnection) urlEntry.getURL().openConnection();
			}
			
			connection.setRequestMethod("GET");
			
			if(!urlEntry.getUserAgent().equals("")){
				connection.setRequestProperty("User-Agent", urlEntry.getUserAgent());
			}
  
			/*in questo punto viene effettuata la richiesta http*/
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			if(READ_RESPONSE){
				String line;
				while ((line = rd.readLine()) != null) {
					response.append(line);
				}			  
				rd.close();
				System.out.println(response.toString());
			}

			logEntry = new LogEntry(urlEntry, date);
			log.writeLogFile(logEntry, contactNumber);

		} catch (UnknownHostException e) {
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					programlog.addWarning("Unknow Host: " + e.getMessage() + " for ID=" + urlEntry.getID());
					System.out.println("Unknow Host: " + e.getMessage() + " for ID=" + urlEntry.getID());
				}
			});
			
			returnValue = false;
		} catch (Exception e) {
			e.printStackTrace();
		} 		
		return returnValue;
	}
}
