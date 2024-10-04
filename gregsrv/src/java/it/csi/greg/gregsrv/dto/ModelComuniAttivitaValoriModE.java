/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelComuniAttivitaValoriModE {
	
	private Integer idComune;
	private String codIstatComune;
	private String descComune;
	private List<ModelRendicontazioneAttivitaModE> attivita;
	
	public ModelComuniAttivitaValoriModE () {}

	public Integer getIdComune() {
		return idComune;
	}

	public void setIdComune(Integer idComune) {
		this.idComune = idComune;
	}

	public String getCodIstatComune() {
		return codIstatComune;
	}

	public void setCodIstatComune(String codIstatComune) {
		this.codIstatComune = codIstatComune;
	}

	public String getDescComune() {
		return descComune;
	}

	public void setDescComune(String descComune) {
		this.descComune = descComune;
	}

	public List<ModelRendicontazioneAttivitaModE> getAttivita() {
		return attivita;
	}

	public void setAttivita(List<ModelRendicontazioneAttivitaModE> attivita) {
		this.attivita = attivita;
	}
	
}