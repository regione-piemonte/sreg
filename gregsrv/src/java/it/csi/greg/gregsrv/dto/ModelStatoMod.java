/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class ModelStatoMod {
	private boolean valorizzato;
	private String stato;

	//altri modelli
	public ModelStatoMod () {}
	
	

	public ModelStatoMod(Object[] o) {
		this.valorizzato = (boolean) o[0];
		this.stato = (String) o[1];
	}



	public boolean isValorizzato() {
		return valorizzato;
	}

	public void setValorizzato(boolean valorizzato) {
		this.valorizzato = valorizzato;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}


}
