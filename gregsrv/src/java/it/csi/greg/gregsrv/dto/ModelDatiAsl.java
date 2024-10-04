/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDAsl;

public class ModelDatiAsl {
	
	private Integer idAsl;
	private String codAsl;
	private String desAsl;
	
	public ModelDatiAsl () {}
	
	public ModelDatiAsl(GregDAsl asl) {
		this.idAsl = asl.getIdAsl();
		this.codAsl = asl.getCodAsl();
		this.desAsl = asl.getDesAsl();
	}
	
	public ModelDatiAsl(String codAsl, String desAsl) {
		this.codAsl = codAsl;
		this.desAsl = desAsl;
	}

	public Integer getIdAsl() {
		return this.idAsl;
	}
	
	public void setIdAsl(Integer idAsl) {
		this.idAsl = idAsl;
	}
	
	public String getCodAsl() {
		return this.codAsl;
	}
	
	public void setCodAsl(String codAsl) {
		this.codAsl = codAsl;
	}
	
	public String getDesAsl() {
		return this.desAsl;
	}
	
	public void setDesAsl(String desAsl) {
		this.desAsl = desAsl;
	}
	
}