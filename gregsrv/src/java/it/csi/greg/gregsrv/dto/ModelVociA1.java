/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDVoceModA1;

public class ModelVociA1 {
	
	private String codvoce;
	private String descVoceModA1;
	private int ordinamento;
	private String msgInformativo;
	
	public ModelVociA1 () {}
	
	public ModelVociA1 (GregDVoceModA1 voci) {
		this.codvoce = voci.getCodVoceModA1();
		this.descVoceModA1 = voci.getDescVoceModA1();
		this.ordinamento = voci.getOrdinamento();
		this.msgInformativo = voci.getMsgInformativo();
	}
	
	public String getCodvoce() {
		return codvoce;
	}

	public String getDescVoceModA1() {
		return descVoceModA1;
	}

	public void setDescVoceModA1(String descVoceModA1) {
		this.descVoceModA1 = descVoceModA1;
	}

	public int getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getMsgInformativo() {
		return msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public void setCodvoce(String codvoce) {
		this.codvoce = codvoce;
	}

	
	
}