/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregDDisabilita;

public class ModelCDisabilita {
	private Integer idDisabilita;
	private String codDisabilita;
	private String descDisabilita;
	private List<ModelCDettagliDisabilita> listaDettagliDisabilita;
	private BigDecimal valore;
	
	public ModelCDisabilita () {}
	
	public ModelCDisabilita (GregDDisabilita disabilita) {
		this.idDisabilita = disabilita.getIdDisabilita();
		this.codDisabilita = disabilita.getCodDisabilita();
		this.descDisabilita = disabilita.getDesDisabilita();
		this.listaDettagliDisabilita = new ArrayList<ModelCDettagliDisabilita>();
	}

	public Integer getIdDisabilita() {
		return idDisabilita;
	}

	public void setIdDisabilita(Integer idDisabilita) {
		this.idDisabilita = idDisabilita;
	}

	public String getCodDisabilita() {
		return codDisabilita;
	}

	public void setCodDisabilita(String codDisabilita) {
		this.codDisabilita = codDisabilita;
	}

	public String getDescDisabilita() {
		return descDisabilita;
	}

	public void setDescDisabilita(String descDisabilita) {
		this.descDisabilita = descDisabilita;
	}

	public List<ModelCDettagliDisabilita> getListaDettagliDisabilita() {
		return listaDettagliDisabilita;
	}

	public void setListaDettagliDisabilita(List<ModelCDettagliDisabilita> listaDettagliDisabilita) {
		this.listaDettagliDisabilita = listaDettagliDisabilita;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}
	
}
