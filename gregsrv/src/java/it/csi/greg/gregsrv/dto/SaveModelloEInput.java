/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class SaveModelloEInput {

	private Integer idEnte;
	private Integer idRendicontazioneEnte;
	private List<ModelComuniAttivitaValoriModE> listaRendicontazione;
	private ModelCronologiaEnte cronologia;
	private ModelProfilo profilo;
	
	public SaveModelloEInput() { }

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public List<ModelComuniAttivitaValoriModE> getListaRendicontazione() {
		return listaRendicontazione;
	}

	public void setListaRendicontazione(List<ModelComuniAttivitaValoriModE> listaRendicontazione) {
		this.listaRendicontazione = listaRendicontazione;
	}

	public ModelCronologiaEnte getCronologia() {
		return cronologia;
	}

	public void setCronologia(ModelCronologiaEnte cronologia) {
		this.cronologia = cronologia;
	}

	public ModelProfilo getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}

	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}

}

