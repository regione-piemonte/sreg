/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregCMessaggioApplicativo;

public class ModelMsgApplicativo {
	
	private Integer idMsgApplicativo;
	private String codMsgApplicativo;
	private String tipoMsgApplicativo;
	private String testoMsgApplicativo;
	
	public ModelMsgApplicativo() {}
	
	public ModelMsgApplicativo(GregCMessaggioApplicativo messaggio) {
		this.idMsgApplicativo = messaggio.getPkIdMessaggioApplicativo();
		this.codMsgApplicativo = messaggio.getCodMessaggio();
		this.tipoMsgApplicativo = messaggio.getTipoMessaggio();
		this.testoMsgApplicativo = messaggio.getTestoMessaggio();
	}

	public Integer getIdMsgApplicativo() {
		return idMsgApplicativo;
	}

	public void setIdMsgApplicativo(Integer idMsgApplicativo) {
		this.idMsgApplicativo = idMsgApplicativo;
	}

	public String getCodMsgApplicativo() {
		return codMsgApplicativo;
	}

	public void setCodMsgApplicativo(String codMsgApplicativo) {
		this.codMsgApplicativo = codMsgApplicativo;
	}

	public String getTipoMsgApplicativo() {
		return tipoMsgApplicativo;
	}

	public void setTipoMsgApplicativo(String tipoMsgApplicativo) {
		this.tipoMsgApplicativo = tipoMsgApplicativo;
	}

	public String getTestoMsgApplicativo() {
		return testoMsgApplicativo;
	}

	public void setTestoMsgApplicativo(String testoMsgApplicativo) {
		this.testoMsgApplicativo = testoMsgApplicativo;
	}
}