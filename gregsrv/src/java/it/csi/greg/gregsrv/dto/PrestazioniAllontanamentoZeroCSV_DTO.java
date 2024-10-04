/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class PrestazioniAllontanamentoZeroCSV_DTO {

	private Integer prestazioneId;
	private String prestazioneCod;
	private String prestazioneDesc;
	private String valorePerPrestazioneAlZero;
	private String valorePerFamiglieMinoriB1;
	private String tooltipDesc;

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

	public String getValorePerPrestazioneAlZero() {
		return valorePerPrestazioneAlZero;
	}

	public void setValorePerPrestazioneAlZero(String valorePerPrestazioneAlZero) {
		this.valorePerPrestazioneAlZero = valorePerPrestazioneAlZero;
	}

	public String getValorePerFamiglieMinoriB1() {
		return valorePerFamiglieMinoriB1;
	}

	public void setValorePerFamiglieMinoriB1(String valorePerFamiglieMinoriB1) {
		this.valorePerFamiglieMinoriB1 = valorePerFamiglieMinoriB1;
	}

	public String getTooltipDesc() {
		return tooltipDesc;
	}

	public void setTooltipDesc(String tooltipDesc) {
		this.tooltipDesc = tooltipDesc;
	}
}
