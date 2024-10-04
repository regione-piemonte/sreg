/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelValoriVociModD {
	
	private String descrizioneVoce;
	private String operatore;
	private List<ModelCampiVoceModD> campi;
	private Integer idVoce;
	private String codVoce;
	private Integer ordinamento;
	
	public ModelValoriVociModD () {}

	public String getDescrizioneVoce() {
		return descrizioneVoce;
	}

	public void setDescrizioneVoce(String descrizioneVoce) {
		this.descrizioneVoce = descrizioneVoce;
	}

	public String getOperatore() {
		return operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public List<ModelCampiVoceModD> getCampi() {
		return campi;
	}

	public void setCampi(List<ModelCampiVoceModD> campi) {
		this.campi = campi;
	}

	public Integer getIdVoce() {
		return idVoce;
	}

	public void setIdVoce(Integer idVoce) {
		this.idVoce = idVoce;
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getCodVoce() {
		return codVoce;
	}

	public void setCodVoce(String codVoce) {
		this.codVoce = codVoce;
	}
	
}