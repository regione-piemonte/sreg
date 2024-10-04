/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.Date;

public class ModelChiusura {
	private Integer idScheda;
	private Date dataChiusura;
	private String motivazione;
	private String notaEnte;
	private String notaInterna;
	private ModelProfilo profilo;
	private String denominazione;
	private String email;
	private String nome;
	private String cognome;
	private String emailResp;
	
	
	public ModelChiusura () {}

	public Integer getIdScheda() {
		return idScheda;
	}

	public void setIdScheda(Integer idScheda) {
		this.idScheda = idScheda;
	}

	public Date getDataChiusura() {
		return dataChiusura;
	}

	public void setDataChiusura(Date dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}

	public String getNotaEnte() {
		return notaEnte;
	}

	public void setNotaEnte(String notaEnte) {
		this.notaEnte = notaEnte;
	}

	public String getNotaInterna() {
		return notaInterna;
	}

	public void setNotaInterna(String notaInterna) {
		this.notaInterna = notaInterna;
	}

	public ModelProfilo getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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

	public String getEmailResp() {
		return emailResp;
	}

	public void setEmailResp(String emailResp) {
		this.emailResp = emailResp;
	}


}
