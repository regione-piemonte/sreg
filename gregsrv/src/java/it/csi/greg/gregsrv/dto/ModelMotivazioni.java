/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDMotivazione;

public class ModelMotivazioni {
	private String codMotivazione;
	private String descMotivazione;

	//altri modelli
	public ModelMotivazioni () {}

	public ModelMotivazioni(GregDMotivazione motivazione) {
		super();
		this.codMotivazione = motivazione.getCodMotivazione();
		this.descMotivazione = motivazione.getDescMotivazione();
	}

	public String getCodMotivazione() {
		return codMotivazione;
	}

	public void setCodMotivazione(String codMotivazione) {
		this.codMotivazione = codMotivazione;
	}

	public String getDescMotivazione() {
		return descMotivazione;
	}

	public void setDescMotivazione(String descMotivazione) {
		this.descMotivazione = descMotivazione;
	}
	
	
	
}
