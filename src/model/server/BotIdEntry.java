package model.server;

import java.io.Serializable;

/**
 * Classe che rappresenta l'ID del Bot
 */
public class BotIdEntry  implements Serializable{

	private static final long serialVersionUID = 1135570176964278386L;
	
	String botId;
		
	public BotIdEntry(String botId) {
		this.botId = botId;
	}

	public String getBotId() {
		return botId;
	}

	public void setProperty(String botId) {
		this.botId = botId;
	}
}
