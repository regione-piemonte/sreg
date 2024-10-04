/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;

public class ModelCTargetUtenze {
	private Integer idTargetUtenze;
	private String codTargetUtenze;
	private String descTargetUtenze;
	private List<ModelCDettagliUtenze> listaDettagliUtenze;
	private List<ModelCDisabilita> listaDisabilita;
	private BigDecimal valore;
	
	public ModelCTargetUtenze () {}
	
	public ModelCTargetUtenze (GregDTargetUtenza target) {
		this.idTargetUtenze = target.getIdTargetUtenza();
		this.codTargetUtenze = target.getCodUtenza();
		this.descTargetUtenze = target.getDesUtenza();
		this.listaDettagliUtenze = new ArrayList<ModelCDettagliUtenze>();
		this.listaDisabilita = new ArrayList<ModelCDisabilita>();
	}

	public Integer getIdTargetUtenze() {
		return idTargetUtenze;
	}

	public void setIdTargetUtenze(Integer idTargetUtenze) {
		this.idTargetUtenze = idTargetUtenze;
	}

	public String getCodTargetUtenze() {
		return codTargetUtenze;
	}

	public void setCodTargetUtenze(String codTargetUtenze) {
		this.codTargetUtenze = codTargetUtenze;
	}

	public String getDescTargetUtenze() {
		return descTargetUtenze;
	}

	public void setDescTargetUtenze(String descTargetUtenze) {
		this.descTargetUtenze = descTargetUtenze;
	}

	public List<ModelCDettagliUtenze> getListaDettagliUtenze() {
		return listaDettagliUtenze;
	}

	public void setListaDettagliUtenze(List<ModelCDettagliUtenze> listaDettagliUtenze) {
		this.listaDettagliUtenze = listaDettagliUtenze;
	}

	public List<ModelCDisabilita> getListaDisabilita() {
		return listaDisabilita;
	}

	public void setListaDisabilita(List<ModelCDisabilita> listaDisabilita) {
		this.listaDisabilita = listaDisabilita;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}
	
}
