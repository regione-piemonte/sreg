/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;


public class ModelDenominazioniComuniAssociati {
	
	private Integer idSchedaEnteGestore;
	private String denominazione;
	private ArrayList<Integer> comuniAssociati;
	
	
	public ModelDenominazioniComuniAssociati() { }


	public Integer getIdSchedaEnteGestore() {
		return idSchedaEnteGestore;
	}


	public void setIdSchedaEnteGestore(Integer idSchedaEnteGestore) {
		this.idSchedaEnteGestore = idSchedaEnteGestore;
	}


	public String getDenominazione() {
		return denominazione;
	}


	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}


	public ArrayList<Integer> getComuniAssociati() {
		return comuniAssociati;
	}


	public void setComuniAssociati(ArrayList<Integer> comuniAssociati) {
		this.comuniAssociati = comuniAssociati;
	}
	
}