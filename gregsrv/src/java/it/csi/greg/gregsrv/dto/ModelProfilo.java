/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class ModelProfilo implements Serializable {
	private String codProfilo;
	private String descProfilo;
	private String infoProfilo;
	private String tipoProfilo;
	private ArrayList<ModelLista> listaenti;
	private Map<String, boolean[]> listaazioni;
	
	public ModelProfilo () {}

	public String getCodProfilo() {
		return codProfilo;
	}

	public void setCodProfilo(String codProfilo) {
		this.codProfilo = codProfilo;
	}

	public String getDescProfilo() {
		return descProfilo;
	}

	public void setDescProfilo(String descProfilo) {
		this.descProfilo = descProfilo;
	}

	public String getInfoProfilo() {
		return infoProfilo;
	}

	public void setInfoProfilo(String infoProfilo) {
		this.infoProfilo = infoProfilo;
	}

	public String getTipoProfilo() {
		return tipoProfilo;
	}

	public void setTipoProfilo(String tipoProfilo) {
		this.tipoProfilo = tipoProfilo;
	}

	public ArrayList<ModelLista> getListaenti() {
		return listaenti;
	}

	public void setListaenti(ArrayList<ModelLista> listaenti) {
		this.listaenti = listaenti;
	}

	public Map<String, boolean[]> getListaazioni() {
		return listaazioni;
	}

	public void setListaazioni(Map<String, boolean[]> listaazioni) {
		this.listaazioni = listaazioni;
	}
	
}
