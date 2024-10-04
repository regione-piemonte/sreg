/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;


public class VociModelloA {
	
	private List<ModelTitoloModA> listaTitoli;
	private List<ModelRendModAPart3> listaVociModAPart3;

	public VociModelloA () {}

	public List<ModelTitoloModA> getListaTitoli() {
		return listaTitoli;
	}

	public void setListaTitoli(List<ModelTitoloModA> listaTitoli) {
		this.listaTitoli = listaTitoli;
	}

	public List<ModelRendModAPart3> getListaVociModAPart3() {
		return listaVociModAPart3;
	}

	public void setListaVociModAPart3(List<ModelRendModAPart3> listaVociModAPart3) {
		this.listaVociModAPart3 = listaVociModAPart3;
	}

}