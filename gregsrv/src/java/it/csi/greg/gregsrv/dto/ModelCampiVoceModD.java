/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;


public class ModelCampiVoceModD {
	
	private String voce;
	private String codVoce;
	private Boolean show;
	private BigDecimal value;
	private Integer ordinamento;
	
	public ModelCampiVoceModD () {}

	public String getVoce() {
		return voce;
	}

	public void setVoce(String voce) {
		this.voce = voce;
	}

	public String getCodVoce() {
		return codVoce;
	}

	public void setCodVoce(String codVoce) {
		this.codVoce = codVoce;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}
	
}