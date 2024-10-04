/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class SaveModelloA2Input {

	private Integer idEnte;
	private Integer idRendicontazioneEnte;
	private List<ModelCausali> causali;
	private List<ModelTrasferimentoA2> trasferimentiEnteComune;
	private List<ModelTrasferimentoA2> trasferimentiComuneEnte;
	private ModelCronologiaEnte cronologia;
	private ModelProfilo profilo;
	
	public SaveModelloA2Input() { }
	
	public List<ModelCausali> getCausali() {
		return causali;
	}
	public void setCausali(List<ModelCausali> causali) {
		this.causali = causali;
	}
	public List<ModelTrasferimentoA2> getTrasferimentiEnteComune() {
		return trasferimentiEnteComune;
	}
	public void setTrasferimentiEnteComune(List<ModelTrasferimentoA2> trasferimentiEnteComune) {
		this.trasferimentiEnteComune = trasferimentiEnteComune;
	}
	public List<ModelTrasferimentoA2> getTrasferimentiComuneEnte() {
		return trasferimentiComuneEnte;
	}
	public void setTrasferimentiComuneEnte(List<ModelTrasferimentoA2> trasferimentiComuneEnte) {
		this.trasferimentiComuneEnte = trasferimentiComuneEnte;
	}

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
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

