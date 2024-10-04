/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class ModelRendicontazioneModAPart3 {
	
	private Integer idVoce;
	private String codVoce;
	private String sezioneModello;
	private String descVoce;
	private BigDecimal valore;
	private Integer idRendicontazioneEnte;
	private Integer ordinamento;

	public ModelRendicontazioneModAPart3 () {}
	

	public Integer getIdVoce() {
		return idVoce;
	}

	public void setIdVoce(Integer idVoce) {
		this.idVoce = idVoce;
	}

	public String getCodVoce() {
		return codVoce;
	}

	public void setCodVoce(String codVoce) {
		this.codVoce = codVoce;
	}

	public String getSezioneModello() {
		return sezioneModello;
	}

	public void setSezioneModello(String sezioneModello) {
		this.sezioneModello = sezioneModello;
	}

	public String getDescVoce() {
		return descVoce;
	}

	public void setDescVoce(String descVoce) {
		this.descVoce = descVoce;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}
	
}