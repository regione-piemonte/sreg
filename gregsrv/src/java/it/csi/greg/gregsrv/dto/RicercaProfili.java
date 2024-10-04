/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class RicercaProfili {
	
	private Integer profilo;
	private Integer lista;
	private Integer ente;
	private Integer azione;
	
	public Integer getProfilo() {
		return profilo;
	}
	public void setProfilo(Integer profilo) {
		this.profilo = profilo;
	}
	public Integer getAzione() {
		return azione;
	}
	public void setAzione(Integer azione) {
		this.azione = azione;
	}
	public Integer getLista() {
		return lista;
	}
	public void setLista(Integer lista) {
		this.lista = lista;
	}
	public Integer getEnte() {
		return ente;
	}
	public void setEnte(Integer ente) {
		this.ente = ente;
	}

	

	
}
