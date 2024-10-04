/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import it.csi.greg.gregsrv.business.entity.GregDAsl;
import it.csi.greg.gregsrv.business.entity.GregDProvince;
import it.csi.greg.gregsrv.business.entity.GregRSchedeEntiGestoriComuni;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.util.Checker;

public class ModelDatiEnteRendicontazione {
	
	private String denominazione;
	private String codTipoEnte;

	
	public ModelDatiEnteRendicontazione() { }
	
	public ModelDatiEnteRendicontazione(Object[] obj) {
		this.denominazione =  String.valueOf(obj[0]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[0]);
		this.codTipoEnte = String.valueOf(obj[1]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[1]);
	}
	
		public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodTipoEnte() {
		return codTipoEnte;
	}

	public void setCodTipoEnte(String codTipoEnte) {
		this.codTipoEnte = codTipoEnte;
	}
}