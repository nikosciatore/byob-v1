package byobv1.model;

public class SleepMode {
	String month;
	String day;
	String hour;
	
	public SleepMode(String month,	String day,	String hour) {
		this.month = month;
		this.day = day;
		this.hour = hour;
	}

	@Override
	public String toString() {
		return month + "-" + day + "-" + hour;
	}
}
