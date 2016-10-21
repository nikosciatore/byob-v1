package byobv1;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

import byobv1.model.Bot;

public class Main {
	
	static boolean DEBUG = true;
	
	static Bot bot;
	static Config config;
	static Log log;
	
	static Path prjDirPath, dataDirPath, configFilePath, logFilePath;		

	public static void main(String[] args) {

		init();		
		startGUI();
		start();
		exit();
		
	}

	private static void exit() {
		config.writeFile(configFilePath, bot.getContactsList());
		System.out.println("Exit");
		
	}

	private static void startGUI() {
		//TODO
		/*JavaFX*/
		/*the method implements the first extension*/		
	}

	private static void start() {
		Timer timer = new Timer();

		timer.scheduleAtFixedRate(
		    new TimerTask()
		    {
		        public void run()
		        {
		            System.out.println("3 seconds passed");
		        }
		    },
		    0,      // run first occurrence immediately
		    3000);  // run every three seconds
		
//		for (int i = 0; i < bot.getContactsList().size(); i++) {
//			
//		}
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
		//TODO
//		System.out.println(System.getProperty("os.name\n"));
		/*the method implements the third extension*/		
	}

}
