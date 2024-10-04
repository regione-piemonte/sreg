/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class PrestazioniAlZeroPerFnpsDTO {
	private String codPrestMinisteriale;
	private BigDecimal sommaValore;
	
	public String getCodPrestMinisteriale() {
		return codPrestMinisteriale;
	}
	public void setCodPrestMinisteriale(String codPrestMinisteriale) {
		this.codPrestMinisteriale = codPrestMinisteriale;
	}
	public BigDecimal getSommaValore() {
		return sommaValore;
	}
	public void setSommaValore(BigDecimal sommaValore) {
		this.sommaValore = sommaValore;
	}
	
	@Override
	public String toString() {
		return "PrestazioniAlZeroPerFnpsDTO [codPrestMinisteriale=" + codPrestMinisteriale + ", sommaValore="
				+ sommaValore + "]";
	}	
}
