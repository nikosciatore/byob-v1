package model;

import java.io.Serializable;

/**
 * Classe che rappresenta il campo period del file di configurazione
 * essendo il campo period formato da una coppia di interi (e.g. 1-2)
 */
public class Range implements Serializable{

	private static final long serialVersionUID = -3316167233071658484L;

	private Integer min;
	private Integer max;
	
	public Range() {
		min = new Integer(0);
		max = new Integer(0);
	}
	
	public Range(Integer min, Integer max) {
		this.min = min;
		this.max = max;
	}

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

	@Override
	public String toString() {
		return min.toString()+"-"+max.toString();
	}
	
	public Integer getRange(){
		return max - min;
	}
}