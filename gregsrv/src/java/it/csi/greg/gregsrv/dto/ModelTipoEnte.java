/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDTipoEnte;

public class ModelTipoEnte {
	
	private Integer idTipoEnte;
	private String codTipoEnte;
	private String desTipoEnte;
	
	public ModelTipoEnte() {}
	
	public ModelTipoEnte(Integer idTipoEnte, String codTipoEnte, String DesTipoEnte) {
		this.idTipoEnte = idTipoEnte;
		this.codTipoEnte = codTipoEnte;
		this.desTipoEnte  = DesTipoEnte;
	}
	
	public ModelTipoEnte(String codTipoEnte, String DesTipoEnte) {
		this.codTipoEnte = codTipoEnte;
		this.desTipoEnte  = DesTipoEnte;
	}
	
	public ModelTipoEnte(GregDTipoEnte tipoEnte) {
		this.idTipoEnte = tipoEnte.getIdTipoEnte();
		this.codTipoEnte = tipoEnte.getCodTipoEnte();
		this.desTipoEnte  = tipoEnte.getDesTipoEnte();
	}


	public Integer getIdTipoEnte() {
		return idTipoEnte;
	}

	public void setIdTipoEnte(Integer idTipoEnte) {
		this.idTipoEnte = idTipoEnte;
	}

	public String getCodTipoEnte() {
		return codTipoEnte;
	}

	public void setCodTipoEnte(String codTipoEnte) {
		this.codTipoEnte = codTipoEnte;
	}

	public String getDesTipoEnte() {
		return desTipoEnte;
	}

	public void setDesTipoEnte(String desTipoEnte) {
		this.desTipoEnte = desTipoEnte;
	}

}