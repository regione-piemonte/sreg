/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

import it.csi.greg.gregsrv.business.entity.GregDDettaglioDisabilita;

public class ModelCDettagliDisabilita {
	private Integer idDettagliDisabilita;
	private String codDettagliDisabilita;
	private String descDettagliDisabilita;
	private BigDecimal valore;
	
	public ModelCDettagliDisabilita () {}
	
	public ModelCDettagliDisabilita (GregDDettaglioDisabilita disabilita) {
		this.idDettagliDisabilita = disabilita.getIdDettaglioDisabilita();
		this.codDettagliDisabilita = disabilita.getCodDettaglioDisabilita();
		this.descDettagliDisabilita = disabilita.getDescDettaglioDisabilita();
	}

	public Integer getIdDettagliDisabilita() {
		return idDettagliDisabilita;
	}

	public void setIdDettagliDisabilita(Integer idDettagliDisabilita) {
		this.idDettagliDisabilita = idDettagliDisabilita;
	}

	public String getCodDettagliDisabilita() {
		return codDettagliDisabilita;
	}

	public void setCodDettagliDisabilita(String codDettagliDisabilita) {
		this.codDettagliDisabilita = codDettagliDisabilita;
	}

	public String getDescDettagliDisabilita() {
		return descDettagliDisabilita;
	}

	public void setDescDettagliDisabilita(String descDettagliDisabilita) {
		this.descDettagliDisabilita = descDettagliDisabilita;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}
	
	
}
