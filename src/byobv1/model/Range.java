package byobv1.model;

public class Range {
	private Integer min;
	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	private Integer max;
	
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
	
	public Integer getRange(){
		return max - min;
	}
}