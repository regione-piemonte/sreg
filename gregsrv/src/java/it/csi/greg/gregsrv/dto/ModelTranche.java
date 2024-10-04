/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class ModelTranche {

	private String codTranche;
	private String desTranche;
	
	public ModelTranche () {}


	public String getCodTranche() {
		return codTranche;
	}

	public void setCodTranche(String codTranche) {
		this.codTranche = codTranche;
	}

	public String getDesTranche() {
		return desTranche;
	}

	public void setDesTranche(String desTranche) {
		this.desTranche = desTranche;
	}
	
}
