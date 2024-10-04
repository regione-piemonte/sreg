/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRRendMiProTitEnteGestoreModB;

public class ModelRendicontazioneModB {
	
	private Integer idRendicontazioneEnte;
	private Integer idSchedaEnteGestore;
	private List<ModelBMissioni> listaMissioni;
	private ModelProfilo profilo;
	
	public ModelRendicontazioneModB() {
		
	}
	
	public ModelRendicontazioneModB (GregRRendMiProTitEnteGestoreModB rendicontazione) {
		this.idRendicontazioneEnte = rendicontazione.getGregTRendicontazioneEnte().getIdRendicontazioneEnte();
		this.idSchedaEnteGestore = rendicontazione.getGregTRendicontazioneEnte().getGregTSchedeEntiGestori().getIdSchedaEnteGestore();
		this.listaMissioni = new ArrayList<ModelBMissioni>();
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


	public List<ModelBMissioni> getListaMissioni() {
		return listaMissioni;
	}

	public void setListaMissioni(List<ModelBMissioni> listaMissioni) {
		this.listaMissioni = listaMissioni;
	}

	public ModelProfilo getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}

}