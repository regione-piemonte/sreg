/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class ModelTotalePrestazioniB1 {
	private String codPrestazione;
	private String descPrestazione;
	private BigDecimal totale;

	//altri modelli
	public ModelTotalePrestazioniB1 () {}

	public String getCodPrestazione() {
		return codPrestazione;
	}

	public void setCodPrestazione(String codPrestazione) {
		this.codPrestazione = codPrestazione;
	}

	public BigDecimal getTotale() {
		return totale;
	}

	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}

	public String getDescPrestazione() {
		return descPrestazione;
	}

	public void setDescPrestazione(String descPrestazione) {
		this.descPrestazione = descPrestazione;
	}

}
