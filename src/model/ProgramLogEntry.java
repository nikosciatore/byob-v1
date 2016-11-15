package model;

public class ProgramLogEntry {
	String timestamp;
	String type;
	String message;

	public ProgramLogEntry(String timestamp, String type, String message) {
		this.timestamp = timestamp;
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

}
