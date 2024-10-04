/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDStatoEnte;
import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;

public class ModelStatiEnte {
	
	private String codStatoEnte;
	private String descStatoEnte;
	
	public ModelStatiEnte() { }
	
	public ModelStatiEnte(GregDStatoEnte stato) {
		if(stato != null) {
			this.codStatoEnte = stato.getCodStatoEnte();
			this.descStatoEnte = stato.getDescStatoEnte();
		}
	}
	
	public ModelStatiEnte(String codStato, String descStato) {
		this.codStatoEnte = codStato;
		this.descStatoEnte = descStato;
	}

	public String getCodStatoEnte() {
		return codStatoEnte;
	}

	public void setCodStatoEnte(String codStatoEnte) {
		this.codStatoEnte = codStatoEnte;
	}

	public String getDescStatoEnte() {
		return descStatoEnte;
	}

	public void setDescStatoEnte(String descStatoEnte) {
		this.descStatoEnte = descStatoEnte;
	}

	
}