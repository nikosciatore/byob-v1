package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import model.URLEntry;

/**
 * Classe per la gestione del file di configurazione 
 * contenente la lista delle URL da contattare
 */
public class Config{

	static Path filePath;
	ConfigHeader configHeader;
	ArrayList<URLEntry> contactsList;

	public Config(Path filePath) {
		Config.filePath = filePath;
		configHeader = new ConfigHeader();
		contactsList = new ArrayList<URLEntry>();

	}
	
	public ConfigHeader getConfigHeader() {
		return configHeader;
	}

	public void setConfigHeader(ConfigHeader configHeader) {
		this.configHeader = configHeader;
	}

	public ArrayList<URLEntry> getContactsList() {
		return contactsList;
	}

	public void setContactsList(ArrayList<URLEntry> contactsList) {
		this.contactsList = contactsList;
	}
	
	/**
	 * Apre, se esiste, oppure crea il file di configurazione 
	 * contenente la lista delle URL da contattare
	 */
	public void openOrCreateConfigFile(){
		File configFile = new File(filePath.toString());
		try {
			if(configFile.createNewFile()){
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
	
	/**
	 * Lettura del file di configurazione contenente la lista delle URL da contattare
	 */
	public void readFile(){
		URLEntry tempEntry;
		
		Charset charset = Charset.forName("ISO-8859-1");
		try (BufferedReader reader = Files.newBufferedReader(filePath, charset)) {
			String line = null;
		    while ((line = reader.readLine()) != null) {
		    	if(line.length()==0 || line.charAt(0)=='#'){
		    		continue;
		    	}
		    	tempEntry = parseEntry(line, contactsList.size());
		    	if(tempEntry!=null){
		    		contactsList.add(tempEntry);
		    	}
		    }
		    reader.close();
		} catch (NoSuchFileException e) {
			contactsList = null;
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	/**
	 * Effettua il parsing di una riga del file di configurazione
	 * @param line stringa contenente una singola riga del file di configurazione
	 * @param readedEntry numero delle righe lette
	 * @return Oggetto di tipo URLEntry
	 */
	private URLEntry parseEntry(String line, int readedEntry) {
		URLEntry returnValue = new URLEntry();
		ArrayList<String> entries = Utility.splitInArrayList(line," ");
		
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
			case "--ttl":
				configHeader.setTtl(entries.get(i+1));
				returnValue = null;
				break;
			default:
				//log: unknow field in configuration file
				break;
			}
		}
		return returnValue;
	}

	/**
	 * Metodo che consente di scrivere il file di configurazione 
	 * contenente la lista delle URL da contattare
	 */
	public void writeFile(){
		Charset charset = Charset.forName("ISO-8859-1");
		try (BufferedWriter writer = Files.newBufferedWriter(filePath, charset)) {
			writer.write(configHeader.toString());
			writer.newLine();
			for (int i = 0; i < contactsList.size(); i++) {
				writer.write(contactsList.get(i).toString());
				writer.newLine();
			}
			writer.close();
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}	
}
