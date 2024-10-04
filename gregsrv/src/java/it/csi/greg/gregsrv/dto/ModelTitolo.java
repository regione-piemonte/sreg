/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDTitolo;

public class ModelTitolo {
	
	private String codTitolo;
	private String descTitolo;
	
	public ModelTitolo() {}
	
	public ModelTitolo(GregDTitolo titolo) {
		this.codTitolo = titolo != null ? titolo.getCodTitolo() : null;
		this.descTitolo = titolo != null ? titolo.getDesTitolo() : null;
	}
	
	public String getCodTitolo() {
		return codTitolo;
	}
	public void setCodTitolo(String codTitolo) {
		this.codTitolo = codTitolo;
	}
	public String getDescTitolo() {
		return descTitolo;
	}
	public void setDescTitolo(String descTitolo) {
		this.descTitolo = descTitolo;
	}
	
	
}
