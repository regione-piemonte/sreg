/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class ModelB1ProgrammiMissioneTotali {
	private String codice;
	private BigDecimal valore;
	
	public ModelB1ProgrammiMissioneTotali() {}
	
	public ModelB1ProgrammiMissioneTotali(String codice, BigDecimal valore) {
		this.codice=codice;
		this.valore=valore;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}
	
}
