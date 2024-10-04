/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class ModelPrestazioneRegoleAllD {
	private String codPrestReg1;
	private String codTipologia;
	private String regola;
	
	public ModelPrestazioneRegoleAllD(String codPrestReg1, String codTipologia, String regola) {
		super();
		this.codPrestReg1 = codPrestReg1;
		this.codTipologia = codTipologia;
		this.regola = regola;
	}

	public String getCodPrestReg1() {
		return codPrestReg1;
	}

	public void setCodPrestReg1(String codPrestReg1) {
		this.codPrestReg1 = codPrestReg1;
	}

	public String getCodTipologia() {
		return codTipologia;
	}

	public void setCodTipologia(String codTipologia) {
		this.codTipologia = codTipologia;
	}

	public String getRegola() {
		return regola;
	}

	public void setRegola(String regola) {
		this.regola = regola;
	}


}
