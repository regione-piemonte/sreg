/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class EsportaModelloCInput {

	private Integer idEnte;
	private ModelRendicontazioneModC datiC;
	private List<ModelCPrestazioni> prestazioniC;
	private String totaleMinori;
	private String totaleAdulti;
	private List<String> totaleUtenti;
	private String denominazioneEnte;
	
	public EsportaModelloCInput() { }

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public ModelRendicontazioneModC getDatiC() {
		return datiC;
	}

	public void setDatiC(ModelRendicontazioneModC datiC) {
		this.datiC = datiC;
	}

	public List<ModelCPrestazioni> getPrestazioniC() {
		return prestazioniC;
	}

	public void setPrestazioniC(List<ModelCPrestazioni> prestazioniC) {
		this.prestazioniC = prestazioniC;
	}

	public String getTotaleMinori() {
		return totaleMinori;
	}

	public void setTotaleMinori(String totaleMinori) {
		this.totaleMinori = totaleMinori;
	}

	public String getTotaleAdulti() {
		return totaleAdulti;
	}

	public void setTotaleAdulti(String totaleAdulti) {
		this.totaleAdulti = totaleAdulti;
	}

	public List<String> getTotaleUtenti() {
		return totaleUtenti;
	}

	public void setTotaleUtenti(List<String> totaleUtenti) {
		this.totaleUtenti = totaleUtenti;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

	
	
}

