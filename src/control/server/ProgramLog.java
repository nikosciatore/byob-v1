package control.server;

import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.gui.ProgramLogEntryProperty;
import model.server.ProgramLogEntry;

/**
 * Classe per la generazione del log di programma
 */
public class ProgramLog {
	
    private static ProgramLog instance = null;
	private static ObservableList<ProgramLogEntryProperty> programLogEntryObservableList;

    public static synchronized ProgramLog getProgramLog() {
        if (instance == null) {
            instance = new ProgramLog();
        }
        return instance;
    }
	

	private ProgramLog() {
		programLogEntryObservableList = FXCollections.observableArrayList();
	}

	public static ObservableList<ProgramLogEntryProperty> getProgramLogEntryObservableList() {
		return programLogEntryObservableList;
	}

	public void add(String type, String message) {
		Date date = new Date();
		programLogEntryObservableList.add(new ProgramLogEntryProperty(new ProgramLogEntry(date.toString(),type,message)));
		
		try {
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void addInfo(String message) {
		add("INFO", message);
	}

	public void addWarning(String message) {
		add("WARNING", message);
	}
	
	public void addError(String message) {
		add("ERROR", message);
	}

}
