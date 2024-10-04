/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDTipoVoce;

public class ModelTipoVoce {
	
	private Integer idTipoVoce;
	private String codTipoVoce;
	private String descTipoVoce;
	private int ordinamento;
	
	public ModelTipoVoce () {}
	
	public ModelTipoVoce (GregDTipoVoce tipoVoce) {
		this.idTipoVoce = tipoVoce.getIdTipoVoce();
		this.codTipoVoce = tipoVoce.getCodTipoVoce();
		this.descTipoVoce = tipoVoce.getDescTipoVoce();
		this.ordinamento = tipoVoce.getOrdinamento();
	}

	public Integer getIdTipoVoce() {
		return idTipoVoce;
	}

	public void setIdTipoVoce(Integer idTipoVoce) {
		this.idTipoVoce = idTipoVoce;
	}

	public String getCodTipoVoce() {
		return codTipoVoce;
	}

	public void setCodTipoVoce(String codTipoVoce) {
		this.codTipoVoce = codTipoVoce;
	}

	public String getDescTipoVoce() {
		return descTipoVoce;
	}

	public void setDescTipoVoce(String descTipoVoce) {
		this.descTipoVoce = descTipoVoce;
	}

	public int getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}
	
}