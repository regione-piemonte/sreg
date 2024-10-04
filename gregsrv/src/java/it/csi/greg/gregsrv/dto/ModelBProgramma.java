/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregDProgrammaMissione;

public class ModelBProgramma {
	private Integer idProgramma;
	private String codProgramma;
	private String descProgramma;
	private String informativa;
	private String colore;
	private String msgInformativo;
	private String motivazione;
	private boolean flagConf;
	private List<ModelBTitolo> listaTitolo;
	
	public ModelBProgramma () {}
	
	public ModelBProgramma (GregDProgrammaMissione programma) {
		this.idProgramma = programma.getIdProgrammaMissione();
		this.codProgramma = programma.getCodProgrammaMissione();
		this.descProgramma = programma.getDesProgrammaMissione();
		this.informativa = programma.getInformativa();
		this.msgInformativo=programma.getMsgInformativo();
		this.listaTitolo = new ArrayList<ModelBTitolo>();
	}

	public void setIdProgramma(Integer idProgramma) {
		this.idProgramma = idProgramma;
	}

	public String getCodProgramma() {
		return codProgramma;
	}

	public void setCodProgramma(String codProgramma) {
		this.codProgramma = codProgramma;
	}

	public String getDescProgramma() {
		return descProgramma;
	}

	public void setDescProgramma(String descProgramma) {
		this.descProgramma = descProgramma;
	}
	
	public List<ModelBTitolo> getListaTitolo() {
		return listaTitolo;
	}

	public void setListaTitolo(List<ModelBTitolo> listaTitolo) {
		this.listaTitolo = listaTitolo;
	}

	public String getInformativa() {
		return informativa;
	}

	public void setInformativa(String informativa) {
		this.informativa = informativa;
	}

	public Integer getIdProgramma() {
		return idProgramma;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public String getMsgInformativo() {
		return msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}

	public boolean isFlagConf() {
		return flagConf;
	}

	public void setFlagConf(boolean flagConf) {
		this.flagConf = flagConf;
	}
	
	
	
}
