/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelB1VociPrestReg2 {
	private String codPrestazione;
	private String descPrestazione;
	private List<ModelB1UtenzaPrestReg2> utenze;

	public ModelB1VociPrestReg2() {}

	public String getCodPrestazione() {
		return codPrestazione;
	}

	public void setCodPrestazione(String codPrestazione) {
		this.codPrestazione = codPrestazione;
	}

	public String getDescPrestazione() {
		return descPrestazione;
	}

	public void setDescPrestazione(String descPrestazione) {
		this.descPrestazione = descPrestazione;
	}

	public List<ModelB1UtenzaPrestReg2> getUtenze() {
		return utenze;
	}

	public void setUtenze(List<ModelB1UtenzaPrestReg2> utenze) {
		this.utenze = utenze;
	}
	
	
}
