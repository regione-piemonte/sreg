/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDVoceModD;
import it.csi.greg.gregsrv.util.Checker;

public class ModelVociD {
	
	private Integer idVoce;
	private String codVoce;
	private String descVoce;
	private String msgInformativo;
	private int ordinamento;
	private String operatoreMatematico;
	private String sezioneModello;
	
	public ModelVociD () {}
	
	public ModelVociD (GregDVoceModD voce) {
		this.idVoce = voce.getIdVoceModD();
		this.codVoce = voce.getCodVoceModD();
		this.descVoce = voce.getDescVoceModD();
		this.msgInformativo = voce.getMsgInformativo();
		this.ordinamento = voce.getOrdinamento();
		this.operatoreMatematico =  Checker.isValorizzato(voce.getOperatoreMatematico())? voce.getOperatoreMatematico() : "";
		this.sezioneModello =voce.getSezioneModello();
	}

	public Integer getIdVoce() {
		return idVoce;
	}

	public void setIdVoce(Integer idVoce) {
		this.idVoce = idVoce;
	}

	public String getCodVoce() {
		return codVoce;
	}

	public void setCodVoce(String codVoce) {
		this.codVoce = codVoce;
	}

	public String getDescVoce() {
		return descVoce;
	}

	public void setDescVoce(String descVoce) {
		this.descVoce = descVoce;
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

	public String getOperatoreMatematico() {
		return operatoreMatematico;
	}

	public void setOperatoreMatematico(String operatoreMatematico) {
		this.operatoreMatematico = operatoreMatematico;
	}

	public String getSezioneModello() {
		return sezioneModello;
	}

	public void setSezioneModello(String sezioneModello) {
		this.sezioneModello = sezioneModello;
	}
	
}