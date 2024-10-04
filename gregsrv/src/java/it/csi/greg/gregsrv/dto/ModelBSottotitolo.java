/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

import it.csi.greg.gregsrv.business.entity.GregDSottotitolo;

public class ModelBSottotitolo {
	private Integer idSottotitolo;
	private String codSottotitolo;
	private String descSottotitolo;
	private String msgInformativo;
	private BigDecimal valore;

	public ModelBSottotitolo () {}
	
	public ModelBSottotitolo (GregDSottotitolo sottotitolo) {
		this.idSottotitolo = sottotitolo.getIdSottotitolo();
		this.codSottotitolo = sottotitolo.getCodSottotitolo();
		this.descSottotitolo = sottotitolo.getDesSottotitolo();
	}

	public Integer getIdSottotitolo() {
		return idSottotitolo;
	}

	public void setIdSottotitolo(Integer idSottotitolo) {
		this.idSottotitolo = idSottotitolo;
	}

	public String getCodSottotitolo() {
		return codSottotitolo;
	}

	public void setCodSottotitolo(String codSottotitolo) {
		this.codSottotitolo = codSottotitolo;
	}

	public String getDescSottotitolo() {
		return descSottotitolo;
	}

	public void setDescSottotitolo(String descSottotitolo) {
		this.descSottotitolo = descSottotitolo;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public String getMsgInformativo() {
		return msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	
}
