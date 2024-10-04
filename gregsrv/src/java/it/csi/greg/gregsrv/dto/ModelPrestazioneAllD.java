/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelPrestazioneAllD {
	private Integer idPrestazione;
	private String codPrestazione;
	private String descPrestazione;
	private String macroAttivita;
	private List<ModelUtenzaAllD> utenze;

	public ModelPrestazioneAllD () {}

	public Integer getIdPrestazione() {
		return idPrestazione;
	}

	public void setIdPrestazione(Integer idPrestazione) {
		this.idPrestazione = idPrestazione;
	}

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

	public String getMacroAttivita() {
		return macroAttivita;
	}

	public void setMacroAttivita(String macroAttivita) {
		this.macroAttivita = macroAttivita;
	}

	public List<ModelUtenzaAllD> getUtenze() {
		return utenze;
	}

	public void setUtenze(List<ModelUtenzaAllD> utenze) {
		this.utenze = utenze;
	}

}
