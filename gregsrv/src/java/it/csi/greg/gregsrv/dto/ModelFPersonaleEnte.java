/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregDPersonaleEnte;

public class ModelFPersonaleEnte {
	private String codPersonaleEnte;
	private String descPersonaleEnte;
	private List<ModelFValori> listaValori;
	
	public ModelFPersonaleEnte () {}
	
	public ModelFPersonaleEnte (GregDPersonaleEnte personaleEnte) {
		this.codPersonaleEnte = personaleEnte.getCodPersonaleEnte();
		this.descPersonaleEnte = personaleEnte.getDescPersonaleEnte();
		this.listaValori = new ArrayList<ModelFValori>();
	}

	public String getCodPersonaleEnte() {
		return codPersonaleEnte;
	}

	public void setCodPersonaleEnte(String codPersonaleEnte) {
		this.codPersonaleEnte = codPersonaleEnte;
	}

	public String getDescPersonaleEnte() {
		return descPersonaleEnte;
	}

	public void setDescPersonaleEnte(String descPersonaleEnte) {
		this.descPersonaleEnte = descPersonaleEnte;
	}
	
	public List<ModelFValori> getListaValori() {
		return listaValori;
	}

	public void setListaValori(List<ModelFValori> listaValori) {
		this.listaValori = listaValori;
	}

}
