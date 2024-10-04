/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class ModelRisultatiModelloE {
	
	private Integer idRendicontazioneEnte;
	private Integer idSchedaEnteGestore;
	private String denominazioneEnte;
	private Integer annoGestione;
	private String codIstatComune;
	private String desComune;
	private Integer idComune;
	private Integer idRendicontazioneModE;
	private String codAttivitaSocioAssist;
	private BigDecimal valore;
	
	public ModelRisultatiModelloE () {}
	
	public ModelRisultatiModelloE (Object[] obj) {
		this.idRendicontazioneEnte = (Integer) obj[0];
		this.idSchedaEnteGestore = (Integer) obj[1];
//		this.denominazioneEnte = String.valueOf(obj[2]);
		this.annoGestione = (Integer) obj[2];
		this.codIstatComune = String.valueOf(obj[3]);
		this.desComune = String.valueOf(obj[4]);
		this.idComune = (Integer) obj[5];
		this.idRendicontazioneModE = (Integer) obj[6];
		this.codAttivitaSocioAssist = String.valueOf(obj[7]);
		this.valore = (BigDecimal) obj[8];
	}

	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}

	public Integer getIdSchedaEnteGestore() {
		return idSchedaEnteGestore;
	}

	public void setIdSchedaEnteGestore(Integer idSchedaEnteGestore) {
		this.idSchedaEnteGestore = idSchedaEnteGestore;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

	public Integer getAnnoGestione() {
		return annoGestione;
	}

	public void setAnnoGestione(Integer annoGestione) {
		this.annoGestione = annoGestione;
	}

	public String getCodIstatComune() {
		return codIstatComune;
	}

	public void setCodIstatComune(String codIstatComune) {
		this.codIstatComune = codIstatComune;
	}

	public String getDesComune() {
		return desComune;
	}

	public void setDesComune(String desComune) {
		this.desComune = desComune;
	}

	public Integer getIdComune() {
		return idComune;
	}

	public void setIdComune(Integer idComune) {
		this.idComune = idComune;
	}

	public Integer getIdRendicontazioneModE() {
		return idRendicontazioneModE;
	}

	public void setIdRendicontazioneModE(Integer idRendicontazioneModE) {
		this.idRendicontazioneModE = idRendicontazioneModE;
	}

	public String getCodAttivitaSocioAssist() {
		return codAttivitaSocioAssist;
	}

	public void setCodAttivitaSocioAssist(String codAttivitaSocioAssist) {
		this.codAttivitaSocioAssist = codAttivitaSocioAssist;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}
	
}