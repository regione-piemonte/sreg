/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;


public class VociModelloD {
	
	private String sezioneModello;
	private List<ModelVociD> listaVoci;
	
	public VociModelloD () {}

	public String getSezioneModello() {
		return sezioneModello;
	}

	public void setSezioneModello(String sezioneModello) {
		this.sezioneModello = sezioneModello;
	}

	public List<ModelVociD> getListaVoci() {
		return listaVoci;
	}

	public void setListaVoci(List<ModelVociD> listaVoci) {
		this.listaVoci = listaVoci;
	}
	
}