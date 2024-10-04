/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;



public class ModelEntiComuniAssociati {
	
	private Integer idSchedaEnteGestore;
	private Integer idComune;
	private String denominazione;
	private String desComune;
	
	public ModelEntiComuniAssociati() { }
	
	public ModelEntiComuniAssociati(Object[] obj) { 
		this.idSchedaEnteGestore = (Integer) obj[0];
		this.idComune = (Integer) obj[1];
		this.denominazione = String.valueOf(obj[2]);
		this.desComune = String.valueOf(obj[3]);
	}

	public Integer getIdSchedaEnteGestore() {
		return idSchedaEnteGestore;
	}

	public void setIdSchedaEnteGestore(Integer idSchedaEnteGestore) {
		this.idSchedaEnteGestore = idSchedaEnteGestore;
	}

	public Integer getIdComune() {
		return idComune;
	}

	public void setIdComune(Integer idComune) {
		this.idComune = idComune;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getDesComune() {
		return desComune;
	}

	public void setDesComune(String desComune) {
		this.desComune = desComune;
	}
	
}