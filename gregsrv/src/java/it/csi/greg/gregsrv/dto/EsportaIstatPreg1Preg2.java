/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EsportaIstatPreg1Preg2 {

	private Integer annoGestione;
	private String codRegionale;
	private String denominazione;
	private String codPreg1;
	private String desPreg1;
	private String codPreg2;
	private String desPreg2;
	private List<BigDecimal> utenzeRegionali;
	private String codiceCatIstat;
	private String descrizioneCatIstat;
	private String codiceIstat;
	private String descrizioneIstat;
	private List<BigDecimal> utenzeMinisteriali;
	
	
	
	public EsportaIstatPreg1Preg2() { }
	
	public EsportaIstatPreg1Preg2(Object[] o, int lunghezza) {
		this.annoGestione = (Integer) o[0];
		this.codRegionale = (String) o[1];
		this.denominazione =  (String) o[2];
		this.codPreg1 = (String) o[3];
		this.desPreg1 = (String) o[4];
		this.codPreg2 = (String) o[5];
		this.desPreg2 = (String) o[6];
		this.utenzeRegionali = new ArrayList<BigDecimal>();
		for(int i=7; i<(7+lunghezza); i++) {
			BigDecimal valore = (BigDecimal) o[i];
			this.utenzeRegionali.add(valore);
		}
		this.codiceCatIstat =  (String) o[7+lunghezza];
		this.descrizioneCatIstat =  (String) o[8+lunghezza];
		this.codiceIstat =  (String) o[9+lunghezza];
		this.descrizioneIstat =  (String) o[10+lunghezza];
		this.utenzeMinisteriali = new ArrayList<BigDecimal>();
		for(int i=11+lunghezza; i<o.length; i++) {
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

	public String getCodPreg1() {
		return codPreg1;
	}

	public void setCodPreg1(String codPreg1) {
		this.codPreg1 = codPreg1;
	}

	public String getDesPreg1() {
		return desPreg1;
	}

	public void setDesPreg1(String desPreg1) {
		this.desPreg1 = desPreg1;
	}

	public String getCodPreg2() {
		return codPreg2;
	}

	public void setCodPreg2(String codPreg2) {
		this.codPreg2 = codPreg2;
	}

	public String getDesPreg2() {
		return desPreg2;
	}

	public void setDesPreg2(String desPreg2) {
		this.desPreg2 = desPreg2;
	}

	public List<BigDecimal> getUtenzeRegionali() {
		return utenzeRegionali;
	}

	public void setUtenzeRegionali(List<BigDecimal> utenzeRegionali) {
		this.utenzeRegionali = utenzeRegionali;
	}
	
}

