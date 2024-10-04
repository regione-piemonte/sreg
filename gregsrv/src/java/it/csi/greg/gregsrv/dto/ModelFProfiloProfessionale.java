/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDProfiloProfessionale;

public class ModelFProfiloProfessionale {
	private String codProfiloProfessionale;
	private String descProfiloProfessionale;

	
	public ModelFProfiloProfessionale () {}
	
	public ModelFProfiloProfessionale (GregDProfiloProfessionale profiloProfessionale) {
		this.codProfiloProfessionale = profiloProfessionale.getCodProfiloProfessionale();
		this.descProfiloProfessionale = profiloProfessionale.getDescProfiloProfessionale();
		
	}

	public String getCodProfiloProfessionale() {
		return codProfiloProfessionale;
	}

	public void setCodProfiloProfessionale(String codProfiloProfessionale) {
		this.codProfiloProfessionale = codProfiloProfessionale;
	}

	public String getDescProfiloProfessionale() {
		return descProfiloProfessionale;
	}

	public void setDescProfiloProfessionale(String descProfiloProfessionale) {
		this.descProfiloProfessionale = descProfiloProfessionale;
	}

}
