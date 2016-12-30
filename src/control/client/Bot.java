package control.client;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Timer;
import control.Config;
import control.server.ProgramLog;
import model.BotStatus;
import model.SystemInfoEntry;
import model.URLEntry;
import model.gui.URLEntryProperty;

public class Bot {
	String botId;
	BotStatus status;
	ArrayList<URLEntry> contactsList;
	Timer timer;
	
	Config config;
	Log log;
	ProgramLog programLog;
	SystemInfoBot systemInfoBot;

	ArrayList<ContactThread> contactThreadList;

	Path prjDirPath, dataDirPath, configFilePath, logFilePath, sysInfoFilePath, botDirPath;		

	SocketBotThread socketBotThread;

	
	public Bot() {
		botId = generateID();
		status = BotStatus.IDLE;
		contactsList = new ArrayList<URLEntry>();
	}
	
	private String generateID() {
		String macAdd = getMacAddress();
        macAdd = macAdd.replace("-", "");
        char[] macAddChars = macAdd.toCharArray();
        Arrays.sort(macAddChars);        
        String macAddSorted = new String(macAddChars);
		return  macAddSorted;
	}

	private String getMacAddress() {
		String returnValue = null;
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

			for (NetworkInterface networkInterface : Collections.list(networkInterfaces)){
		
				byte[] mac = networkInterface.getHardwareAddress();

				if(mac==null){
					continue;
				}
				
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < mac.length; j++) {
					sb.append(String.format("%02X%s", mac[j], (j < mac.length - 1) ? "-" : ""));
				}
				returnValue = sb.toString();
				
				break;
			}
				
		} catch (SocketException e) {
			e.printStackTrace();
		}
				return returnValue;
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
		return contactsList;
	}
	
	/**
	 * Set contact list and save configuration file
	 * */
	public void setContactsList(ArrayList<URLEntry> contactsList) {
		this.contactsList = contactsList;
		config.writeFile(configFilePath, contactsList);

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
		contactsList = config.readFile(configFilePath);
		
		log.openOrCreateLogFile();
		programLog.addInfo("Program Started");

		systemInfoBot.overwriteSystemInfoFile();
		systemInfoBot.writeSystemInfoFile(botId);

		if(contactsList.size()==0){
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
		
		start();
		
		
		
	}
/*TODO trovare il modo di chiamare questo metodo nel client*/
	public void close() {
		config.writeFile(configFilePath, contactsList);

		try {
			timer.cancel();	
		} catch (NullPointerException e) {
			
		}
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
