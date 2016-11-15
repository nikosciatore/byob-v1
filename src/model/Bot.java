package model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Timer;

import application.ProgramLog;
import control.Config;
import control.ContactThread;
import control.Log;

public class Bot {
	Integer id;
	BotStatus status;
	ArrayList<URLEntry> contactsList;
	Timer timer;
	
	Config config;
	Log log;
	ProgramLog programLog;
	SystemInfo systemInfo;

	ArrayList<ContactThread> contactThreadList;

	Path prjDirPath, dataDirPath, configFilePath, logFilePath;		

	
	public Bot() {
		id = generateID();
		status = BotStatus.IDLE;
		contactsList = new ArrayList<URLEntry>();
	}
	
	private Integer generateID() {
		Double returnValue = Math.random()*1000000000;
		return  returnValue.intValue();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BotStatus getStatus() {
		return status;
	}
	public void setStatus(BotStatus status) {
		this.status = status;
	}
	public ArrayList<URLEntry> getContactsList() {
		return contactsList;
	}
	public void setContactsList(ArrayList<URLEntry> contactsList) {
		this.contactsList = contactsList;
	}

	public void init() {
		
		prjDirPath = Paths.get(System.getProperty("user.dir"));
		dataDirPath = prjDirPath.resolve("data");
		configFilePath = dataDirPath.resolve("config.txt");
		logFilePath = dataDirPath.resolve("log.txt");

		config = new Config();
		log = new Log(logFilePath);
		programLog = new ProgramLog();
		systemInfo = new SystemInfo();
		
		contactsList = config.readFile(configFilePath);
		
		log.openOrCreateLogFile();
		programLog.addInfo("Prova Info");
		programLog.addWarning("Prova Warning");
		programLog.addError("Prova Error");
		
		systemInfo.gatherInfo();
		
	}

	public void close() {

		try {
			timer.cancel();	
		} catch (NullPointerException e) {
			
		}

//		if(config.hasBeenModified(configFilePath, bot.getContactsList())){
			config.writeFile(configFilePath, contactsList);
//			System.out.println("config file written");
//		}		
	}

	public void start() {
		contactThreadList = new ArrayList<ContactThread>();
		timer = new Timer();
		
		for (int i = 0; i < contactsList.size(); i++) {
			ContactThread cThread = new ContactThread(contactsList.get(i), timer);
			contactThreadList.add(cThread);
			timer.schedule(cThread, 0);
		}
	}

	public void stop() {
		timer.cancel();
		for (int i = 0; i < contactThreadList.size(); i++) {
			contactThreadList.get(i).setContactNumber(0);
		}
	}
	
	public void pause() {
		/*TODO*/
	}

	public void resume() {
		/*TODO*/
	}
	
	public ArrayList<URLEntryProperty> getContactsListProperty() {
		ArrayList<URLEntryProperty> returnValue = new ArrayList<URLEntryProperty>();
		
		for (int i = 0; i < contactsList.size(); i++) {
			returnValue.add(new URLEntryProperty(contactsList.get(i)));
		}

		return returnValue;
	}

	public URLEntry addContact(URLEntry newUrlEntry) {
		int lastContactId = 0;
		if(!contactsList.isEmpty()){
			lastContactId = contactsList.get(contactsList.size()-1).getID();
		}
		newUrlEntry.setID(lastContactId+1);
		this.contactsList.add(newUrlEntry);
		return newUrlEntry;
	}

	public void removeContact(Integer id) {
		for (int i = 0; i < contactsList.size(); i++) {
			if(contactsList.get(i).getID().equals(id)){
				contactsList.remove(i);
			}
		}
	}

	public void editContact(URLEntry newUrlEntry) {
		int i;
		for (i = 0; i < contactsList.size(); i++) {
			if(contactsList.get(i).getID()==newUrlEntry.getID()){
				break;
			}
		}
		contactsList.set(i, newUrlEntry);
	}
}
