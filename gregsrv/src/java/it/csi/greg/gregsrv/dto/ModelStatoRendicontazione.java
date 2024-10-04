/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;

public class ModelStatoRendicontazione {
	
	private Integer idStatoRendicontazione;
	private String codStatoRendicontazione;
	private String descStatoRendicontazione;	
	
	public ModelStatoRendicontazione() { }
	
	public ModelStatoRendicontazione(GregDStatoRendicontazione stato) {
		if(stato != null) {
			this.idStatoRendicontazione = stato.getIdStatoRendicontazione();
			this.codStatoRendicontazione = stato.getCodStatoRendicontazione();
			this.descStatoRendicontazione = stato.getDesStatoRendicontazione();
		}
	}
	
	public ModelStatoRendicontazione(Integer idStato, String codStato, String descStato) {
		this.idStatoRendicontazione = idStato;
		this.codStatoRendicontazione = codStato;
		this.descStatoRendicontazione = descStato;
	}
	
	public ModelStatoRendicontazione(String codStato, String descStato) {
		this.codStatoRendicontazione = codStato;
		this.descStatoRendicontazione = descStato;
	}


	public Integer getIdStatoRendicontazione() {
		return idStatoRendicontazione;
	}

	public void setIdStatoRendicontazione(Integer idStatoRendicontazione) {
		this.idStatoRendicontazione = idStatoRendicontazione;
	}

	public String getCodStatoRendicontazione() {
		return codStatoRendicontazione;
	}

	public void setCodStatoRendicontazione(String codStatoRendicontazione) {
		this.codStatoRendicontazione = codStatoRendicontazione;
	}

	public String getDescStatoRendicontazione() {
		return descStatoRendicontazione;
	}

	public void setDescStatoRendicontazione(String descStatoRendicontazione) {
		this.descStatoRendicontazione = descStatoRendicontazione;
	}
}