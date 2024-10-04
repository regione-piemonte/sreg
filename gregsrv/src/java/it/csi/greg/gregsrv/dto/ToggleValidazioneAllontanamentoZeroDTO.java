/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class ToggleValidazioneAllontanamentoZeroDTO {
	
	private Integer idRendicontazioneEnte;
	private Boolean toggle;
	private ModelCronologiaEnte cronologia;
	private ModelProfilo profilo; 
	private String notaEnte;
	private String notaInterna;
	private String modello;

	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}

	public Boolean getToggle() {
		return toggle;
	}

	public void setToggle(Boolean toggle) {
		this.toggle = toggle;
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

	@Override
	public String toString() {
		return "ToggleValidazioneAllontanamentoZeroDTO [idRendicontazioneEnte="
				+ idRendicontazioneEnte
				+ ", toggle="
				+ toggle
				+ "]";
	}

	public String getNotaEnte() {
		return notaEnte;
	}

	public void setNotaEnte(String notaEnte) {
		this.notaEnte = notaEnte;
	}

	public String getNotaInterna() {
		return notaInterna;
	}

	public void setNotaInterna(String notaInterna) {
		this.notaInterna = notaInterna;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}
	
	
}
