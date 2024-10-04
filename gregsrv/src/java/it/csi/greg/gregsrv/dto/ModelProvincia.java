/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDProvince;

public class ModelProvincia {
	
	private Integer idProvincia;
	private String codIstatProvincia;
	private String desProvincia;
	
	public ModelProvincia() {}
	
	public ModelProvincia(GregDProvince provincia) {
		this.idProvincia = provincia.getIdProvincia();
		this.codIstatProvincia = provincia.getCodIstatProvincia();
		this.desProvincia = provincia.getDesProvincia();
	}
	
	public ModelProvincia(String codProvincia, String desProvincia) {
		this.codIstatProvincia = codProvincia;
		this.desProvincia = desProvincia;
	}


	public Integer getIdProvincia() {
		return idProvincia;
	}

	public void setIdProvincia(Integer idProvincia) {
		this.idProvincia = idProvincia;
	}

	public String getCodIstatProvincia() {
		return codIstatProvincia;
	}

	public void setCodIstatProvincia(String codIstatProvincia) {
		this.codIstatProvincia = codIstatProvincia;
	}

	public String getDesProvincia() {
		return desProvincia;
	}

	public void setDesProvincia(String desProvincia) {
		this.desProvincia = desProvincia;
	}
}