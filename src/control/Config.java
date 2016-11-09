package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import model.URLEntry;

public class Config {
	
	public ArrayList<URLEntry> readFile(Path file){
		ArrayList<URLEntry> returnValue = new ArrayList<>();
		URLEntry tempEntry;
		
		Charset charset = Charset.forName("ISO-8859-1");
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
			String line = null;
		    while ((line = reader.readLine()) != null) {
		    	if(line.length()==0 || line.charAt(0)=='#'){
		    		continue;
		    	}
		    	tempEntry = parseEntry(line, returnValue.size());
		    	returnValue.add(tempEntry);
		    }
		    reader.close();
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		
		return returnValue;
	}
	
	private URLEntry parseEntry(String line, int readedEntry) {
		URLEntry returnValue = new URLEntry();
		ArrayList<String> entries = Utility.splitInArrayList(line);
		
		returnValue.setID(readedEntry + 1);
		
		for (int i = 0; i < entries.size(); i=i+2) {			
			switch (entries.get(i)) {
			case "--url":
				returnValue.setURL(entries.get(i+1));
				break;
			case "--period":
				returnValue.setPeriod(entries.get(i+1));
				break;
			case "--maxcontact":
				returnValue.setMaxContactNumber(entries.get(i+1));
				break;
			case "--sleepmode":
				returnValue.setSleepMode(entries.get(i+1));
				break;
			case "--useragent":
				returnValue.setUserAgent(entries.get(i+1));
				break;
			case "--proxy":
				returnValue.setProxy(entries.get(i+1));
				break;
			default:
				//log: unknow field in configuration file
				break;
			}
		}
		return returnValue;
	}

	public void writeFile(Path file, ArrayList<URLEntry> configuration){
		Charset charset = Charset.forName("ISO-8859-1");
		try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
			for (int i = 0; i < configuration.size(); i++) {
				writer.write(configuration.get(i).toString());
				writer.newLine();
			}
			writer.close();
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}

	public boolean hasBeenModified(Path configFilePath, ArrayList<URLEntry> contactList) {
		
		ArrayList<URLEntry> configFile	= readFile(configFilePath);
		
		if(configFile.equals(contactList)){
			return false;
		}else{
			return true;
		}
	}	
}
