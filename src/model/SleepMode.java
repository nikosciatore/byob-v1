package model;

import java.io.Serializable;

public class SleepMode implements Serializable{

	private static final long serialVersionUID = -3825436293961506586L;
	
	String month;
	String day;
	String hour;
		
	public SleepMode(String month,	String day,	String hour) {
		
		StringBuilder monthString = new StringBuilder("000000000000");
		StringBuilder dayString = new StringBuilder("0000000000000000000000000000000");
		StringBuilder hourString = new StringBuilder("000000000000000000000000");
		
		int groupNumber;
		
		for (int i = 0; i < monthString.length(); i++) {
			groupNumber = getGroupNumber(i, monthString, month);
			if(month.charAt(groupNumber) == '1'){
				monthString.setCharAt(i, '1');
			}
		}

		for (int i = 0; i < dayString.length(); i++) {
			groupNumber = getGroupNumber(i, dayString, day);
			if(day.charAt(groupNumber) == '1'){
				dayString.setCharAt(i, '1');
			}
		}

		for (int i = 0; i < hourString.length(); i++) {
			groupNumber = getGroupNumber(i, hourString, hour);
			if(hour.charAt(groupNumber) == '1'){
				hourString.setCharAt(i, '1');
			}
		}
		
		this.month = monthString.toString();
		this.day = dayString.toString();
		this.hour = hourString.toString();
	}

	private int getGroupNumber(int index, StringBuilder stringBuilder, String string) {
		int returnValue = (int) index/(stringBuilder.length()/string.length());
		return Math.min(returnValue, string.length() - 1);
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
		StringBuilder hourString = new StringBuilder("000000000000000000000000");
		monthString.setCharAt(month - 1, '1');
		dayString.setCharAt(day - 1, '1');
		hourString.setCharAt(hour - 1, '1');
		
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
