/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class ModelRendicontazioneAttivitaModE {
	
	private Integer idAttivitaSocioAssist;
	private String codAttivitaSocioAssist;
	private String descAttivitaSocioAssist;
	private BigDecimal valore;
	
	public ModelRendicontazioneAttivitaModE () {}

	public Integer getIdAttivitaSocioAssist() {
		return idAttivitaSocioAssist;
	}

	public void setIdAttivitaSocioAssist(Integer idAttivitaSocioAssist) {
		this.idAttivitaSocioAssist = idAttivitaSocioAssist;
	}

	public String getCodAttivitaSocioAssist() {
		return codAttivitaSocioAssist;
	}

	public void setCodAttivitaSocioAssist(String codAttivitaSocioAssist) {
		this.codAttivitaSocioAssist = codAttivitaSocioAssist;
	}

	public String getDescAttivitaSocioAssist() {
		return descAttivitaSocioAssist;
	}

	public void setDescAttivitaSocioAssist(String descAttivitaSocioAssist) {
		this.descAttivitaSocioAssist = descAttivitaSocioAssist;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}
	
}