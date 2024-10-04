/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import it.csi.greg.gregsrv.business.entity.GregDVoceModA;

public class ModelRendModAPart3 {
	
	private Integer idVoce;
	private String codVoce;
	private String descVoce;
	private int ordinamento;
	private String msgInformativo;
	private BigDecimal valore;
	
	public ModelRendModAPart3 () {}
	
	
	public ModelRendModAPart3 (GregDVoceModA voce) {
		this.idVoce = voce.getIdVoceModA();
		this.codVoce = voce.getCodVoceModA();
		this.descVoce = voce.getDesVoceModA();
		this.ordinamento = voce.getOrdinamento();
		this.setMsgInformativo(voce.getMsgInformativo());
	}

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

	public int getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getMsgInformativo() {
		return msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idVoce == null) ? 0 : idVoce.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelRendModAPart3 other = (ModelRendModAPart3) obj;
		if (idVoce == null) {
			if (other.idVoce != null)
				return false;
		} else if (!idVoce.equals(other.idVoce))
			return false;
		return true;
	}


	public String getDescVoce() {
		return descVoce;
	}


	public void setDescVoce(String descVoce) {
		this.descVoce = descVoce;
	}
}