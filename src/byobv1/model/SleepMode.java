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

	public SleepMode() {
		month = null;
		day = null;
		hour = null;
	}
	
	static SleepMode integersToSleepMode(Integer month, Integer day, Integer hour){
		
		SleepMode returnValue = new SleepMode();

		StringBuilder monthString = new StringBuilder("000000000000");
		StringBuilder dayString = new StringBuilder("0000000000000000000000000000000");
		StringBuilder hourString = new StringBuilder("0000");
		monthString.setCharAt(month, '1');
		dayString.setCharAt(day, '1');
		hourString.setCharAt((int)hour/6, '1');
		
		returnValue.month = monthString.toString();
		returnValue.day = dayString.toString();
		returnValue.hour = hourString.toString();
		
		return returnValue;
	}
	
	public boolean isIncluded(SleepMode now) {
		
		for (int i = 0; i < month.length(); i++) {
			if(now.month.charAt(i)==this.month.charAt(i) && now.month.charAt(i)=='1'){
				for (int j = 0; j < day.length(); j++) {
					if(now.day.charAt(j)==this.day.charAt(j) && now.day.charAt(j)=='1'){
						for (int k = 0; k < hour.length(); k++) {
							if(now.hour.charAt(k)==this.hour.charAt(k) && now.hour.charAt(k)=='1'){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return month + "-" + day + "-" + hour;
	}
}
