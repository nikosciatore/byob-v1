package model;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Timer;
import application.Main;
import application.ProgramLog;
import control.Config;
import control.ContactThread;
import control.Log;

public class Bot {
	String id;
	BotStatus status;
	ArrayList<URLEntry> contactsList;
	Timer timer;
	
	Config config;
	Log log;
	ProgramLog programLog;
	SystemInfo systemInfo;

	ArrayList<ContactThread> contactThreadList;

	Path prjDirPath, dataDirPath, configFilePath, logFilePath, sysInfoFilePath;		

	public Bot() {
		id = generateID();
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
		return id;
	}
	public void setId(String id) {
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
		sysInfoFilePath = dataDirPath.resolve("sysinfo.txt");

		config = new Config();
		log = new Log(logFilePath);
		programLog = ProgramLog.getProgramLog();
		systemInfo = new SystemInfo(sysInfoFilePath);
		
		contactsList = config.readFile(configFilePath);
		
		log.openOrCreateLogFile();
		programLog.addInfo("Program Started");
		

		systemInfo.openOrCreateSystemInfoFile();;
		systemInfo.writeSystemInfoFile(id);
		
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
		
		Main.uiController.setTab("log");
		
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
