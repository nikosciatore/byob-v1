package application;
	

import javafx.application.Application;
import javafx.stage.Stage;
import model.Bot;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	public static boolean DEBUG = true;
	
	public static Bot bot;
	
	public static Stage stage;
	
	public static UserInterfaceController uiController;
	
	VBox root;
	Scene scene;
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		stage = primaryStage;
				
		
		bot = new Bot();
		bot.init();

		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("user_interface.fxml"));
		root = (VBox)loader.load();
		uiController = loader.getController();
//		root = (VBox)FXMLLoader.load(getClass().getResource("user_interface.fxml"));
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
		bot.close();
		super.stop();
	}
	
	public static void main(String[] args) {
		if(args[0].equals("client")){
			System.out.println("I'm the client");
		}else if (args[0].equals("server")){
			launch(args);
		}
	}
}
