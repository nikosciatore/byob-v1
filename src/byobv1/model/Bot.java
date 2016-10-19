package byobv1.model;

import java.util.ArrayList;

public class Bot {
	Integer id;
	BotStatus status;
	ArrayList<URLEntry> contactsList;
	
	public Bot() {
		id = generateID();
		status = BotStatus.IDLE;
		contactsList = new ArrayList<URLEntry>();
	}
	
	private Integer generateID() {
		Double returnValue = Math.random()*1000000000;
		return  returnValue.intValue();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BotStatus getStatus() {
		return status;
	}
	public void setStatus(BotStatus status) {
		this.status = status;
	}
	public ArrayList<URLEntry> getContactsList() {
		return contactsList;
	}
	public void setContactsList(ArrayList<URLEntry> contactsList) {
		this.contactsList = contactsList;
	}
}
