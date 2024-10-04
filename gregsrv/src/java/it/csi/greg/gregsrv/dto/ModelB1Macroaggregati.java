/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class ModelB1Macroaggregati {
	private String codice;
	private int IDPrestReg1MacroaggregatoBilancio;
	private String valore;
	private int IDTabellaRiferimento;
	
	public ModelB1Macroaggregati() {}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public int getIDPrestReg1MacroaggregatoBilancio() {
		return IDPrestReg1MacroaggregatoBilancio;
	}

	public void setIDPrestReg1MacroaggregatoBilancio(int iDPrestReg1MacroaggregatoBilancio) {
		IDPrestReg1MacroaggregatoBilancio = iDPrestReg1MacroaggregatoBilancio;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public int getIDTabellaRiferimento() {
		return IDTabellaRiferimento;
	}

	public void setIDTabellaRiferimento(int iDTabellaRiferimento) {
		IDTabellaRiferimento = iDTabellaRiferimento;
	}
	
	

}
