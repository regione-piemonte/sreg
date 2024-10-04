/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDTipoVoce;
import it.csi.greg.gregsrv.business.entity.GregRTipoVoceModD;

public class ModelTipoVoceModD {
	
	private ModelVociD voce;
	private ModelTipoVoce tipoVoce;
	private Boolean dataEntry;
	
	public ModelTipoVoceModD () {}
	
	public ModelTipoVoceModD (GregRTipoVoceModD tipoVoce, GregDTipoVoce tipo) {
		this.voce = new ModelVociD(tipoVoce.getGregDVoceModD());
		this.tipoVoce = new ModelTipoVoce(tipo);
		this.dataEntry = tipoVoce.getDataEntry();
	}

	public ModelVociD getVoce() {
		return voce;
	}

	public void setVoce(ModelVociD voce) {
		this.voce = voce;
	}

	public ModelTipoVoce getTipoVoce() {
		return tipoVoce;
	}

	public void setTipoVoce(ModelTipoVoce tipoVoce) {
		this.tipoVoce = tipoVoce;
	}

	public Boolean getDataEntry() {
		return dataEntry;
	}

	public void setDataEntry(Boolean dataEntry) {
		this.dataEntry = dataEntry;
	}
	
}