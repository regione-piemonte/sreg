/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MonteOre {
	private String codProfessionale;
	private BigDecimal numeroPersonale;
	private BigDecimal numeroOre;

	//altri modelli
	public MonteOre () {}

	public String getCodProfessionale() {
		return codProfessionale;
	}

	public void setCodProfessionale(String codProfessionale) {
		this.codProfessionale = codProfessionale;
	}

	public BigDecimal getNumeroPersonale() {
		return numeroPersonale;
	}

	public void setNumeroPersonale(BigDecimal numeroPersonale) {
		this.numeroPersonale = numeroPersonale;
	}

	public BigDecimal getNumeroOre() {
		return numeroOre;
	}

	public void setNumeroOre(BigDecimal numeroOre) {
		this.numeroOre = numeroOre;
	}

}
