/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EsportaIstatSpese {

	private Integer annoGestione;
	private String codRegionale;
	private String denominazione;
	private String modello;
	private String codiceCatIstat;
	private String descrizioneCatIstat;
	private String codiceIstat;
	private String descrizioneIstat;
	private List<BigDecimal> utenzeMinisteriali;
	
	
	
	public EsportaIstatSpese() { }
	
	public EsportaIstatSpese(Object[] o) {
		super();
		this.annoGestione = (Integer) o[0];
		this.codRegionale = (String) o[1];
		this.denominazione =  (String) o[2];
		this.modello =  (String) o[3];
		this.codiceCatIstat =  (String) o[4];
		this.descrizioneCatIstat =  (String) o[5];
		this.codiceIstat =  (String) o[6];
		this.descrizioneIstat =  (String) o[7];
		this.utenzeMinisteriali = new ArrayList<BigDecimal>();
		for(int i=8; i<o.length; i++) {
			BigDecimal valore = (BigDecimal) o[i];
			this.utenzeMinisteriali.add(valore);
		}
	}




	public Integer getAnnoGestione() {
		return annoGestione;
	}


	public void setAnnoGestione(Integer annoGestione) {
		this.annoGestione = annoGestione;
	}


	public String getCodRegionale() {
		return codRegionale;
	}


	public void setCodRegionale(String codRegionale) {
		this.codRegionale = codRegionale;
	}


	public String getDenominazione() {
		return denominazione;
	}


	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}


	public String getModello() {
		return modello;
	}


	public void setModello(String modello) {
		this.modello = modello;
	}


	public String getCodiceCatIstat() {
		return codiceCatIstat;
	}


	public void setCodiceCatIstat(String codiceCatIstat) {
		this.codiceCatIstat = codiceCatIstat;
	}


	public String getDescrizioneCatIstat() {
		return descrizioneCatIstat;
	}


	public void setDescrizioneCatIstat(String descrizioneCatIstat) {
		this.descrizioneCatIstat = descrizioneCatIstat;
	}


	public String getCodiceIstat() {
		return codiceIstat;
	}


	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}


	public String getDescrizioneIstat() {
		return descrizioneIstat;
	}


	public void setDescrizioneIstat(String descrizioneIstat) {
		this.descrizioneIstat = descrizioneIstat;
	}


	public List<BigDecimal> getUtenzeMinisteriali() {
		return utenzeMinisteriali;
	}


	public void setUtenzeMinisteriali(List<BigDecimal> utenzeMinisteriali) {
		this.utenzeMinisteriali = utenzeMinisteriali;
	}
	
	

}

