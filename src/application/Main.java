package application;
	

import control.client.Bot;
import control.server.BotServer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	public static boolean DEBUG = true;

	public static boolean SERVER;
	
	
	public static BotServer botServer;
	public static Bot bot;
	
	public static Stage stage;
	
	public static UserInterfaceController uiController;
	
	VBox root;
	Scene scene;
		
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		stage = primaryStage;
//		root = (VBox)FXMLLoader.load(getClass().getResource("user_interface.fxml"));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("user_interface.fxml"));
		root = (VBox)loader.load();
		uiController = loader.getController();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("user_interface.css").toExternalForm());
		primaryStage.setTitle("BYOBv1");
		primaryStage.setScene(scene);
		primaryStage.show();

		
}
	
	public static UserInterfaceController getUiController() {
		return uiController;
	}

	@Override
	public void stop() throws Exception {
		if(SERVER){
			botServer.close();
		}else{
			bot.close();
		}
		super.stop();
	}
	
	public static void main(String[] args) {
		
		if(args[0].equals("client")){
			
			bot = new Bot();
			
			bot.init();
			SERVER = false;

		}else if (args[0].equals("server")){

			botServer = new BotServer();
			botServer.init();
			SERVER = true;

			launch(args);
		}
	}
}
