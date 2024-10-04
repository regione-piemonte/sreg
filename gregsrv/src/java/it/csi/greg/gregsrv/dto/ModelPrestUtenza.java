/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;

public class ModelPrestUtenza {

	private Integer idUtenza;
	private String codUtenza;
	private String descUtenza;
	private Integer idMissioneProgrammaRelazione;
	private String codMissioneProgramma;
	private String descMissioneProgramma;
	private String coloreMissioneProgramma;
	private Integer idTipologiaSpesaRelazione;
	private String codTipologiaSpesa;
	private String descTipologiaSpesa;
	private String coloreTipologiaSpesa;
	private Timestamp dal;
	private Timestamp al;
	private Timestamp dataMin;
	private boolean modificabile;
	private Timestamp dataCancellazione;
	private Timestamp dataCreazione;
	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public ModelPrestUtenza () {}

	public Integer getIdUtenza() {
		return idUtenza;
	}

	public void setIdUtenza(Integer idUtenza) {
		this.idUtenza = idUtenza;
	}

	public String getCodUtenza() {
		return codUtenza;
	}

	public void setCodUtenza(String codUtenza) {
		this.codUtenza = codUtenza;
	}

	public String getDescUtenza() {
		return descUtenza;
	}

	public void setDescUtenza(String descUtenza) {
		this.descUtenza = descUtenza;
	}

	public String getCodMissioneProgramma() {
		return codMissioneProgramma;
	}

	public void setCodMissioneProgramma(String codMissioneProgramma) {
		this.codMissioneProgramma = codMissioneProgramma;
	}

	public String getDescMissioneProgramma() {
		return descMissioneProgramma;
	}

	public void setDescMissioneProgramma(String descMissioneProgramma) {
		this.descMissioneProgramma = descMissioneProgramma;
	}

	public String getCodTipologiaSpesa() {
		return codTipologiaSpesa;
	}

	public void setCodTipologiaSpesa(String codTipologiaSpesa) {
		this.codTipologiaSpesa = codTipologiaSpesa;
	}

	public String getDescTipologiaSpesa() {
		return descTipologiaSpesa;
	}

	public void setDescTipologiaSpesa(String descTipologiaSpesa) {
		this.descTipologiaSpesa = descTipologiaSpesa;
	}

	public String getColoreMissioneProgramma() {
		return coloreMissioneProgramma;
	}

	public void setColoreMissioneProgramma(String coloreMissioneProgramma) {
		this.coloreMissioneProgramma = coloreMissioneProgramma;
	}

	public String getColoreTipologiaSpesa() {
		return coloreTipologiaSpesa;
	}

	public void setColoreTipologiaSpesa(String coloreTipologiaSpesa) {
		this.coloreTipologiaSpesa = coloreTipologiaSpesa;
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

	public Integer getIdMissioneProgrammaRelazione() {
		return idMissioneProgrammaRelazione;
	}

	public void setIdMissioneProgrammaRelazione(Integer idMissioneProgrammaRelazione) {
		this.idMissioneProgrammaRelazione = idMissioneProgrammaRelazione;
	}

	public Integer getIdTipologiaSpesaRelazione() {
		return idTipologiaSpesaRelazione;
	}

	public void setIdTipologiaSpesaRelazione(Integer idTipologiaSpesaRelazione) {
		this.idTipologiaSpesaRelazione = idTipologiaSpesaRelazione;
	}

	public Timestamp getDataCancellazione() {
		return dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}
	
}
