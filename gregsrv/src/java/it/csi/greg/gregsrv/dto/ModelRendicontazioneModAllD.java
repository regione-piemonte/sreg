/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRRendMiProTitEnteGestoreModB;

public class ModelRendicontazioneModAllD {
	
	private Integer idRendicontazioneEnte;
	private Integer idSchedaEnteGestore;
	private ModelVociAllD vociAllD;
	private String giustificazione;
	private boolean giustificazionePerInf;
	private ModelProfilo profilo;
	private List<ModelFondi> azioniSistema;
	private List<ModelFondi> quote;
	private boolean azzeramentoGiustificativo;
	
	public ModelRendicontazioneModAllD() {
		
	}
	
	public ModelRendicontazioneModAllD (GregRRendMiProTitEnteGestoreModB rendicontazione) {
		this.idRendicontazioneEnte = rendicontazione.getGregTRendicontazioneEnte().getIdRendicontazioneEnte();
		this.idSchedaEnteGestore = rendicontazione.getGregTRendicontazioneEnte().getGregTSchedeEntiGestori().getIdSchedaEnteGestore();
		this.vociAllD = new ModelVociAllD();
	}
	
	
	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}

	public Integer getIdSchedaEnteGestore() {
		return idSchedaEnteGestore;
	}

	public void setIdSchedaEnteGestore(Integer idSchedaEnteGestore) {
		this.idSchedaEnteGestore = idSchedaEnteGestore;
	}

	public ModelVociAllD getVociAllD() {
		return vociAllD;
	}

	public void setVociAllD(ModelVociAllD vociAllD) {
		this.vociAllD = vociAllD;
	}

	public ModelProfilo getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}

	public String getGiustificazione() {
		return giustificazione;
	}

	public void setGiustificazione(String giustificazione) {
		this.giustificazione = giustificazione;
	}

	public boolean isGiustificazionePerInf() {
		return giustificazionePerInf;
	}

	public void setGiustificazionePerInf(boolean giustificazionePerInf) {
		this.giustificazionePerInf = giustificazionePerInf;
	}

	public List<ModelFondi> getAzioniSistema() {
		return azioniSistema;
	}

	public void setAzioniSistema(List<ModelFondi> azioniSistema) {
		this.azioniSistema = azioniSistema;
	}

	public boolean isAzzeramentoGiustificativo() {
		return azzeramentoGiustificativo;
	}

	public void setAzzeramentoGiustificativo(boolean azzeramentoGiustificativo) {
		this.azzeramentoGiustificativo = azzeramentoGiustificativo;
	}

	public List<ModelFondi> getQuote() {
		return quote;
	}

	public void setQuote(List<ModelFondi> quote) {
		this.quote = quote;
	}

	
}