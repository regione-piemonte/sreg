/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregRRendMiProTitEnteGestoreModB;

public class ModelRendicontazioneModF {
	
	private Integer idRendicontazioneEnte;
	private Integer idSchedaEnteGestore;
	private ModelFConteggi conteggi;
	private ModelProfilo profilo;
	
	
	public ModelRendicontazioneModF() {
		
	}
	
	public ModelRendicontazioneModF (GregRRendMiProTitEnteGestoreModB rendicontazione) {
		this.idRendicontazioneEnte = rendicontazione.getGregTRendicontazioneEnte().getIdRendicontazioneEnte();
		this.idSchedaEnteGestore = rendicontazione.getGregTRendicontazioneEnte().getGregTSchedeEntiGestori().getIdSchedaEnteGestore();
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

	public ModelFConteggi getConteggi() {
		return conteggi;
	}

	public void setConteggi(ModelFConteggi conteggi) {
		this.conteggi = conteggi;
	}

	public ModelProfilo getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}
	
}