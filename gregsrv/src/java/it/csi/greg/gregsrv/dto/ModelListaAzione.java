/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRProfiloAzione;
import it.csi.greg.gregsrv.business.entity.GregRUserProfilo;
import it.csi.greg.gregsrv.business.entity.GregTUser;

public class ModelListaAzione {
	
	private Integer idProfiloAzione;
	private Integer idAzione;
	private String codAzione;
	private String descAzione;
	private String stato;
	
	public ModelListaAzione () {}

	public ModelListaAzione (GregRProfiloAzione pa) {
		this.idProfiloAzione = pa.getIdProfiloAzione();
		this.idAzione = pa.getGregDAzione().getIdAzione();
		this.codAzione = pa.getGregDAzione().getCodAzione();
		this.descAzione = pa.getGregDAzione().getDescAzione();
	}

	public Integer getIdProfiloAzione() {
		return idProfiloAzione;
	}

	public void setIdProfiloAzione(Integer idProfiloAzione) {
		this.idProfiloAzione = idProfiloAzione;
	}

	public Integer getIdAzione() {
		return idAzione;
	}


	public void setIdAzione(Integer idAzione) {
		this.idAzione = idAzione;
	}


	public String getCodAzione() {
		return codAzione;
	}


	public void setCodAzione(String codAzione) {
		this.codAzione = codAzione;
	}


	public String getDescAzione() {
		return descAzione;
	}


	public void setDescAzione(String descAzione) {
		this.descAzione = descAzione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
	
	
}

	