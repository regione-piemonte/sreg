/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregDProvince;

public class ModelComune {
	
	private Integer idComune;
	private String codIstatComune;
	private String desComune;
	private ModelProvincia provincia;
	
	public ModelComune () {}
	
	public ModelComune(GregDComuni comune) {
		this.idComune = comune.getIdComune();
		this.codIstatComune = comune.getCodIstatComune();
		this.desComune = comune.getDesComune();
		this.provincia = new ModelProvincia(comune.getGregDProvince());
	}
	
	public ModelComune(Integer idcomune,String codistatcomune,String descomune,GregDProvince provincia) {
		this.idComune = idcomune;
		this.codIstatComune = codistatcomune;
		this.desComune = descomune;
		this.provincia = new ModelProvincia(provincia);
	}
	
	public ModelComune(String codistatcomune,String descomune, String codProvincia, String desProvincia) {
		this.codIstatComune = codistatcomune;
		this.desComune = descomune;
		this.provincia = new ModelProvincia(codProvincia, desProvincia);
	}

	public Integer getIdComune() {
		return idComune;
	}

	public void setIdComune(Integer idComune) {
		this.idComune = idComune;
	}

	public String getCodIstatComune() {
		return codIstatComune;
	}

	public void setCodIstatComune(String codIstatComune) {
		this.codIstatComune = codIstatComune;
	}

	public String getDesComune() {
		return desComune;
	}

	public void setDesComune(String desComune) {
		this.desComune = desComune;
	}

	public ModelProvincia getProvincia() {
		return provincia;
	}

	public void setProvincia(ModelProvincia provincia) {
		this.provincia = provincia;
	}
}