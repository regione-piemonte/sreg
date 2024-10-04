/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;


import java.util.List;

public class SaveModelloA1Input {

	private Integer idEnte;
	private Integer idRendicontazioneEnte;
	private List<ModelDatiA1> datiA1;
	private ModelCronologiaEnte cronologia;
	private ModelProfilo profilo;
	
	public SaveModelloA1Input() { }

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

	public List<ModelDatiA1> getDatiA1() {
		return datiA1;
	}

	public void setDatiA1(List<ModelDatiA1> datiA1) {
		this.datiA1 = datiA1;
	}

	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}
	
	
}

