package byobv1;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Timer;

import byobv1.model.Bot;

public class Main {
	
	public static boolean DEBUG = true;
	
	static Bot bot;
	static Config config;
	static Log log;
	
	static Path prjDirPath, dataDirPath, configFilePath, logFilePath;		

	static ArrayList<ContactThread> contactThreadList;
	
	public static void main(String[] args) {

		init();		
		startGUI();
		start();
		exit();
		
	}

	private static void startGUI() {
		//TODO gui
		/*JavaFX*/
		/*the method implements the first extension*/		
	}

	private static void init() {
		gatherInfo();
		
		prjDirPath = Paths.get(System.getProperty("user.dir"));
		dataDirPath = prjDirPath.resolve("data");
		configFilePath = dataDirPath.resolve("config.txt");
		logFilePath = dataDirPath.resolve("log.txt");

		config = new Config();
		bot = new Bot();
		log = new Log();
		
		bot.setContactsList(config.readFile(configFilePath));
		
		log.openOrCreateLogFile(logFilePath);
		
	}

	private static void gatherInfo() {
		//TODO gather info
//		System.out.println(System.getProperty("os.name\n"));
		/*the method implements the third extension*/		
	}
	
	private static void start() {
		contactThreadList = new ArrayList<ContactThread>();
		Timer timer = new Timer();
		
		for (int i = 0; i < bot.getContactsList().size(); i++) {
			ContactThread cThread = new ContactThread(bot.getContactsList().get(i), timer);
			contactThreadList.add(cThread);
			timer.schedule(cThread, 0);
		}
		
		
	}
	
	private static void exit() {
//		if(config.hasBeenModified(configFilePath, bot.getContactsList())){
			config.writeFile(configFilePath, bot.getContactsList());
//			System.out.println("config file written");
//		}
		System.out.println("Exit");
		
	}

}
