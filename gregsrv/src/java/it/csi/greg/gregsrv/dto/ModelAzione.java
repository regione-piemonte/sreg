/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.Map;

public class ModelAzione {
//	private String codAzione;
//	private boolean disabilitato;
//	private boolean visibile;
	public Map<String, boolean[]> azioni;
	
	public ModelAzione () {}

	public Map<String, boolean[]> getAzioni() {
		return azioni;
	}

	public void setAzioni(Map<String, boolean[]> azioni) {
		this.azioni = azioni;
	}

	
//	public String getCodAzione() {
//		return codAzione;
//	}
//
//	public void setCodAzione(String codAzione) {
//		this.codAzione = codAzione;
//	}
//
//	public boolean isDisabilitato() {
//		return disabilitato;
//	}
//
//	public void setDisabilitato(boolean disabilitato) {
//		this.disabilitato = disabilitato;
//	}
//
//	public boolean isVisibile() {
//		return visibile;
//	}
//
//	public void setVisibile(boolean visibile) {
//		this.visibile = visibile;
//	}
//	
}
