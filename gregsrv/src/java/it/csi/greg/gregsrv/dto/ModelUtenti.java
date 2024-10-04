/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRUserProfilo;
import it.csi.greg.gregsrv.business.entity.GregTUser;

public class ModelUtenti {
	
	private Integer idUtente;
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String email;
	private List<ModelAbilitazioni> abilitazioni;
	private Timestamp maxData;
	private Timestamp dataFineValidita;
	
	public ModelUtenti () {}
	
	public ModelUtenti (GregTUser utente, List<GregRUserProfilo> userProfilo) {
		this.idUtente = utente.getIdUser();
		this.codiceFiscale = utente.getCodiceFiscale();
		this.nome = utente.getNome();
		this.cognome = utente.getCognome();
		this.email = utente.getEmail();
		this.maxData = null;
		this.abilitazioni = new ArrayList<ModelAbilitazioni>();
		for(GregRUserProfilo up : userProfilo) {
			ModelAbilitazioni a = new ModelAbilitazioni(up);
			this.abilitazioni.add(a);
			if(up.getDataFineValidita()==null) {
				if(this.maxData==null) {
					this.maxData=up.getDataInizioValidita();
				}else if(up.getDataInizioValidita().getTime()>this.maxData.getTime()) {
					this.maxData=up.getDataInizioValidita();
				}
			}
		}
	}

	public Integer getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<ModelAbilitazioni> getAbilitazioni() {
		return abilitazioni;
	}

	public void setAbilitazioni(List<ModelAbilitazioni> abilitazioni) {
		this.abilitazioni = abilitazioni;
	}

	public Timestamp getMaxData() {
		return maxData;
	}

	public void setMaxData(Timestamp maxData) {
		this.maxData = maxData;
	}

	public Timestamp getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Timestamp dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

}