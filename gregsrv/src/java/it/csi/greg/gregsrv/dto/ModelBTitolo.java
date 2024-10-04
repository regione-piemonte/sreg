/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregDTitolo;

public class ModelBTitolo {
	private Integer idTitolo;
	private String codTitolo;
	private String descTitolo;
	private String altraDescTitolo;
	private String msgInformativo;
	private BigDecimal valore;
	private List<ModelBSottotitolo> listaSottotitolo;
	
	public ModelBTitolo () {}
	
	public ModelBTitolo (GregDTitolo titolo) {
		this.idTitolo = titolo.getIdTitolo();
		this.codTitolo = titolo.getCodTitolo();
		this.descTitolo = titolo.getDesTitolo();
		this.altraDescTitolo = titolo.getAltraDesc();
		this.listaSottotitolo = new ArrayList<ModelBSottotitolo>();
	}

	public Integer getIdTitolo() {
		return idTitolo;
	}

	public void setIdTitolo(Integer idTitolo) {
		this.idTitolo = idTitolo;
	}

	public String getCodTitolo() {
		return codTitolo;
	}

	public void setCodTitolo(String codTitolo) {
		this.codTitolo = codTitolo;
	}

	public String getDescTitolo() {
		return descTitolo;
	}

	public void setDescTitolo(String descTitolo) {
		this.descTitolo = descTitolo;
	}
	
	public List<ModelBSottotitolo> getListaSottotitolo() {
		return listaSottotitolo;
	}

	public void setListaSottotitolo(List<ModelBSottotitolo> listaSottotitolo) {
		this.listaSottotitolo = listaSottotitolo;
	}

	public String getAltraDescTitolo() {
		return altraDescTitolo;
	}

	public void setAltraDescTitolo(String altraDescTitolo) {
		this.altraDescTitolo = altraDescTitolo;
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
