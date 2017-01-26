package model.gui;

import javafx.beans.property.SimpleStringProperty;
import model.server.BotIdEntry;

/**
 * Classe necessaria per riempire le righe della TabelView 
 * presente nell'interfaccia grafica
 */
public class BotIdEntryProperty {

	private final SimpleStringProperty botId;

	public BotIdEntryProperty(BotIdEntry botIdEntry) {
		this.botId = new SimpleStringProperty(botIdEntry.getBotId());
	}
	
	public String getBotId() {
		return botId.get();
	}
	
	public void setBotId(String botId) {
		this.botId.set(botId);
	}
}
