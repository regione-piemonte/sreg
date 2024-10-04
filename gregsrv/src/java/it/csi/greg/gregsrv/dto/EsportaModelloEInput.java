/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class EsportaModelloEInput {

	private Integer idEnte;
	private List<ModelComuniAttivitaValoriModE> rendModE;
	private List<String> totaleRiga;
	private List<String> totaleColonna;
	private String denominazioneEnte;
	
	public EsportaModelloEInput() { }

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public List<ModelComuniAttivitaValoriModE> getRendModE() {
		return rendModE;
	}

	public void setRendModE(List<ModelComuniAttivitaValoriModE> rendModE) {
		this.rendModE = rendModE;
	}

	public List<String> getTotaleRiga() {
		return totaleRiga;
	}

	public void setTotaleRiga(List<String> totaleRiga) {
		this.totaleRiga = totaleRiga;
	}

	public List<String> getTotaleColonna() {
		return totaleColonna;
	}

	public void setTotaleColonna(List<String> totaleColonna) {
		this.totaleColonna = totaleColonna;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

}

