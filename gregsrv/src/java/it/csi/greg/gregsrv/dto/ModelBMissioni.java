/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregDMissione;

public class ModelBMissioni {
	private Integer idMissione;
	private String codMissione;
	private String descMissione;
	private String altraDescMissione;
	private String msgInformativo;
	private List<ModelBProgramma> listaProgramma;
	
	public ModelBMissioni () {}
	
	public ModelBMissioni (GregDMissione missione) {
		this.idMissione = missione.getIdMissione();
		this.codMissione = missione.getCodMissione();
		this.descMissione = missione.getDesMissione();
		this.altraDescMissione=missione.getAltraDesc();
		this.msgInformativo=missione.getMsgInformativo();
		this.listaProgramma = new ArrayList<ModelBProgramma>();
	}

	public Integer getIdMissione() {
		return idMissione;
	}

	public void setIdMissione(Integer idMissione) {
		this.idMissione = idMissione;
	}

	public String getCodMissione() {
		return codMissione;
	}

	public void setCodMissione(String codMissione) {
		this.codMissione = codMissione;
	}

	public String getDescMissione() {
		return descMissione;
	}

	public void setDescMissione(String descMissione) {
		this.descMissione = descMissione;
	}
	
	public List<ModelBProgramma> getListaProgramma() {
		return listaProgramma;
	}

	public void setListaProgramma(List<ModelBProgramma> listaProgramma) {
		this.listaProgramma = listaProgramma;
	}

	public String getAltraDescMissione() {
		return altraDescMissione;
	}

	public void setAltraDescMissione(String altraDescMissione) {
		this.altraDescMissione = altraDescMissione;
	}

	public String getMsgInformativo() {
		return msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}
	
	
}
