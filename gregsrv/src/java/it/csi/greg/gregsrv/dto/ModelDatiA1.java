/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ModelDatiA1 {
	
	private String codcomune;
	Map<String, String> valore = new HashMap<String, String>();
	private String desccomune;
	private Date dataInizioValidita;
	private Date dataFineValidita;
	private Integer idComune;
	
	public ModelDatiA1 () {}
	

	public String getCodcomune() {
		return codcomune;
	}

	public void setCodcomune(String codcomune) {
		this.codcomune = codcomune;
	}


	public Map<String, String> getValore() {
		return valore;
	}


	public void setValore(Map<String, String> valore) {
		this.valore = valore;
	}


	public String getDesccomune() {
		return desccomune;
	}

	public void setDesccomune(String desccomune) {
		this.desccomune = desccomune;
	}


	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}


	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}


	public Date getDataFineValidita() {
		return dataFineValidita;
	}


	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}


	public Integer getIdComune() {
		return idComune;
	}


	public void setIdComune(Integer idComune) {
		this.idComune = idComune;
	}
	
}