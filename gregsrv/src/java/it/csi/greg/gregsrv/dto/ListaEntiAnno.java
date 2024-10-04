/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ListaEntiAnno {
	private List<ModelRicercaEntiGestoriAttivi> enti;
	private Integer anno;
	private ModelProfilo profilo;
	
	//altri modelli
	public ListaEntiAnno () {}

	public List<ModelRicercaEntiGestoriAttivi> getEnti() {
		return enti;
	}

	public void setEnti(List<ModelRicercaEntiGestoriAttivi> enti) {
		this.enti = enti;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public ModelProfilo getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}
	
	
}
