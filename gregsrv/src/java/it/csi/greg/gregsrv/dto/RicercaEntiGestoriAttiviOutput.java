/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class RicercaEntiGestoriAttiviOutput {
	
	List<ModelRicercaEntiGestoriAttivi> data;

	public List<ModelRicercaEntiGestoriAttivi> getData() {
		return data;
	}

	public void setData(List<ModelRicercaEntiGestoriAttivi> data) {
		this.data = data;
	}

}
