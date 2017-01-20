package control.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import model.LogEntry;

/**
 * Classe per la gestione del log relativo ai contatti effettuati dal Bot
 */
public class Log {
	
	static Path filePath;
	
	public Log(Path filePath) {
		Log.filePath = filePath;
	}

	public Log() {
		
	}

	String header = "# legend:\n"
			+ "# timestamp\n"
			+ "# url\n"
			+ "# period_range(s)  max_contact_number  sleep_mode\n"
			+ "# user_agent proxy\n"
			+ "\n";
	
	/**
	 * Apertura o creazione del file di log
	 */
	public void openOrCreateLogFile(){
		File logFile = new File(filePath.toString());
		try {
			if(logFile.createNewFile()){
				Charset charset = Charset.forName("ISO-8859-1");
				try (BufferedWriter writer = Files.newBufferedWriter(filePath, charset)) {
					writer.write(header);
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
	 * Scrittura di una riga del file di log
	 */
	public void writeLogFile(LogEntry logEntry, Integer contactNumber){
		Charset charset = Charset.forName("ISO-8859-1");
		try (BufferedWriter writer = Files.newBufferedWriter(filePath, charset, StandardOpenOption.APPEND)) {
				writer.write(logEntry.toString());
				writer.newLine();
				writer.newLine();
			writer.close();
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
}
