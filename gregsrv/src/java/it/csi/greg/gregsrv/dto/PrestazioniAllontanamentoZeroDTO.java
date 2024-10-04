/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class PrestazioniAllontanamentoZeroDTO {
		
	private Integer prestazioneId;
	private String prestazioneCod;
	private String prestazioneDesc;
	private BigDecimal valorePerPrestazioneAlZero;
	private BigDecimal valorePerFamiglieMinoriB1;
	private String tooltipDesc;
	
//	public PrestazioniAllontanamentoZeroDTO(Object[] o) {		
//		this.prestazioneId = (Integer) o[0];
//		this.prestazioneCod = (String) o[1];
//		this.prestazioneDesc = (String) o[2];
//		this.valorePerPrestazioneAlZero = (BigDecimal) o[3];
//		this.valorePerFamiglieMinoriB1 = (BigDecimal) o[4];
//	}

	public Integer getPrestazioneId() {
		return prestazioneId;
	}

	public void setPrestazioneId(Integer prestazioneId) {
		this.prestazioneId = prestazioneId;
	}

	public String getPrestazioneCod() {
		return prestazioneCod;
	}

	public void setPrestazioneCod(String prestazioneCod) {
		this.prestazioneCod = prestazioneCod;
	}

	public String getPrestazioneDesc() {
		return prestazioneDesc;
	}

	public void setPrestazioneDesc(String prestazioneDesc) {
		this.prestazioneDesc = prestazioneDesc;
	}

	public BigDecimal getValorePerPrestazioneAlZero() {
		return valorePerPrestazioneAlZero;
	}

	public void setValorePerPrestazioneAlZero(BigDecimal valorePerPrestazioneAlZero) {
		this.valorePerPrestazioneAlZero = valorePerPrestazioneAlZero;
	}

	public BigDecimal getValorePerFamiglieMinoriB1() {
		return valorePerFamiglieMinoriB1;
	}

	public void setValorePerFamiglieMinoriB1(BigDecimal valorePerFamiglieMinoriB1) {
		this.valorePerFamiglieMinoriB1 = valorePerFamiglieMinoriB1;
	}
	
	

	public String getTooltipDesc() {
		return tooltipDesc;
	}

	public void setTooltipDesc(String tooltipDesc) {
		this.tooltipDesc = tooltipDesc;
	}

	@Override
	public String toString() {
		return "PrestazioniAllontanamentoZeroDTO [prestazioneId="
				+ prestazioneId
				+ ", prestazioneCod="
				+ prestazioneCod
				+ ", prestazioneDesc="
				+ prestazioneDesc
				+ "]";
	}
	
	
}
