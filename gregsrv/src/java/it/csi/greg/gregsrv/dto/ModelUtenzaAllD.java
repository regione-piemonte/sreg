/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.List;

public class ModelUtenzaAllD {
	private String aree;
	private Integer idUtenza;
	private String codUtenza;
	private String descUtenza;
	private BigDecimal valore;
	private BigDecimal valoreB1;
	private BigDecimal valoreFNPS;
	private BigDecimal valorePercentuale;
	private boolean utilizzatoPerCalcolo;
	
	public ModelUtenzaAllD () {}

	public String getAree() {
		return aree;
	}


	public void setAree(String aree) {
		this.aree = aree;
	}


	public Integer getIdUtenza() {
		return idUtenza;
	}


	public void setIdUtenza(Integer idUtenza) {
		this.idUtenza = idUtenza;
	}


	public String getCodUtenza() {
		return codUtenza;
	}


	public void setCodUtenza(String codUtenza) {
		this.codUtenza = codUtenza;
	}


	public String getDescUtenza() {
		return descUtenza;
	}


	public void setDescUtenza(String descUtenza) {
		this.descUtenza = descUtenza;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public BigDecimal getValoreB1() {
		return valoreB1;
	}

	public void setValoreB1(BigDecimal valoreB1) {
		this.valoreB1 = valoreB1;
	}

	public BigDecimal getValoreFNPS() {
		return valoreFNPS;
	}

	public void setValoreFNPS(BigDecimal valoreFNPS) {
		this.valoreFNPS = valoreFNPS;
	}

	public BigDecimal getValorePercentuale() {
		return valorePercentuale;
	}

	public void setValorePercentuale(BigDecimal valorePercentuale) {
		this.valorePercentuale = valorePercentuale;
	}

	public boolean isUtilizzatoPerCalcolo() {
		return utilizzatoPerCalcolo;
	}

	public void setUtilizzatoPerCalcolo(boolean utilizzatoPerCalcolo) {
		this.utilizzatoPerCalcolo = utilizzatoPerCalcolo;
	}
	
}
