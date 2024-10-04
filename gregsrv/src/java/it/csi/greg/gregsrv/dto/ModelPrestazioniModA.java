/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelPrestazioniModA {

	private List<ModelPrestazioneUtenzaModA> prestazioniRS;
	private List<ModelTargetUtenza> subtotaliRS;
	private List<ModelPrestazioneUtenzaModA> prestazioniCD;
	private List<ModelTargetUtenza> subtotaliCD;
	private List<ModelTargetUtenza> totaliSRCD;
	
	public ModelPrestazioniModA() { }
	
	public List<ModelPrestazioneUtenzaModA> getPrestazioniRS() {
		return prestazioniRS;
	}
	public void setPrestazioniRS(List<ModelPrestazioneUtenzaModA> prestazioniRS) {
		this.prestazioniRS = prestazioniRS;
	}
	public List<ModelPrestazioneUtenzaModA> getPrestazioniCD() {
		return prestazioniCD;
	}
	public void setPrestazioniCD(List<ModelPrestazioneUtenzaModA> prestazioniCD) {
		this.prestazioniCD = prestazioniCD;
	}

	public List<ModelTargetUtenza> getSubtotaliRS() {
		return subtotaliRS;
	}

	public void setSubtotaliRS(List<ModelTargetUtenza> subtotaliRS) {
		this.subtotaliRS = subtotaliRS;
	}

	public List<ModelTargetUtenza> getSubtotaliCD() {
		return subtotaliCD;
	}

	public void setSubtotaliCD(List<ModelTargetUtenza> subtotaliCD) {
		this.subtotaliCD = subtotaliCD;
	}

	public List<ModelTargetUtenza> getTotaliSRCD() {
		return totaliSRCD;
	}

	public void setTotaliSRCD(List<ModelTargetUtenza> totaliSRCD) {
		this.totaliSRCD = totaliSRCD;
	}
	
}
