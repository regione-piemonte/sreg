/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;
import java.util.List;

public class ModelPrest1PrestMin {

	private String codMacro;
	private String descMacro;
	private Integer idPrestMin;
	private String codPrestMin;
	private String descPrestMin;
	private List<String> utenzeMin;
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

	public ModelPrest1PrestMin() {
	}

	public String getCodMacro() {
		return codMacro;
	}

	public void setCodMacro(String codMacro) {
		this.codMacro = codMacro;
	}

	public String getDescMacro() {
		return descMacro;
	}

	public void setDescMacro(String descMacro) {
		this.descMacro = descMacro;
	}

	public Integer getIdPrestMin() {
		return idPrestMin;
	}

	public void setIdPrestMin(Integer idPrestMin) {
		this.idPrestMin = idPrestMin;
	}

	public String getCodPrestMin() {
		return codPrestMin;
	}

	public void setCodPrestMin(String codPrestMin) {
		this.codPrestMin = codPrestMin;
	}

	public String getDescPrestMin() {
		return descPrestMin;
	}

	public void setDescPrestMin(String descPrestMin) {
		this.descPrestMin = descPrestMin;
	}

	public List<String> getUtenzeMin() {
		return utenzeMin;
	}

	public void setUtenzeMin(List<String> utenzeMin) {
		this.utenzeMin = utenzeMin;
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

}
