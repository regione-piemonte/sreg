/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRListaEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregRProfiloAzione;
import it.csi.greg.gregsrv.business.entity.GregRUserProfilo;
import it.csi.greg.gregsrv.business.entity.GregTUser;

public class ModelListaEnti {

	private Integer idListaEnte;
	private Integer idEnte;
	private String codEnte;
	private String descEnte;
	private String stato;

	public ModelListaEnti() {
	}

	public ModelListaEnti(GregRListaEntiGestori pa) {
		this.idListaEnte = pa.getIdListaEntiGestori();
		this.idEnte = pa.getGregTSchedeEntiGestori().getIdSchedaEnteGestore();
		this.codEnte = pa.getGregTSchedeEntiGestori().getCodiceRegionale();
		this.descEnte = pa.getGregTSchedeEntiGestori().getGregREnteGestoreContattis().stream()
				.filter((c) -> c.getDataFineValidita() == null).findFirst().get().getDenominazione();
	}

	public Integer getIdListaEnte() {
		return idListaEnte;
	}

	public void setIdListaEnte(Integer idListaEnte) {
		this.idListaEnte = idListaEnte;
	}

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public String getCodEnte() {
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}

	public String getDescEnte() {
		return descEnte;
	}

	public void setDescEnte(String descEnte) {
		this.descEnte = descEnte;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

}
