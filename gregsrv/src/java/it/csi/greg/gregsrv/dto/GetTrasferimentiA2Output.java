/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class GetTrasferimentiA2Output {
	
	private List<ModelTrasferimentoA2> trasferimentiEnteComune;
	private List<ModelTrasferimentoA2> trasferimentiComuneEnte;

	
	public GetTrasferimentiA2Output() { }
	
	public GetTrasferimentiA2Output(List<ModelTrasferimentoA2> trasferimentiEnteComune,
			List<ModelTrasferimentoA2> trasferimentiComuneEnte) {
		this.trasferimentiEnteComune = trasferimentiEnteComune;
		this.trasferimentiComuneEnte = trasferimentiComuneEnte;
	}
	
	
	public List<ModelTrasferimentoA2> getTrasferimentiEnteComune() {
		return trasferimentiEnteComune;
	}
	public void setTrasferimentiEnteComune(List<ModelTrasferimentoA2> trasferimentiEnteComune) {
		this.trasferimentiEnteComune = trasferimentiEnteComune;
	}
	public List<ModelTrasferimentoA2> getTrasferimentiComuneEnte() {
		return trasferimentiComuneEnte;
	}
	public void setTrasferimentiComuneEnte(List<ModelTrasferimentoA2> trasferimentiComuneEnte) {
		this.trasferimentiComuneEnte = trasferimentiComuneEnte;
	}
	
	
}
