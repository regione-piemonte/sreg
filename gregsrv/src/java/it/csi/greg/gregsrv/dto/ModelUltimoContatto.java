/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;


public class ModelUltimoContatto {
	
	private String denominazione;
	private String email;
	private ModelResponsabileEnte responsabileEnte;

	
	public ModelUltimoContatto() { }
	
	public ModelUltimoContatto(Object[] obj) {
		this.denominazione =  String.valueOf(obj[0]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[0]);
		this.email =  String.valueOf(obj[1]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[1]);
		this.responsabileEnte = new ModelResponsabileEnte(null,String.valueOf(obj[2]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[2]),
				String.valueOf(obj[3]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[3]),
				String.valueOf(obj[5]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[5]),
				null,
				null,
				String.valueOf(obj[4]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[4]));
		
	}
	
	
	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ModelResponsabileEnte getResponsabileEnte() {
		return responsabileEnte;
	}

	public void setResponsabileEnte(ModelResponsabileEnte responsabileEnte) {
		this.responsabileEnte = responsabileEnte;
	}	
}