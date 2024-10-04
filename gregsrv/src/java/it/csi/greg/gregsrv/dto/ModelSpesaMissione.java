/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDSpesaMissioneProgramma;

public class ModelSpesaMissione {
	private Integer idSpesaMissione;
	private String codSpesaMissione;
	private String descMissioneCartacea;
	private String descProgrammaCartaceo;
	private String msgInformativo;
	private int ordinamento;
	
	public ModelSpesaMissione () {}
	
	public ModelSpesaMissione (GregDSpesaMissioneProgramma spesaMissione) {
		this.idSpesaMissione = spesaMissione.getIdSpesaMissioneProgramma();
		this.codSpesaMissione = spesaMissione.getCodSpesaMissioneProgramma();
		this.descMissioneCartacea = spesaMissione.getDescMissioneCartacea();
		this.descProgrammaCartaceo=spesaMissione.getDescProgrammaCartaceo();
		this.msgInformativo=spesaMissione.getMsgInformativo();
		this.ordinamento = spesaMissione.getOrdinamento();
	}
	
	

	public Integer getIdSpesaMissione() {
		return idSpesaMissione;
	}

	public void setIdSpesaMissione(Integer idSpeseMissioni) {
		this.idSpesaMissione = idSpeseMissioni;
	}

	public String getCodSpesaMissione() {
		return codSpesaMissione;
	}

	public void setCodSpesaMissione(String codSpeseMissioni) {
		this.codSpesaMissione = codSpeseMissioni;
	}

	public String getDescMissioneCartacea() {
		return descMissioneCartacea;
	}

	public void setDescMissioneCartacea(String descMissioneCartacea) {
		this.descMissioneCartacea = descMissioneCartacea;
	}

	public String getDescProgrammaCartaceo() {
		return descProgrammaCartaceo;
	}

	public void setDescProgrammaCartaceo(String descProgrammaCartaceo) {
		this.descProgrammaCartaceo = descProgrammaCartaceo;
	}

	public String getMsgInformativo() {
		return msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public int getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}
	
}
