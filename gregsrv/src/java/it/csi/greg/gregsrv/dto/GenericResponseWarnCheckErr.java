/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class GenericResponseWarnCheckErr extends GenericResponse {
	
	private List<String> warnings;
	private List<String> errors;
	private List<String> check;
	private Integer idEnte;
	private boolean valorizzato;
	
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
	
	public Integer getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}
	public List<String> getCheck() {
		return check;
	}
	public void setCheck(List<String> check) {
		this.check = check;
	}
	
	public boolean isValorizzato() {
		return valorizzato;
	}
	public void setValorizzato(boolean valorizzato) {
		this.valorizzato = valorizzato;
	}
	
}
