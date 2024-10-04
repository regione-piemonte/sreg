/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;


public class ModelResponsabileEnte {
	
	private Integer idResponsabile;
	private String cognome;
	private String nome;
	private String cellulare;
	private String telefono;
	private String codiceFiscale;
	private String eMail;
	
	public ModelResponsabileEnte() { }

	
	public ModelResponsabileEnte(Integer idresponsabile, String cognome, String nome, String codicefiscale, String cellulare, String telefono, String email) {
		this.idResponsabile = idresponsabile;
		this.cognome = cognome;
		this.nome = nome;
		this.codiceFiscale = codicefiscale;
		this.cellulare = cellulare;
		this.telefono = telefono;
		this.eMail = email;
	}
	
	public ModelResponsabileEnte(String cognome, String nome, String codicefiscale, String cellulare, String telefono, String email) {
		this.cognome = cognome;
		this.nome = nome;
		this.codiceFiscale = codicefiscale;
		this.cellulare = cellulare;
		this.telefono = telefono;
		this.eMail = email;
	}

	public Integer getIdResponsabile() {
		return idResponsabile;
	}

	public void setIdResponsabile(Integer idResponsabile) {
		this.idResponsabile = idResponsabile;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCellulare() {
		return cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}


	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	
}