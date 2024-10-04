/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class ModelFConteggi {
	ModelFConteggioPersonale conteggioPersonale;
	ModelFConteggioOre conteggioOre;
	
	public ModelFConteggi () {}

	public ModelFConteggioPersonale getConteggioPersonale() {
		return conteggioPersonale;
	}

	public void setConteggioPersonale(ModelFConteggioPersonale conteggioPersonale) {
		this.conteggioPersonale = conteggioPersonale;
	}

	public ModelFConteggioOre getConteggioOre() {
		return conteggioOre;
	}

	public void setConteggioOre(ModelFConteggioOre conteggioOre) {
		this.conteggioOre = conteggioOre;
	}
	
	
	
	
}
