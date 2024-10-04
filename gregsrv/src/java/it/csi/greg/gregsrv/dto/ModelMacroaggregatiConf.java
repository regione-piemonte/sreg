/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;

public class ModelMacroaggregatiConf {
	
	private Integer idMacroaggregati;
	private String codMacroaggregati;
	private String descMacroaggregati;
	private Timestamp dal;
	private Timestamp al;
	private Timestamp dataCancellazione;
	private boolean modificabile;
	private Timestamp dataMin;
	private Timestamp dataCreazione;
	
	public ModelMacroaggregatiConf () {}


	public Integer getIdMacroaggregati() {
		return idMacroaggregati;
	}


	public void setIdMacroaggregati(Integer idMacroaggregati) {
		this.idMacroaggregati = idMacroaggregati;
	}


	public String getCodMacroaggregati() {
		return codMacroaggregati;
	}

	public void setCodMacroaggregati(String codMacroaggregati) {
		this.codMacroaggregati = codMacroaggregati;
	}

	public String getDescMacroaggregati() {
		return descMacroaggregati;
	}

	public void setDescMacroaggregati(String descMacroaggregati) {
		this.descMacroaggregati = descMacroaggregati;
	}

	public Timestamp getDal() {
		return dal;
	}

	public void setDal(Timestamp dal) {
		this.dal = dal;
	}

	public Timestamp getAl() {
		return al;
	}

	public void setAl(Timestamp al) {
		this.al = al;
	}

	public boolean isModificabile() {
		return modificabile;
	}


	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}


	public Timestamp getDataMin() {
		return dataMin;
	}


	public void setDataMin(Timestamp dataMin) {
		this.dataMin = dataMin;
	}


	public Timestamp getDataCancellazione() {
		return dataCancellazione;
	}


	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}


	public Timestamp getDataCreazione() {
		return dataCreazione;
	}


	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

}
