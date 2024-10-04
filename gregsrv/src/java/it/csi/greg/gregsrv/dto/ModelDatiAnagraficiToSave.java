/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;


public class ModelDatiAnagraficiToSave {
	
	private ModelDatiAnagrafici datiEnte;
	private List<ModelComuniAssociati> comuniAssociati;
	private boolean modificaEnte;
	private boolean modificaResp;
	private boolean modificaComune;
	private ModelProfilo profilo;
	private Date dataModifica;
	private Date dataApertura;
	private ModelLista listaSelezionata;
	private boolean sameData;
	
	public ModelDatiAnagraficiToSave() { }

	public ModelDatiAnagrafici getDatiEnte() {
		return datiEnte;
	}

	public void setDatiEnte(ModelDatiAnagrafici datiEnte) {
		this.datiEnte = datiEnte;
	}

	public List<ModelComuniAssociati> getComuniAssociati() {
		return comuniAssociati;
	}

	public void setComuniAssociati(List<ModelComuniAssociati> comuniAssociati) {
		this.comuniAssociati = comuniAssociati;
	}

	public boolean isModificaEnte() {
		return modificaEnte;
	}

	public void setModificaEnte(boolean modificaEnte) {
		this.modificaEnte = modificaEnte;
	}

	public boolean isModificaResp() {
		return modificaResp;
	}

	public void setModificaResp(boolean modificaResp) {
		this.modificaResp = modificaResp;
	}

	public boolean isModificaComune() {
		return modificaComune;
	}

	public void setModificaComune(boolean modificaComune) {
		this.modificaComune = modificaComune;
	}

	public ModelProfilo getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}


	public Date getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(Date dataModifica) {
		this.dataModifica = dataModifica;
	}

	public Date getDataApertura() {
		return dataApertura;
	}

	public void setDataApertura(Date dataApertura) {
		this.dataApertura = dataApertura;
	}

	public ModelLista getListaSelezionata() {
		return listaSelezionata;
	}

	public void setListaSelezionata(ModelLista listaSelezionata) {
		this.listaSelezionata = listaSelezionata;
	}

	public boolean isSameData() {
		return sameData;
	}

	public void setSameData(boolean sameData) {
		this.sameData = sameData;
	}

}
