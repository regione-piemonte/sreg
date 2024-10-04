/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;

public class ModelRendicontazioneEnte {
	private Integer idRendicontazioneEnte;
	private Integer annoEsercizio;
	private Integer idSchedaEnte;
	private ModelStatoRendicontazione statoRendicontazione;
	private Boolean strutturaResidenziale;
	private Boolean centroDiurnoStruttSemires;
	private BigDecimal fnps;
	private BigDecimal vincoloFondo;
	private BigDecimal pippi;
	
	public ModelRendicontazioneEnte() { }
	
	public ModelRendicontazioneEnte(GregTRendicontazioneEnte rend) {		
		this.idRendicontazioneEnte = rend.getIdRendicontazioneEnte();
		this.annoEsercizio = rend.getAnnoGestione();
		this.idSchedaEnte = rend.getGregTSchedeEntiGestori().getIdSchedaEnteGestore();
		this.statoRendicontazione = new ModelStatoRendicontazione(rend.getGregDStatoRendicontazione());
		this.strutturaResidenziale = rend.getStrutturaResidenziale();
		this.centroDiurnoStruttSemires = rend.getCentroDiurnoStruttSemires();
//		this.fnps = rend.getFnps();
//		this.pippi = rend.getPippi();
//		this.vincoloFondo = rend.getVincoloFondo();
	}
	
	public ModelRendicontazioneEnte(Object[] o) {		
		this.idRendicontazioneEnte = (Integer) o[0];
		this.annoEsercizio = (Integer) o[1];
		this.statoRendicontazione = new ModelStatoRendicontazione((String) o[2], (String) o[3]);
		this.idSchedaEnte = (Integer) o[4];
	}


	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}

	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}

	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	public ModelStatoRendicontazione getStatoRendicontazione() {
		return statoRendicontazione;
	}

	public void setStatoRendicontazione(ModelStatoRendicontazione statoRendicontazione) {
		this.statoRendicontazione = statoRendicontazione;
	}

	public Boolean getStrutturaResidenziale() {
		return strutturaResidenziale;
	}

	public void setStrutturaResidenziale(Boolean strutturaResidenziale) {
		this.strutturaResidenziale = strutturaResidenziale;
	}

	public Boolean getCentroDiurnoStruttSemires() {
		return centroDiurnoStruttSemires;
	}

	public void setCentroDiurnoStruttSemires(Boolean centroDiurnoStruttSemires) {
		this.centroDiurnoStruttSemires = centroDiurnoStruttSemires;
	}

	public BigDecimal getFnps() {
		return fnps;
	}

	public void setFnps(BigDecimal fnps) {
		this.fnps = fnps;
	}

	public BigDecimal getVincoloFondo() {
		return vincoloFondo;
	}

	public void setVincoloFondo(BigDecimal vincoloFondo) {
		this.vincoloFondo = vincoloFondo;
	}

	public BigDecimal getPippi() {
		return pippi;
	}

	public void setPippi(BigDecimal pippi) {
		this.pippi = pippi;
	}

	public Integer getIdSchedaEnte() {
		return idSchedaEnte;
	}

	public void setIdSchedaEnte(Integer idSchedaEnte) {
		this.idSchedaEnte = idSchedaEnte;
	}
	
}
