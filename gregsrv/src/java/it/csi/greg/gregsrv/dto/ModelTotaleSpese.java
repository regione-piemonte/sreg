/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;


public class ModelTotaleSpese {
	
	private String descMissione;
	private String codMissione;
	private BigDecimal totale;
	private Integer ordinamento;
	
	public ModelTotaleSpese () {}

	public String getDescMissione() {
		return descMissione;
	}

	public void setDescMissione(String descMissione) {
		this.descMissione = descMissione;
	}

	public String getCodMissione() {
		return codMissione;
	}

	public void setCodMissione(String codMissione) {
		this.codMissione = codMissione;
	}

	public BigDecimal getTotale() {
		return totale;
	}

	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	
}