/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregDAttivitaSocioAssist;

public class ModelAttivitaSocioAssist {
	
	private Integer idAttivitaSocioAssist;
	private String codAttivitaSocioAssist;
	private String descAttivitaSocioAssist;
	private String tooltipDescAttivitaSocioAssist;
	
	public ModelAttivitaSocioAssist () {}
	
	public ModelAttivitaSocioAssist (GregDAttivitaSocioAssist attivita) {
		this.idAttivitaSocioAssist = attivita.getIdAttivitaSocioAssist();
		this.codAttivitaSocioAssist = attivita.getCodAttivitaSocioAssist();
		this.descAttivitaSocioAssist = attivita.getDescAttivitaSocioAssist().length() > 22? attivita.getDescAttivitaSocioAssist().substring(0, 22) + "[...]" : attivita.getDescAttivitaSocioAssist();
		this.tooltipDescAttivitaSocioAssist = attivita.getDescAttivitaSocioAssist();
	}

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

	public String getTooltipDescAttivitaSocioAssist() {
		return tooltipDescAttivitaSocioAssist;
	}

	public void setTooltipDescAttivitaSocioAssist(String tooltipDescAttivitaSocioAssist) {
		this.tooltipDescAttivitaSocioAssist = tooltipDescAttivitaSocioAssist;
	}
	
}