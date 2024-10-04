/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.Date;

import it.csi.greg.gregsrv.business.entity.GregDMotivazione;
import it.csi.greg.gregsrv.business.entity.GregREnteGestoreStatoEnte;

public class ModelCronogiaStato {
	private Date dal;
	private Date al;
	private String operatore;
	private String stato;
	private String motivazione;
	private String notaEnte;
	private String notaInterna;

	//altri modelli
	public ModelCronogiaStato () {}
	

	public ModelCronogiaStato(GregREnteGestoreStatoEnte stato, String operatore) {
		this.dal = stato.getDataInizioValidita();
		this.al = stato.getDataFineValidita();
		this.operatore = operatore;
		this.stato = stato.getGregDStatoEnte().getDescStatoEnte();
		this.motivazione = stato.getGregDMotivazione() == null ? " " : stato.getGregDMotivazione().getDescMotivazione();
		this.notaEnte = stato.getNotaPerEnte();
		this.notaInterna = stato.getNotaInterna();
	}



	public Date getDal() {
		return dal;
	}

	public void setDal(Date dal) {
		this.dal = dal;
	}

	public Date getAl() {
		return al;
	}

	public void setAl(Date al) {
		this.al = al;
	}

	public String getOperatore() {
		return operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
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

	
	
	
}
