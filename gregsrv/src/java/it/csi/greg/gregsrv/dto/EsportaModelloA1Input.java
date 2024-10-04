/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;


import java.util.List;

public class EsportaModelloA1Input {

	private Integer idEnte;
	private List<ModelDatiA1> datiA1;
	private List<ModelVociA1> vociA1;
	private String denominazioneEnte;
	
	public EsportaModelloA1Input() { }

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public List<ModelDatiA1> getDatiA1() {
		return datiA1;
	}

	public void setDatiA1(List<ModelDatiA1> datiA1) {
		this.datiA1 = datiA1;
	}

	public List<ModelVociA1> getVociA1() {
		return vociA1;
	}

	public void setVociA1(List<ModelVociA1> vociA1) {
		this.vociA1 = vociA1;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}
	
}

