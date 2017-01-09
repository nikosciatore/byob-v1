package control.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import application.Main;
import control.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BotIdEntry;
import model.SystemInfo;
import model.SystemInfoEntry;
import model.gui.BotIdEntryProperty;
import model.gui.SystemInfoEntryProperty;

public class SystemInfoBotServer {
	
	static Path filePath;
	ArrayList<SystemInfo> systemInfoList;
	

	static ObservableList<BotIdEntryProperty> botIdEntryObservableList;
	

	public SystemInfoBotServer(Path filePath) {
		SystemInfoBotServer.filePath = filePath;
		botIdEntryObservableList = FXCollections.observableArrayList();
	}

	public static ObservableList<BotIdEntryProperty> getBotIdEntryObservableList() {
		return botIdEntryObservableList;
	}

	public ArrayList<SystemInfo> getSystemInfoList() {
		return systemInfoList;
	}

	
	
	public void readFile(){
		systemInfoList = new ArrayList<SystemInfo>();
		SystemInfo tempSystemInfo;
		
		Charset charset = Charset.forName("ISO-8859-1");
		try (BufferedReader reader = Files.newBufferedReader(filePath, charset)) {
			String line = null;
		    while ((line = reader.readLine()) != null) {
		    	if(line.charAt(0)=='#'){
		    		continue;
		    	}
		    	tempSystemInfo = parseSystemInfo(line, reader);
		    	systemInfoList.add(tempSystemInfo);
		    	botIdEntryObservableList.add(new BotIdEntryProperty(new BotIdEntry(tempSystemInfo.getSystemInfoEntryList().get(0).getValue())));
		    }
		    reader.close();
		} catch (NoSuchFileException e) {
			e.printStackTrace();
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		} 		
	}
	
	private SystemInfo parseSystemInfo(String line, BufferedReader reader) throws IOException {
		SystemInfo returnValue;
		ArrayList<SystemInfoEntry> systemInfoEntryList = new ArrayList<SystemInfoEntry>();

		int lineLenght; 
		try {
			lineLenght = line.length();			
		} catch (NullPointerException e) {
			lineLenght = 0;
		}
		
    	while (lineLenght!=0) {
    		ArrayList<String> entries = Utility.splitInArrayList(line,": ");
    		systemInfoEntryList.add(new SystemInfoEntry(entries.get(0), entries.get(1)));
    		line = reader.readLine();
    		
    		try {
    			lineLenght = line.length();			
    		} catch (NullPointerException e) {
    			lineLenght = 0;
    		}
		}
    	returnValue = new SystemInfo(systemInfoEntryList);
		return returnValue;
	}
	
	
	public void writeFile() {
		
		overwriteSystemInfoFile();
		
		Charset charset = Charset.forName("ISO-8859-1");
		try (BufferedWriter writer = Files.newBufferedWriter(filePath, charset, StandardOpenOption.WRITE)) {
			for (int i = 0; i < systemInfoList.size(); i++) {
				for (int j = 0; j < systemInfoList.get(i).getSystemInfoEntryList().size(); j++) {
					writer.write(systemInfoList.get(i).getSystemInfoEntryList().get(j).getProperty() + ": " + 
								 systemInfoList.get(i).getSystemInfoEntryList().get(j).getValue());
					writer.newLine();
				}
			writer.newLine();	
			}	
			writer.close();
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		
	}
	
	
	
	public void openOrCreateSystemInfoFile() {
		File logFile = new File(filePath.toString());
		try {
			if(logFile.createNewFile()){
				Charset charset = Charset.forName("ISO-8859-1");
				try (BufferedWriter writer = Files.newBufferedWriter(filePath, charset)) {
					writer.close();
				} catch (IOException x) {
				    System.err.format("IOException: %s%n", x);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void overwriteSystemInfoFile() {
		File sysInfoFile = new File(filePath.toString());
		try {
			sysInfoFile.delete();
			sysInfoFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add system info if not already present, replace otherwise
	 */
	public void addOrReplaceSystemInfo(SystemInfo newSystemInfo) {
		boolean contains = false;
		String tempBotId, newBotId;
		newBotId = newSystemInfo.getSystemInfoEntryList().get(0).getValue();
		for (int i = 0; i < systemInfoList.size(); i++) {
			tempBotId = systemInfoList.get(i).getSystemInfoEntryList().get(0).getValue();
			if(tempBotId.equals(newBotId)){
				contains = true;
				systemInfoList.set(i, newSystemInfo);
				Main.getUiController().getBotIdTableView().getSelectionModel().clearSelection();
			}
			
		}
		if(!contains){
			systemInfoList.add(newSystemInfo);
	    	botIdEntryObservableList.add(new BotIdEntryProperty(new BotIdEntry(newBotId)));			
		}
	}

	
	public ArrayList<SystemInfoEntryProperty> getSystemInfoProperty(int selectedindex) {
		ArrayList<SystemInfoEntryProperty> returnValue = new ArrayList<SystemInfoEntryProperty>();
		SystemInfoEntryProperty tempSystemInfoEntryProperty;
		ArrayList<SystemInfoEntry> tempSystemInfoEntryList;
		SystemInfoEntry tempSystemInfoEntry;
		
		tempSystemInfoEntryList = systemInfoList.get(selectedindex).getSystemInfoEntryList();
		for (int i = 0; i < tempSystemInfoEntryList.size(); i++) {
			tempSystemInfoEntry = new SystemInfoEntry(tempSystemInfoEntryList.get(i).getProperty(), tempSystemInfoEntryList.get(i).getValue());
			tempSystemInfoEntryProperty = new SystemInfoEntryProperty(tempSystemInfoEntry);
			returnValue.add(tempSystemInfoEntryProperty);
		}
		return returnValue;
	}	
}
