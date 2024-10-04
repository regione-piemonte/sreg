/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import it.csi.greg.gregsrv.business.entity.GregDDettaglioUtenze;

public class ModelCDettagliUtenze {
	private Integer idDettagliUtenze;
	private String codDettagliUtenze;
	private String descDettagliUtenze;
	private BigDecimal valore;
	
	public ModelCDettagliUtenze () {}
	
	public ModelCDettagliUtenze (GregDDettaglioUtenze utenze) {
		this.idDettagliUtenze = utenze.getIdDettaglioUtenza();
		this.codDettagliUtenze = utenze.getCodDettaglioUtenza();
		this.descDettagliUtenze = utenze.getDescDettaglioUtenza();
	}

	public Integer getIdDettagliUtenze() {
		return idDettagliUtenze;
	}

	public void setIdDettagliUtenze(Integer idDettagliUtenze) {
		this.idDettagliUtenze = idDettagliUtenze;
	}

	public String getCodDettagliUtenze() {
		return codDettagliUtenze;
	}

	public void setCodDettagliUtenze(String codDettagliUtenze) {
		this.codDettagliUtenze = codDettagliUtenze;
	}

	public String getDescDettagliUtenze() {
		return descDettagliUtenze;
	}

	public void setDescDettagliUtenze(String descDettagliUtenze) {
		this.descDettagliUtenze = descDettagliUtenze;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	
}
