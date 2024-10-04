/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigInteger;
import java.util.List;

public class ModelReferenteEnte {

	private String cognome;
	private String nome;
	private String email;
	private String codiceFiscale;
	private String profilo;
	
	//altri modelli
	public ModelReferenteEnte () {}

	
	public ModelReferenteEnte(Object[] o) {
		this.cognome = (String) o[0];
		this.nome = (String) o[1];
		this.codiceFiscale = (String) o[2];
		this.email = (String) o[3];
		this.profilo = (String) o[4];
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getCodiceFiscale() {
		return codiceFiscale;
	}


	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}


	public String getProfilo() {
		return profilo;
	}


	public void setProfilo(String profilo) {
		this.profilo = profilo;
	}

}
