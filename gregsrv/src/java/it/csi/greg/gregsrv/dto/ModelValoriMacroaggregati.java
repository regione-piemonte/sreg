/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelValoriMacroaggregati {
	
	private String codMissione;
	private String descrizioneMissione;
	private String descrizioneProgramma;
	private List<ModelCampiMacroaggregati> campi;
	private Integer idSpesaMissione;
	private Integer ordinamento;
	
	public ModelValoriMacroaggregati () {}

	public String getCodMissione() {
		return codMissione;
	}

	public void setCodMissione(String codMissione) {
		this.codMissione = codMissione;
	}


	public String getDescrizioneMissione() {
		return descrizioneMissione;
	}

	public void setDescrizioneMissione(String descrizioneMissione) {
		this.descrizioneMissione = descrizioneMissione;
	}

	public String getDescrizioneProgramma() {
		return descrizioneProgramma;
	}

	public void setDescrizioneProgramma(String descrizioneProgramma) {
		this.descrizioneProgramma = descrizioneProgramma;
	}

	public List<ModelCampiMacroaggregati> getCampi() {
		return campi;
	}

	public void setCampi(List<ModelCampiMacroaggregati> campi) {
		this.campi = campi;
	}

	public Integer getIdSpesaMissione() {
		return idSpesaMissione;
	}

	public void setIdSpesaMissione(Integer idSpesaMissione) {
		this.idSpesaMissione = idSpesaMissione;
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}
	
}