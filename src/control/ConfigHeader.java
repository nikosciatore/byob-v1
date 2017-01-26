package control;

import java.io.Serializable;

/**
 * Classe che rappresenta l'header del file di configurazione contenente la lista dei contatti
 * ttl: il campo ttl specifica il tempo di validità del file di configurazione
 */
public class ConfigHeader implements Serializable{
	
	private static final long serialVersionUID = -4355442191360068798L;

	private Integer ttl; 

	public Integer getTtl() {
		return ttl;
	}

	public void setTtl(Integer ttl) {
		this.ttl = ttl;
	}
	
	public void setTtl(String ttlString) {
		try {
			this.ttl = Integer.parseInt(ttlString);
		} catch (NumberFormatException e) {
			this.ttl = new Integer(1);
		}
	}

	/**
	 * @return true se il campo ttl è stato decrementato false altrimenti
	 * */
	public boolean decreaseTtl() {
		if(ttl.intValue() > 0){
			ttl--;
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		String ttl;
		String ttlPair;
		ttl = this.ttl.toString();
		ttlPair = (this.ttl.toString().equals("")) ? "" : "--ttl " + ttl;
		return ttlPair;
	}
}
