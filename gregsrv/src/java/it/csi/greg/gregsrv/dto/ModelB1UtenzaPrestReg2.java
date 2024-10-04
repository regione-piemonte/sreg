/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class ModelB1UtenzaPrestReg2 {
	private String codice;
	private String valore;
	private int IDPrestReg2UtenzaReg2;
	private int IDTabellaRiferimento;
	
	public ModelB1UtenzaPrestReg2() {}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public int getIDPrestReg2UtenzaReg2() {
		return IDPrestReg2UtenzaReg2;
	}

	public void setIDPrestReg2UtenzaReg2(int iDPrestReg2UtenzaReg2) {
		IDPrestReg2UtenzaReg2 = iDPrestReg2UtenzaReg2;
	}

	public int getIDTabellaRiferimento() {
		return IDTabellaRiferimento;
	}

	public void setIDTabellaRiferimento(int iDTabellaRiferimento) {
		IDTabellaRiferimento = iDTabellaRiferimento;
	}

	

}
