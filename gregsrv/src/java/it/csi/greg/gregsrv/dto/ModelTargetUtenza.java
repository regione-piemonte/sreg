/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;

public class ModelTargetUtenza {
	
	private Integer idTargetUtenza;
	private String codTargetUtenza;
	private String descTargetUtenza;
	private BigDecimal valore;
	private boolean entry;
	
	public ModelTargetUtenza() { }
	
	public ModelTargetUtenza(GregDTargetUtenza d) {
		if(d != null) {
			this.idTargetUtenza = d.getIdTargetUtenza();
			this.codTargetUtenza = d.getCodUtenza();
			this.descTargetUtenza = d.getDesUtenza();
		}
	}
	
	public Integer getIdTargetUtenza() {
		return idTargetUtenza;
	}
	public void setIdTargetUtenza(Integer idTargetUtenza) {
		this.idTargetUtenza = idTargetUtenza;
	}
	public String getCodTargetUtenza() {
		return codTargetUtenza;
	}
	public void setCodTargetUtenza(String codTargetUtenza) {
		this.codTargetUtenza = codTargetUtenza;
	}
	public String getDescTargetUtenza() {
		return descTargetUtenza;
	}
	public void setDescTargetUtenza(String descTargetUtenza) {
		this.descTargetUtenza = descTargetUtenza;
	}
	public BigDecimal getValore() {
		return valore;
	}
	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}
	public boolean isEntry() {
		return entry;
	}
	public void setEntry(boolean entry) {
		this.entry = entry;
	}
	
	
}
