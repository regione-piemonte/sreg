/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.Date;

import it.csi.greg.gregsrv.business.entity.GregTCronologia;

public class ModelCronologiaEnte {
	
	private Integer idCronologia;
	private String notaEnte;
	private String notaInterna;
	private String utente;
	private String modello;
	private String dataOra;
	private String utenteOperazione;
	private Date dataCreazione;
	private Date dataModifica;
	private Date dataCancellazione;
	private ModelStatoRendicontazione statoRendicontazione;
	
	public ModelCronologiaEnte() { }
	
	public ModelCronologiaEnte(GregTCronologia cronologia) {
		
		this.idCronologia = cronologia.getIdCronologia();
		this.notaInterna = cronologia.getNotaInterna();
		this.notaEnte = cronologia.getNotaPerEnte();
		this.utente = cronologia.getUtente();
		this.modello = cronologia.getModello();
		this.dataOra = cronologia.getDataOra().toString();
		this.utenteOperazione = cronologia.getUtenteOperazione();
		this.dataCreazione = cronologia.getDataCreazione();
		this.dataModifica = cronologia.getDataModifica();
		this.dataCancellazione = cronologia.getDataCancellazione();
		this.statoRendicontazione = new ModelStatoRendicontazione(cronologia.getGregDStatoRendicontazione());
	}

	public Integer getIdCronologia() {
		return idCronologia;
	}

	public void setIdCronologia(Integer idCronologia) {
		this.idCronologia = idCronologia;
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

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getDataOra() {
		return dataOra;
	}

	public void setDataOra(String dataOra) {
		this.dataOra = dataOra;
	}

	public String getUtenteOperazione() {
		return utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Date getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(Date dataModifica) {
		this.dataModifica = dataModifica;
	}

	public Date getDataCancellazione() {
		return dataCancellazione;
	}

	public void setDataCancellazione(Date dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	public ModelStatoRendicontazione getStatoRendicontazione() {
		return statoRendicontazione;
	}

	public void setStatoRendicontazione(ModelStatoRendicontazione statoRendicontazione) {
		this.statoRendicontazione = statoRendicontazione;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}
	
}