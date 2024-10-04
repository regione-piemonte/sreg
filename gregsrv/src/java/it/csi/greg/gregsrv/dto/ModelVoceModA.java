/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

import it.csi.greg.gregsrv.business.entity.GregDVoceModA;

public class ModelVoceModA {
	
	private int idVoce;
	private String codVoce;
	private String descVoce;
	private int ordinamento;
	private String msgInformativo;
	private String sezioneModello;
	private String valoreText;
	private BigDecimal valoreNumb;
	private String codTipologiaQuota;
	private ModelPrestazioniModA prestazioni;
	
	public ModelVoceModA () {}

	
	public int getIdVoce() {
		return idVoce;
	}

	public void setIdVoce(int idVoce) {
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

	public String getSezioneModello() {
		return sezioneModello;
	}

	public void setSezioneModello(String sezioneModello) {
		this.sezioneModello = sezioneModello;
	}

	public String getValoreText() {
		return valoreText;
	}

	public void setValoreText(String valoreText) {
		this.valoreText = valoreText;
	}

	public BigDecimal getValoreNumb() {
		return valoreNumb;
	}

	public void setValoreNumb(BigDecimal valoreNumb) {
		this.valoreNumb = valoreNumb;
	}

	public String getCodTipologiaQuota() {
		return codTipologiaQuota;
	}

	public void setCodTipologiaQuota(String codTipologiaQuota) {
		this.codTipologiaQuota = codTipologiaQuota;
	}

	public ModelPrestazioniModA getPrestazioni() {
		return prestazioni;
	}

	public void setPrestazioni(ModelPrestazioniModA prestazioni) {
		this.prestazioni = prestazioni;
	}


	
	
}