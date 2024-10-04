/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

import it.csi.greg.gregsrv.business.entity.GregDProfiloProfessionale;

public class ModelFValori {
	private String codProfiloProfessionale;
	private BigDecimal valore;
	
	public ModelFValori () {}
	
	public ModelFValori (GregDProfiloProfessionale profiloProfessionale) {
		this.codProfiloProfessionale = profiloProfessionale.getCodProfiloProfessionale();
	}

	public String getCodProfiloProfessionale() {
		return codProfiloProfessionale;
	}

	public void setCodProfiloProfessionale(String codProfiloProfessionale) {
		this.codProfiloProfessionale = codProfiloProfessionale;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}	
	
}
