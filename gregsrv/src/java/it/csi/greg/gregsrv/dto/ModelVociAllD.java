/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelVociAllD {
//	private List<String> aree;
	private List<Aree> aree;
	private List<ModelUtenzaAllD> listaTargetUtenze;
	private List<ModelPrestazioneAllD> listaPrestazione;
	
	public ModelVociAllD () {}

	public List<Aree> getAree() {
		return aree;
	}

	public void setAree(List<Aree> aree) {
		this.aree = aree;
	}

	public List<ModelUtenzaAllD> getListaTargetUtenze() {
		return listaTargetUtenze;
	}

	public void setListaTargetUtenze(List<ModelUtenzaAllD> listaTargetUtenze) {
		this.listaTargetUtenze = listaTargetUtenze;
	}

	public List<ModelPrestazioneAllD> getListaPrestazione() {
		return listaPrestazione;
	}

	public void setListaPrestazione(List<ModelPrestazioneAllD> listaPrestazione) {
		this.listaPrestazione = listaPrestazione;
	}
	
}
