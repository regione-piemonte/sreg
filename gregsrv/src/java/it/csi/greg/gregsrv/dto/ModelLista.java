/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelLista  implements Serializable  {
	private String codLista;
	private String descLista;
	private ArrayList<Integer> idente;
	
	public ModelLista () {}

	public String getCodLista() {
		return codLista;
	}

	public void setCodLista(String codLista) {
		this.codLista = codLista;
	}

	public String getDescLista() {
		return descLista;
	}

	public void setDescLista(String descLista) {
		this.descLista = descLista;
	}

	public ArrayList<Integer> getIdente() {
		return idente;
	}

	public void setIdente(ArrayList<Integer> idente) {
		this.idente = idente;
	}
	
}
