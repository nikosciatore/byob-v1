package control.client;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Timer;
import control.Config;
import control.Utility;
import control.server.ProgramLog;
import model.BotStatus;
import model.SortMode;
import model.SystemInfoEntry;
import model.URLEntry;
import model.gui.URLEntryProperty;

public class Bot {
	String botId;
	BotStatus status;
	Timer timer;
	
	Config config;
	Log log;
	ProgramLog programLog;
	SystemInfoBot systemInfoBot;

	ArrayList<ContactThread> contactThreadList;

	Path prjDirPath, dataDirPath, configFilePath, logFilePath, sysInfoFilePath, botDirPath;		

	SocketBotThread socketBotThread;

	
	public Bot() {
		botId = Utility.generateID(SortMode.RANDOM);
		status = BotStatus.IDLE;
	}
	

	public Config getConfig() {
		return config;
	}
	public String getId() {
		return botId;
	}
	public void setId(String id) {
		this.botId = id;
	}
	public BotStatus getStatus() {
		return status;
	}
	public void setStatus(BotStatus status) {
		this.status = status;
	}
	public ArrayList<URLEntry> getContactsList() {
		return config.getContactsList();
	}
	
	/**
	 * Set contact list and save configuration file
	 * */
	public void setContactsList(ArrayList<URLEntry> contactsList) {
		config.setContactsList(contactsList);
		config.writeFile();

	}

	public ArrayList<SystemInfoEntry> getSystemInfoEntryList() {
		return systemInfoBot.getSystemInfoEntryList();
	}
	
	public void init() {

		prjDirPath = Paths.get(System.getProperty("user.dir"));
		dataDirPath = prjDirPath.resolve("data");
		botDirPath = dataDirPath.resolve("bot");
		configFilePath = botDirPath.resolve("config.txt");
		logFilePath = botDirPath.resolve("log.txt");
		sysInfoFilePath = botDirPath.resolve("sysinfo.txt");

		config = new Config(configFilePath);
		log = new Log(logFilePath);
		programLog = ProgramLog.getProgramLog();
		systemInfoBot = new SystemInfoBot(sysInfoFilePath);
		
		config.openOrCreateConfigFile();
		config.readFile();
		
			
		log.openOrCreateLogFile();
		programLog.addInfo("Program Started");

		systemInfoBot.overwriteSystemInfoFile();
		systemInfoBot.writeSystemInfoFile(botId);

		if(config.getContactsList().size()==0 || config.getConfigHeader().getTtl().intValue() == 0){
			socketBotThread = new SocketBotThread();
			socketBotThread.start();
			synchronized (socketBotThread) {
				try {
					socketBotThread.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(config.getConfigHeader().decreaseTtl()){
			config.writeFile();
		}

		
		start();
		
		
		
	}
/*TODO trovare il modo di chiamare questo metodo nel client*/
	public void close() {
		config.writeFile();

		try {
			timer.cancel();	
		} catch (NullPointerException e) {
			
		}
	}

	public void start() {
		
		contactThreadList = new ArrayList<ContactThread>();
		timer = new Timer();
		
		for (int i = 0; i < config.getContactsList().size(); i++) {
			ContactThread cThread = new ContactThread(config.getContactsList().get(i), timer);
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
		
		for (int i = 0; i < config.getContactsList().size(); i++) {
			returnValue.add(new URLEntryProperty(config.getContactsList().get(i)));
		}

		return returnValue;
	}

	public URLEntry addContact(URLEntry newUrlEntry) {
		int lastContactId = 0;
		if(!config.getContactsList().isEmpty()){
			lastContactId = config.getContactsList().get(config.getContactsList().size()-1).getID();
		}
		newUrlEntry.setID(lastContactId+1);
		config.getContactsList().add(newUrlEntry);
		return newUrlEntry;
	}

	public void removeContact(Integer id) {
		for (int i = 0; i < config.getContactsList().size(); i++) {
			if(config.getContactsList().get(i).getID().equals(id)){
				config.getContactsList().remove(i);
			}
		}
	}

	public void editContact(URLEntry newUrlEntry) {
		int i;
		for (i = 0; i < config.getContactsList().size(); i++) {
			if(config.getContactsList().get(i).getID()==newUrlEntry.getID()){
				break;
			}
		}
		config.getContactsList().set(i, newUrlEntry);
	}
}
