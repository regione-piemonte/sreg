/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class FondiEnteAllontanamentoZero {
	
	private BigDecimal fondoRegionale;
	private BigDecimal quotaAllontanamentoZero;
	private String labelFondiRegionale;
	private String labelQuotaAllontanamentoZero;	
	
	public FondiEnteAllontanamentoZero(BigDecimal fondoRegionale, BigDecimal quotaAllontanamentoZero) {
		this.fondoRegionale = fondoRegionale;
		this.quotaAllontanamentoZero = quotaAllontanamentoZero;
	}
	
	public FondiEnteAllontanamentoZero(Object[] o) {
		this.fondoRegionale = (BigDecimal) o[0];
		this.labelFondiRegionale = (String) o[1];
		this.quotaAllontanamentoZero = (BigDecimal) o[2];
		this.labelQuotaAllontanamentoZero = (String) o[3];
	}

	public BigDecimal getFondoRegionale() {
		return fondoRegionale;
	}

	public void setFondoRegionale(BigDecimal fondoRegionale) {
		this.fondoRegionale = fondoRegionale;
	}

	public BigDecimal getQuotaAllontanamentoZero() {
		return quotaAllontanamentoZero;
	}

	public void setQuotaAllontanamentoZero(BigDecimal quotaAllontanamentoZero) {
		this.quotaAllontanamentoZero = quotaAllontanamentoZero;
	}

	public String getLabelFondiRegionale() {
		return labelFondiRegionale;
	}

	public void setLabelFondiRegionale(String labelFondiRegionale) {
		this.labelFondiRegionale = labelFondiRegionale;
	}

	public String getLabelQuotaAllontanamentoZero() {
		return labelQuotaAllontanamentoZero;
	}

	public void setLabelQuotaAllontanamentoZero(String labelQuotaAllontanamentoZero) {
		this.labelQuotaAllontanamentoZero = labelQuotaAllontanamentoZero;
	}

	@Override
	public String toString() {
		return "FondiEnteAllontanamentoZero [fondoRegionale="
				+ fondoRegionale
				+ ", quotaAllontanamentoZero="
				+ quotaAllontanamentoZero
				+ "]";
	}
	
	
}
