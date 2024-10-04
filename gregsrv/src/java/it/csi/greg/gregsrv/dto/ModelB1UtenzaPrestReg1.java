/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class ModelB1UtenzaPrestReg1 {
	
	private String codice;
	private String descUtenza;
	private String colore;
	private String codiceProgrammaMissione;
	private BigDecimal sommaUTASRModA;
	private String valore;
	private int IDPrestReg1UtenzaReg1;
	private int IDTabellaRiferimento;
	private boolean mandatory;
	
	public ModelB1UtenzaPrestReg1() {}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescUtenza() {
		return descUtenza;
	}

	public void setDescUtenza(String descUtenza) {
		this.descUtenza = descUtenza;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public int getIDPrestReg1UtenzaReg1() {
		return IDPrestReg1UtenzaReg1;
	}

	public void setIDPrestReg1UtenzaReg1(int iDPrestReg1UtenzaReg1) {
		IDPrestReg1UtenzaReg1 = iDPrestReg1UtenzaReg1;
	}

	public int getIDTabellaRiferimento() {
		return IDTabellaRiferimento;
	}

	public void setIDTabellaRiferimento(int iDTabellaRiferimento) {
		IDTabellaRiferimento = iDTabellaRiferimento;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public String getCodiceProgrammaMissione() {
		return codiceProgrammaMissione;
	}

	public void setCodiceProgrammaMissione(String codiceProgrammaMissione) {
		this.codiceProgrammaMissione = codiceProgrammaMissione;
	}

	public BigDecimal getSommaUTASRModA() {
		return sommaUTASRModA;
	}

	public void setSommaUTASRModA(BigDecimal sommaUTASRModA) {
		this.sommaUTASRModA = sommaUTASRModA;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	
	

}
