package byobv1.model;

public class Range {
	Integer min;
	Integer max;
	
	public Range() {
		min = new Integer(0);
		max = new Integer(0);
	}

	public Range(Integer min, Integer max) {
		this.min = min;
		this.max = max;
	}

	public Range(String string1, String string2) {
		min = Integer.parseInt(string1);
		max = Integer.parseInt(string2);
	}
	
	@Override
	public String toString() {
		return min.toString()+"-"+max.toString();
	}
}