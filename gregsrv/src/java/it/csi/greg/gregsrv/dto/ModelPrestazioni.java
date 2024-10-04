/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;


public class ModelPrestazioni {
	
	private Integer idPrestazione;
	private String codicePrestazione;
	private String desPrestazione;
	private Boolean prestFiglia;
	private List<ModelPrestazioni> prestazioniCollegate;
	
	
	public ModelPrestazioni() { }
	
	public ModelPrestazioni(GregTPrestazioniRegionali1 prestazione, Boolean figlia) {
		this.idPrestazione = prestazione.getIdPrestReg1();
		this.codicePrestazione = prestazione.getCodPrestReg1();
		this.desPrestazione = prestazione.getDesPrestReg1();
		this.prestFiglia = figlia;
	}
	
	public ModelPrestazioni(GregTPrestazioniRegionali1 prestazioneMadre, List<ModelPrestazioni> prestazioniFiglie, Boolean figlia) {
		this.idPrestazione = prestazioneMadre.getIdPrestReg1();
		this.codicePrestazione = prestazioneMadre.getCodPrestReg1();
		this.desPrestazione = prestazioneMadre.getDesPrestReg1();
		this.prestazioniCollegate = prestazioniFiglie;
		this.prestFiglia = figlia;
	}

	public Integer getIdPrestazione() {
		return idPrestazione;
	}

	public void setIdPrestazione(Integer idPrestazione) {
		this.idPrestazione = idPrestazione;
	}

	public String getCodicePrestazione() {
		return codicePrestazione;
	}

	public void setCodicePrestazione(String codicePrestazione) {
		this.codicePrestazione = codicePrestazione;
	}

	public String getDesPrestazione() {
		return desPrestazione;
	}

	public void setDesPrestazione(String desPrestazione) {
		this.desPrestazione = desPrestazione;
	}

	public Boolean getPrestFiglia() {
		return prestFiglia;
	}

	public void setPrestFiglia(Boolean prestFiglia) {
		this.prestFiglia = prestFiglia;
	}

	public List<ModelPrestazioni> getPrestazioniCollegate() {
		return prestazioniCollegate;
	}

	public void setPrestazioniCollegate(List<ModelPrestazioni> prestazioniCollegate) {
		this.prestazioniCollegate = prestazioniCollegate;
	}
	
}