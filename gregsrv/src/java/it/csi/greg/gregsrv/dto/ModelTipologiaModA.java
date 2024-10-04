/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregDTipologiaModA;

public class ModelTipologiaModA {
	
	private Integer idTipologia;
	private String codTipologia;
	private String descCodTipologia;
	private String descTipologia;
	private int ordinamento;
	private String msgInformativo;
	BigDecimal valore;

	private List<ModelVoceModA> listaVoci;
	
	public ModelTipologiaModA () {}
	
	public ModelTipologiaModA (GregDTipologiaModA tipologia) {
		this.idTipologia = tipologia.getIdTipologiaModA();
		this.codTipologia = tipologia.getCodTipologiaModA();
		this.descCodTipologia = tipologia.getDesCodTipologiaModA();
		this.descTipologia = tipologia.getDesTipologiaModA();
		this.ordinamento = tipologia.getOrdinamento();
		this.setMsgInformativo(tipologia.getMsgInformativo());
	}

	public Integer getIdTipologia() {
		return idTipologia;
	}

	public void setIdTipologia(Integer idTipologia) {
		this.idTipologia = idTipologia;
	}

	public String getCodTipologia() {
		return codTipologia;
	}

	public void setCodTipologia(String codTipologia) {
		this.codTipologia = codTipologia;
	}

	public String getDescCodTipologia() {
		return descCodTipologia;
	}

	public void setDescCodTipologia(String descCodTipologia) {
		this.descCodTipologia = descCodTipologia;
	}

	public String getDescTipologia() {
		return descTipologia;
	}

	public void setDescTipologia(String descTipologia) {
		this.descTipologia = descTipologia;
	}

	public int getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}

	public List<ModelVoceModA> getListaVoci() {
		return listaVoci;
	}

	public void setListaVoci(List<ModelVoceModA> listaVoci) {
		this.listaVoci = listaVoci;
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
		result = prime * result + ((idTipologia == null) ? 0 : idTipologia.hashCode());
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
		ModelTipologiaModA other = (ModelTipologiaModA) obj;
		if (idTipologia == null) {
			if (other.idTipologia != null)
				return false;
		} else if (!idTipologia.equals(other.idTipologia))
			return false;
		return true;
	}
	
	
}