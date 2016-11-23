package application;

import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ProgramLogEntry;
import model.ProgramLogEntryProperty;

public class ProgramLog {
	
    private static ProgramLog instance = null;
	static ObservableList<ProgramLogEntryProperty> programLogEntryObservableList;

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
	}

	public void addInfo(String message) {
		add("INFO", message);
	}

	public void addWarning(String message) {
		add("WARNING", message);
//		UserInterfaceController.setTab("control");
	}
	
	public void addError(String message) {
		add("ERROR", message);
//		UserInterfaceController.setTab("control");
	}

}
