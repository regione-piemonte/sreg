/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;


public class ModelTotaleMacroaggregati {
	
	private String descMacroaggregati;
	private String codMacroaggregati;
	private BigDecimal totale;
	private Integer ordinamento;
	
	public ModelTotaleMacroaggregati () {}

	public String getDescMacroaggregati() {
		return descMacroaggregati;
	}

	public void setDescMacroaggregati(String descMacroaggregati) {
		this.descMacroaggregati = descMacroaggregati;
	}

	public String getCodMacroaggregati() {
		return codMacroaggregati;
	}

	public void setCodMacroaggregati(String codMacroaggregati) {
		this.codMacroaggregati = codMacroaggregati;
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