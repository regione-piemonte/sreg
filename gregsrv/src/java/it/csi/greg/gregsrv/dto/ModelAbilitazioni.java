/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRUserProfilo;
import it.csi.greg.gregsrv.business.entity.GregTUser;
import it.csi.greg.gregsrv.util.SharedConstants;

public class ModelAbilitazioni {
	
	private Integer idUserProfilo;
	private ModelListeConfiguratore profilo;
	private ModelListeConfiguratore lista;
	private Timestamp dataInizioValidita;
	private Timestamp dataFineValidita;
	private String stato;
	
	public ModelAbilitazioni () {}
	
	public ModelAbilitazioni (GregRUserProfilo userProfilo) {
		this.idUserProfilo = userProfilo.getIdUserProfilo();
		this.profilo = new ModelListeConfiguratore();
		this.profilo.setId(userProfilo.getGregDProfilo().getIdProfilo());
		this.profilo.setCodice(userProfilo.getGregDProfilo().getCodProfilo());
		this.profilo.setDescrizione(userProfilo.getGregDProfilo().getDescProfilo());
		this.lista = new ModelListeConfiguratore();
		this.lista.setId(userProfilo.getGregTLista().getIdLista());
		this.lista.setCodice(userProfilo.getGregTLista().getCodLista());
		this.lista.setDescrizione(userProfilo.getGregTLista().getDescLista());
		this.dataInizioValidita = userProfilo.getDataInizioValidita();
		this.dataFineValidita = userProfilo.getDataFineValidita();
		this.stato = SharedConstants.ESISTENTE;
	}

	public Integer getIdUserProfilo() {
		return idUserProfilo;
	}

	public void setIdUserProfilo(Integer idUserProfilo) {
		this.idUserProfilo = idUserProfilo;
	}

	public ModelListeConfiguratore getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelListeConfiguratore profilo) {
		this.profilo = profilo;
	}

	public ModelListeConfiguratore getLista() {
		return lista;
	}

	public void setLista(ModelListeConfiguratore lista) {
		this.lista = lista;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Timestamp getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Timestamp dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public Timestamp getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Timestamp dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	
}