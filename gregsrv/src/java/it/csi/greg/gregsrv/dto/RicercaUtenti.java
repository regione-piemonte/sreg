/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class RicercaUtenti {
	
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String email;
	private Integer profilo;
	private Integer lista;
	private Integer ente;
	private boolean attivo; 
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getProfilo() {
		return profilo;
	}
	public void setProfilo(Integer profilo) {
		this.profilo = profilo;
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
	public boolean isAttivo() {
		return attivo;
	}
	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}
	
}
