/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregDTitoloModA;

public class ModelTitoloModA {
	
	private Integer idTitolo;
	private String codTitolo;
	private String descTitolo;
	private int ordinamento;
	private String msgInformativo;
	private List<ModelTipologiaModA> listaTipologie;
	
	public ModelTitoloModA () {}
	
	public ModelTitoloModA (GregDTitoloModA titolo) {
		this.idTitolo = titolo.getIdTitoloModA();
		this.codTitolo = titolo.getCodTitoloModA();
		this.descTitolo = titolo.getDesTitoloModA();
		this.ordinamento = titolo.getOrdinamento();
		this.setMsgInformativo(titolo.getMsgInformativo());
	}


	public Integer getIdTitolo() {
		return idTitolo;
	}

	public void setIdTitolo(Integer idTitolo) {
		this.idTitolo = idTitolo;
	}

	public String getCodTitolo() {
		return codTitolo;
	}

	public void setCodTitolo(String codTitolo) {
		this.codTitolo = codTitolo;
	}

	public String getDescTitolo() {
		return descTitolo;
	}

	public void setDescTitolo(String descTitolo) {
		this.descTitolo = descTitolo;
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

	public List<ModelTipologiaModA> getListaTipologie() {
		return listaTipologie;
	}

	public void setListaTipologie(List<ModelTipologiaModA> listaTipologie) {
		this.listaTipologie = listaTipologie;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTitolo == null) ? 0 : idTitolo.hashCode());
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
		ModelTitoloModA other = (ModelTitoloModA) obj;
		if (idTitolo == null) {
			if (other.idTitolo != null)
				return false;
		} else if (!idTitolo.equals(other.idTitolo))
			return false;
		return true;
	}
}