/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;

import it.csi.greg.gregsrv.business.entity.GregDColori;

public class ModelColori {

	private Integer idColore;
	private String desColore;
	private String rgb;
	private String utenteOperazione;
	private Timestamp dataCreazione;
	private Timestamp dataModifica;
	private Timestamp dataCancellazione;
	
	public ModelColori() { }
	
	public ModelColori(GregDColori colore) {
		if (colore != null) {
			this.idColore = colore.getIdColore();
			this.desColore = colore.getDesColore();
			this.rgb = colore.getRgb();
			this.utenteOperazione = colore.getUtenteOperazione();
			this.dataCreazione = colore.getDataCreazione();
			this.dataCancellazione = colore.getDataCancellazione();
			this.dataModifica = colore.getDataModifica();
			
		}
	}

	public Integer getIdColore() {
		return idColore;
	}

	public void setIdColore(Integer idColore) {
		this.idColore = idColore;
	}

	public String getDesColore() {
		return desColore;
	}

	public void setDesColore(String desColore) {
		this.desColore = desColore;
	}

	public String getRgb() {
		return rgb;
	}

	public void setRgb(String rgb) {
		this.rgb = rgb;
	}

	public String getUtenteOperazione() {
		return utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Timestamp getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(Timestamp dataModifica) {
		this.dataModifica = dataModifica;
	}

	public Timestamp getDataCancellazione() {
		return dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}
	
		
	
}
