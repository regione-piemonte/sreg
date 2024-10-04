/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelFConteggioPersonale {
	private List<ModelFPersonaleEnte> listaPersonaleEnte;
	private List<ModelFProfiloProfessionale> listaProfiloProfessionale;
	
	public ModelFConteggioPersonale () {}

	public List<ModelFPersonaleEnte> getListaPersonaleEnte() {
		return listaPersonaleEnte;
	}

	public void setListaPersonaleEnte(List<ModelFPersonaleEnte> listaPersonaleEnte) {
		this.listaPersonaleEnte = listaPersonaleEnte;
	}

	public List<ModelFProfiloProfessionale> getListaProfiloProfessionale() {
		return listaProfiloProfessionale;
	}

	public void setListaProfiloProfessionale(List<ModelFProfiloProfessionale> listaProfiloProfessionale) {
		this.listaProfiloProfessionale = listaProfiloProfessionale;
	}
	
	
	
}
