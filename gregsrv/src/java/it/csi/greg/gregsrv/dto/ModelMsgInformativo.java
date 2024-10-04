/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDMsgInformativo;

public class ModelMsgInformativo {
	
	private Integer idMsgInformativo;
	private String codMsgInformativo;
	private String descMsgInformativo;
	private String testoMsgInformativo;
	
	public ModelMsgInformativo() {}
	
	public ModelMsgInformativo(GregDMsgInformativo messaggio) {
		this.idMsgInformativo = messaggio.getIdMsgInformativo();
		this.codMsgInformativo = messaggio.getCodMsgInformativo();
		this.descMsgInformativo = messaggio.getDescMsgInformativo();
		this.testoMsgInformativo = messaggio.getTestoMsgInformativo();
	}

	public Integer getIdMsgInformativo() {
		return idMsgInformativo;
	}

	public void setIdMsgInformativo(Integer idMsgInformativo) {
		this.idMsgInformativo = idMsgInformativo;
	}

	public String getCodMsgInformativo() {
		return codMsgInformativo;
	}

	public void setCodMsgInformativo(String codMsgInformativo) {
		this.codMsgInformativo = codMsgInformativo;
	}

	public String getDescMsgInformativo() {
		return descMsgInformativo;
	}

	public void setDescMsgInformativo(String descMsgInformativo) {
		this.descMsgInformativo = descMsgInformativo;
	}

	public String getTestoMsgInformativo() {
		return testoMsgInformativo;
	}

	public void setTestoMsgInformativo(String testoMsgInformativo) {
		this.testoMsgInformativo = testoMsgInformativo;
	}
}