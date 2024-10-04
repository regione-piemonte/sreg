/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;
import java.util.List;

public class GenericResponseWarnErr extends GenericResponse {
	
	private List<String> warnings;
	private List<String> errors;
	private List<String> ok;
	private String warningCheck;
	private boolean obblMotivazione; 
	private Integer idEnte;
	private Integer idPrestazione;
	private Timestamp dataCreazione;
	
	public List<String> getWarnings() {
		return warnings;
	}
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public boolean isObblMotivazione() {
		return obblMotivazione;
	}
	public void setObblMotivazione(boolean obblMotivazione) {
		this.obblMotivazione = obblMotivazione;
	}
	public Integer getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}
	public String getWarningCheck() {
		return warningCheck;
	}
	public void setWarningCheck(String warningCheck) {
		this.warningCheck = warningCheck;
	}
	public Integer getIdPrestazione() {
		return idPrestazione;
	}
	public void setIdPrestazione(Integer idPrestazione) {
		this.idPrestazione = idPrestazione;
	}
	public List<String> getOk() {
		return ok;
	}
	public void setOk(List<String> ok) {
		this.ok = ok;
	}
	public Timestamp getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
}
