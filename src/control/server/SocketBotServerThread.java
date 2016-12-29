package control.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import application.Main;
import model.SystemInfoEntry;
import model.URLEntry;

public class SocketBotServerThread extends Thread{

	ProgramLog programLog;
	ServerSocket serverSocket;
	Socket connectionSocket;
	ObjectOutputStream outToClient;
    ObjectInputStream inFromClient;				

	
	ArrayList<URLEntry> outContactList;
	ArrayList<SystemInfoEntry> inSystemInfo;
	
	
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

				programLog.addInfo("Bot master waiting");
				
				connectionSocket = serverSocket.accept();
				programLog.addInfo("Accepted request");
				outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
	            inFromClient = new ObjectInputStream(connectionSocket.getInputStream());				

	            inSystemInfo = (ArrayList<SystemInfoEntry>) inFromClient.readObject();		  
	            
	            Main.botServer.addSystemInfo(inSystemInfo);
	            
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
