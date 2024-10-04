/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class EsportaModelloA2Input {

	private Integer idEnte;
	private List<ModelCausali> causali;
	private List<ModelTrasferimentoA2> trasferimentiEnteComune;
	private List<ModelTrasferimentoA2> trasferimentiComuneEnte;
	private List<ModelTrasferimentoA2> trasferimentiEnteComuneFiltered;
	private List<ModelTrasferimentoA2> trasferimentiComuneEnteFiltered;
	private String denominazioneEnte;
	
	public EsportaModelloA2Input() { }
	
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

	public List<ModelTrasferimentoA2> getTrasferimentiEnteComuneFiltered() {
		return trasferimentiEnteComuneFiltered;
	}

	public void setTrasferimentiEnteComuneFiltered(List<ModelTrasferimentoA2> trasferimentiEnteComuneFiltered) {
		this.trasferimentiEnteComuneFiltered = trasferimentiEnteComuneFiltered;
	}

	public List<ModelTrasferimentoA2> getTrasferimentiComuneEnteFiltered() {
		return trasferimentiComuneEnteFiltered;
	}

	public void setTrasferimentiComuneEnteFiltered(List<ModelTrasferimentoA2> trasferimentiComuneEnteFiltered) {
		this.trasferimentiComuneEnteFiltered = trasferimentiComuneEnteFiltered;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}
}

