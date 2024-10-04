/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class EsportaModelloDInput {

	private Integer idEnte;
	private ModelRendicontazioneModD datiD;
	private List<ModelTipoVoce> vociD;
	
	public EsportaModelloDInput() { }

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public ModelRendicontazioneModD getDatiD() {
		return datiD;
	}

	public void setDatiD(ModelRendicontazioneModD datiD) {
		this.datiD = datiD;
	}

	public List<ModelTipoVoce> getVociD() {
		return vociD;
	}

	public void setVociD(List<ModelTipoVoce> vociD) {
		this.vociD = vociD;
	}

	
}

