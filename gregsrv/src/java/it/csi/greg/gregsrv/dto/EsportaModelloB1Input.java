/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class EsportaModelloB1Input {

	private Integer idEnte;
	private List<ModelB1Voci> datiB1;
	private ModelB1ElencoLbl labels;
	private String denominazioneEnte;
	
	public EsportaModelloB1Input() { }

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public List<ModelB1Voci> getDatiB1() {
		return datiB1;
	}

	public void setDatiB1(List<ModelB1Voci> datiB1) {
		this.datiB1 = datiB1;
	}

	public ModelB1ElencoLbl getLabels() {
		return labels;
	}

	public void setLabels(ModelB1ElencoLbl labels) {
		this.labels = labels;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}
	
}

