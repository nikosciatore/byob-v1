package control.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import application.Main;
import model.SystemInfoEntry;
import model.URLEntry;

public class SocketBotThread extends Thread{

	Socket clientSocket;
	
	ArrayList<URLEntry> inContactList;
	ArrayList<SystemInfoEntry> outSystemInfo;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		
		synchronized (this) {
			
		
			try {
				clientSocket = new Socket("localhost", 6789);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
	
				ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
	
	     
	            outSystemInfo = Main.bot.getSystemInfoEntryList();
	            outToServer.writeObject(outSystemInfo);
	
	            inContactList = (ArrayList<URLEntry>) inFromServer.readObject();
	            
	            Main.bot.setContactsList(inContactList);
	            
	            
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			notify();	
		}
	}
	
}
