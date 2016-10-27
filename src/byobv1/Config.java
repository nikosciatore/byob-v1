package byobv1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import byobv1.model.Range;
import byobv1.model.SleepMode;
import byobv1.model.URLEntry;

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
		URL url;
		Range period;
		Integer maxContact;
		SleepMode sleepMode;
		String userAgent;
		URL proxy;
		
		returnValue.setID(readedEntry + 1);
		
		for (int i = 0; i < entries.size(); i=i+2) {
			switch (entries.get(i)) {
			case "--url":
				url = parseUrl(entries.get(i+1));
				returnValue.setURL(url);
				break;
			case "--period":
				period = parsePeriod(entries.get(i+1));
				returnValue.setPeriodicRangeSec(period);
				break;
			case "--maxcontact":
				maxContact = Integer.parseInt(entries.get(i+1));
				returnValue.setMaxContactNumber(maxContact);
				break;
			case "--sleepmode":
				sleepMode = parseSleepMode(entries.get(i+1));
				returnValue.setSleepMode(sleepMode);
				break;
			case "--useragent":
				userAgent = entries.get(i+1);
				returnValue.setUserAgent(userAgent);
				break;
			case "--proxy":
				proxy = parseUrl(entries.get(i+1));
				returnValue.setProxy(proxy);
				break;
			default:
				//log: unknow field in configuration file
				break;
			}
		}
		return returnValue;
	}

	private SleepMode parseSleepMode(String string) {
		SleepMode returnValue;
		String [] strings;
		strings = string.split("-");
		returnValue = new SleepMode(strings[0], strings[1], strings[2]);
		return returnValue;
	}

	private Range parsePeriod(String string) {
		Range returnValue;
		String [] strings;
		Integer min,max;
		strings = string.split("-");
		min = Integer.parseInt(strings[0]);
		max = Integer.parseInt(strings[1]);			
		returnValue = new Range(min, max);
		return returnValue;
	}

	private URL parseUrl(String urlString) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
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
