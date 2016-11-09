package model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Timer;
import control.Config;
import control.ContactThread;
import control.Log;

public class Bot {
	Integer id;
	BotStatus status;
	ArrayList<URLEntry> contactsList;
	
	static Config config;
	static Log log;
	static ArrayList<ContactThread> contactThreadList;

	static Path prjDirPath, dataDirPath, configFilePath, logFilePath;		

	
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
		gatherInfo();
		
		prjDirPath = Paths.get(System.getProperty("user.dir"));
		dataDirPath = prjDirPath.resolve("data");
		configFilePath = dataDirPath.resolve("config.txt");
		logFilePath = dataDirPath.resolve("log.txt");

		config = new Config();
		log = new Log();
		
		contactsList = config.readFile(configFilePath);
		
		log.openOrCreateLogFile(logFilePath);	}



	private void gatherInfo() {
		//TODO gather info
//		System.out.println(System.getProperty("os.name\n"));
		/*the method implements the third extension*/		
	}

	public void close() {
//		if(config.hasBeenModified(configFilePath, bot.getContactsList())){
			config.writeFile(configFilePath, contactsList);
//			System.out.println("config file written");
//		}		
	}

	public void start() {
		contactThreadList = new ArrayList<ContactThread>();
		Timer timer = new Timer();
		
		for (int i = 0; i < contactsList.size(); i++) {
			ContactThread cThread = new ContactThread(contactsList.get(i), timer);
			contactThreadList.add(cThread);
			timer.schedule(cThread, 0);
		}
	}

	public ArrayList<URLEntryProperty> getContactsListProperty() {
		ArrayList<URLEntryProperty> returnValue = new ArrayList<URLEntryProperty>();
		
		for (int i = 0; i < contactsList.size(); i++) {
			returnValue.add(new URLEntryProperty(contactsList.get(i)));
		}

		return returnValue;
	}

	public void addContact(URLEntry newUrlEntry) {
		newUrlEntry.setID(contactsList.size()+1);
		
		this.contactsList.add(newUrlEntry);
	}

	public void removeContact(Integer id) {
		for (int i = 0; i < contactsList.size(); i++) {
			if(contactsList.get(i).getID().equals(id)){
				contactsList.remove(i);
			}
		}
	}



}
