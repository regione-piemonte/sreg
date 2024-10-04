/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModD;

public class ModelRendicontazioneModE {
	
	private Integer idRendicontazioneEnte;
	private Integer idSchedaEnteGestore;
	private String denominazioneEnte;
	private Integer annoGestione;
	private List<ModelComuniAttivitaValoriModE> comuniAttivitaValori;
	
	public ModelRendicontazioneModE () {}
	
//	public ModelRendicontazioneModE (GregRRendicontazioneModD rendicontazione) {
//		this.idRendicontazioneEnte = rendicontazione.getGregTRendicontazioneEnte().getIdRendicontazioneEnte();
//		this.idSchedaEnteGestore = rendicontazione.getGregTRendicontazioneEnte().getGregTSchedeEntiGestori().getIdSchedaEnteGestore();
//		this.denominazioneEnte = rendicontazione.getGregTRendicontazioneEnte().getGregTSchedeEntiGestori().getDenominazione();
//		this.annoGestione = rendicontazione.getGregTRendicontazioneEnte().getAnnoGestione();
//		this.comuniAttivitaValori = new ArrayList<ModelComuniAttivitaValoriModE>();
//	}

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

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

	public Integer getAnnoGestione() {
		return annoGestione;
	}

	public void setAnnoGestione(Integer annoGestione) {
		this.annoGestione = annoGestione;
	}

	public List<ModelComuniAttivitaValoriModE> getComuniAttivitaValori() {
		return comuniAttivitaValori;
	}

	public void setComuniAttivitaValori(List<ModelComuniAttivitaValoriModE> comuniAttivitaValori) {
		this.comuniAttivitaValori = comuniAttivitaValori;
	}

}