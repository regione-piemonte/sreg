/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class ModelStatoModelli {
	private String codModello;
	private boolean trovato;
	private String codTranche;
	private String statoModello;

	//altri modelli
	public ModelStatoModelli () {}
	
	

	public ModelStatoModelli(Object[] o) {
		this.codModello = (String) o[0];
		this.codTranche = (String) o[1];
		this.trovato = (boolean) o[2];
	}



	public String getCodModello() {
		return codModello;
	}

	public void setCodModello(String codModello) {
		this.codModello = codModello;
	}

	public String getStatoModello() {
		return statoModello;
	}

	public void setStatoModello(String statoModello) {
		this.statoModello = statoModello;
	}

	public String getCodTranche() {
		return codTranche;
	}



	public void setCodTranche(String codTranche) {
		this.codTranche = codTranche;
	}



	public boolean isTrovato() {
		return trovato;
	}

	public void setTrovato(boolean trovato) {
		this.trovato = trovato;
	}

}
