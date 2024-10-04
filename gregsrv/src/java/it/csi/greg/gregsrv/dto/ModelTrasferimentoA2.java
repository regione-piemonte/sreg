/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneComuneEnteModA2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneEnteComuneModA2;

public class ModelTrasferimentoA2 {
	
	private Integer id;
	private ModelComuniAssociati comune;
	private ModelCausali causale;
	private BigDecimal importo;
	
	public ModelTrasferimentoA2() { }
	
	public ModelTrasferimentoA2(GregRRendicontazioneComuneEnteModA2 trasf) {
		this.id = trasf.getIdRendicontazioneComuneEnteModA2();
		this.comune = new ModelComuniAssociati(trasf.getGregDComuni());
		this.causale = new ModelCausali(trasf.getGregDCausaleComuneEnteModA2());
		this.importo = trasf.getValore();
	}
	
	public ModelTrasferimentoA2(GregRRendicontazioneEnteComuneModA2 trasf) {
		this.id = trasf.getIdRendicontazioneEnteComuneModA2();
		this.comune = new ModelComuniAssociati(trasf.getGregDComuni());
		this.causale = new ModelCausali(trasf.getGregTCausaleEnteComuneModA2());
		this.importo = trasf.getValore();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ModelComuniAssociati getComune() {
		return comune;
	}
	public void setComune(ModelComuniAssociati comune) {
		this.comune = comune;
	}
	public ModelCausali getCausale() {
		return causale;
	}
	public void setCausale(ModelCausali causale) {
		this.causale = causale;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	
	
}
