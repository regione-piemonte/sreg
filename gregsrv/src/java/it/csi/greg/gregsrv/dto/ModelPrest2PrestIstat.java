/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;
import java.util.List;

public class ModelPrest2PrestIstat {

	private Integer idPrestIstat;
	private String codPrestIstat;
	private String descPrestIstat;
	private List<ModelPrestUtenza> utenzeMinConf;
	private List<String> utenzeMin;
	private Timestamp dataCancellazione;
	private Timestamp dataCreazione;
	
	public ModelPrest2PrestIstat () {}

	public Integer getIdPrestIstat() {
		return idPrestIstat;
	}

	public void setIdPrestIstat(Integer idPrestIstat) {
		this.idPrestIstat = idPrestIstat;
	}

	public String getCodPrestIstat() {
		return codPrestIstat;
	}

	public void setCodPrestIstat(String codPrestIstat) {
		this.codPrestIstat = codPrestIstat;
	}

	public String getDescPrestIstat() {
		return descPrestIstat;
	}

	public void setDescPrestIstat(String descPrestIstat) {
		this.descPrestIstat = descPrestIstat;
	}

	public List<ModelPrestUtenza> getUtenzeMinConf() {
		return utenzeMinConf;
	}

	public void setUtenzeMinConf(List<ModelPrestUtenza> utenzeMinConf) {
		this.utenzeMinConf = utenzeMinConf;
	}

	public List<String> getUtenzeMin() {
		return utenzeMin;
	}

	public void setUtenzeMin(List<String> utenzeMin) {
		this.utenzeMin = utenzeMin;
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
