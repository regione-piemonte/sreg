/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class InfoModello {
	private String desModello;
	private String codModello;
	private Integer idRendicontazione;

	//altri modelli
	public InfoModello () {}

	
	public String getDesModello() {
		return desModello;
	}


	public void setDesModello(String desModello) {
		this.desModello = desModello;
	}


	public String getCodModello() {
		return codModello;
	}

	public void setCodModello(String codModello) {
		this.codModello = codModello;
	}

	public Integer getIdRendicontazione() {
		return idRendicontazione;
	}

	public void setIdRendicontazione(Integer idRendicontazione) {
		this.idRendicontazione = idRendicontazione;
	}
	
	
}
