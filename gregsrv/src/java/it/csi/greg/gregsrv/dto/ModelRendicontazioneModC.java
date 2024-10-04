/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRRendMiProTitEnteGestoreModB;

public class ModelRendicontazioneModC {
	
	private Integer idRendicontazioneEnte;
	private Integer idSchedaEnteGestore;
	private List<ModelCPrestazioni> listaPrestazioni;
	private ModelProfilo profilo;
	
	public ModelRendicontazioneModC() {
		
	}
	
	public ModelRendicontazioneModC (GregRRendMiProTitEnteGestoreModB rendicontazione) {
		this.idRendicontazioneEnte = rendicontazione.getGregTRendicontazioneEnte().getIdRendicontazioneEnte();
		this.idSchedaEnteGestore = rendicontazione.getGregTRendicontazioneEnte().getGregTSchedeEntiGestori().getIdSchedaEnteGestore();
		this.listaPrestazioni = new ArrayList<ModelCPrestazioni>();
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


	public List<ModelCPrestazioni> getListaPrestazioni() {
		return listaPrestazioni;
	}

	public void setListaPrestazioni(List<ModelCPrestazioni> listaPrestazioni) {
		this.listaPrestazioni = listaPrestazioni;
	}

	public ModelProfilo getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}
	
}