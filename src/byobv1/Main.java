package byobv1;

import java.nio.file.Path;
import java.nio.file.Paths;

import byobv1.model.Bot;

public class Main {
	
	static boolean DEBUG = true;
	
	static Bot bot;
	static Config config;
	static Log log;
	
	public static void main(String[] args) {

		init();
		startGUI();
		start();
		
	}

	private static void startGUI() {
		//TODO
		/*the method implements the first extension*/		
	}

	private static void start() {
		// TODO Auto-generated method stub
		
	}

	private static void init() {
		gatherInfo();
		
		Path prjDirPath, dataDirPath, configFilePath;		
		prjDirPath = Paths.get(System.getProperty("user.dir"));
		dataDirPath = prjDirPath.resolve("data");
		configFilePath = dataDirPath.resolve("config.txt");

		config = new Config();
		bot = new Bot();
		bot.setContactsList(config.readFile(configFilePath));
		
		
	}

	private static void gatherInfo() {
		//TODO
		/*the method implements the third extension*/		
	}

}
