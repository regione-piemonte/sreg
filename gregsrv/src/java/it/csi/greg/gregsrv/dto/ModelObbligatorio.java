/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDObbligo;

public class ModelObbligatorio {
	
	private Integer idObbligo;
	private String codObbligo;
	private String desObbligo;
	
	public ModelObbligatorio () {}
	
	public ModelObbligatorio(GregDObbligo obbligatorio) { 
		this.idObbligo =  obbligatorio.getIdObbligo();
		this.codObbligo = obbligatorio.getCodObbligo();
		this.desObbligo = obbligatorio.getDesObbligo();
	}

	public String getCodObbligo() {
		return codObbligo;
	}

	public void setCodObbligo(String codObbligo) {
		this.codObbligo = codObbligo;
	}

	public String getDesObbligo() {
		return desObbligo;
	}

	public void setDesObbligo(String desObbligo) {
		this.desObbligo = desObbligo;
	}

	public Integer getIdObbligo() {
		return idObbligo;
	}

	public void setIdObbligo(Integer idObbligo) {
		this.idObbligo = idObbligo;
	}
	
}
