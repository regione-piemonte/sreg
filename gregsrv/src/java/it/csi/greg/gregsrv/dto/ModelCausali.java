/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregTCausaleEnteComuneModA2;
import it.csi.greg.gregsrv.business.entity.GregDCausaleComuneEnteModA2;

public class ModelCausali {
	
	private Integer id;
	private String descrizione;
	
	
	public ModelCausali () {}
	
	public ModelCausali(GregTCausaleEnteComuneModA2 causale) {
		this.id = causale.getIdCausaleEnteComuneModA2();
		this.descrizione = causale.getDescCausaleEnteComuneModA2();
	}
	
	public ModelCausali(GregDCausaleComuneEnteModA2 causale) {
		this.id = causale.getIdCausaleComuneEnteModA2();
		this.descrizione = causale.getDescCausaleComuneEnteModA2();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	

	
	
}