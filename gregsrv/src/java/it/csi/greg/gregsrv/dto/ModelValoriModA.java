/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class ModelValoriModA {
	
	private String codVoce;
	private String descVoce;
	private String codTipologia;
	private String descTipologia;
	private String sezioneModello;
	private BigDecimal valore;
	
	public ModelValoriModA () {}
	

	public String getCodVoce() {
		return codVoce;
	}

	public void setCodVoce(String codVoce) {
		this.codVoce = codVoce;
	}

	public String getDescVoce() {
		return descVoce;
	}

	public void setDescVoce(String descVoce) {
		this.descVoce = descVoce;
	}

	public String getSezioneModello() {
		return sezioneModello;
	}

	public void setSezioneModello(String sezioneModello) {
		this.sezioneModello = sezioneModello;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}


	public String getCodTipologia() {
		return codTipologia;
	}


	public void setCodTipologia(String codTipologia) {
		this.codTipologia = codTipologia;
	}


	public String getDescTipologia() {
		return descTipologia;
	}


	public void setDescTipologia(String descTipologia) {
		this.descTipologia = descTipologia;
	}
	
}