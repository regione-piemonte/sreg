/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Date;
import java.util.List;

public class RicercaListaEntiDaunire {

	private boolean statoEnte;
	private List<Integer> lista;
	private Date dataMerge;
	private String codregionale;
	
	public boolean isStatoEnte() {
		return statoEnte;
	}
	public void setStatoEnte(boolean statoEnte) {
		this.statoEnte = statoEnte;
	}
	public List<Integer> getLista() {
		return lista;
	}
	public void setLista(List<Integer> lista) {
		this.lista = lista;
	}
	public Date getDataMerge() {
		return dataMerge;
	}
	public void setDataMerge(Date dataMerge) {
		this.dataMerge = dataMerge;
	}
	public String getCodregionale() {
		return codregionale;
	}
	public void setCodregionale(String codregionale) {
		this.codregionale = codregionale;
	}

}
