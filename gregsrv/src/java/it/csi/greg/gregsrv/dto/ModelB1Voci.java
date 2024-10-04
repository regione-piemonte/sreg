/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelB1Voci {

	private String codPrestazione;
	private String descPrestazione;
	private String tipoPrestazione;
	private String msgInformativo;
	private String msgInformativoQSA;
	private List<ModelB1Macroaggregati> macroaggregati;
	private List<ModelB1UtenzaPrestReg1> utenze;
	private List<ModelB1VociPrestReg2> prestazioniRegionali2;
	private List<ModelB1UtenzaPrestReg1> utenzeCostoTotale;
	private List<ModelB1UtenzaPrestReg1> utenzeQuotaSocioAssistenziale;

	public ModelB1Voci() {}

	
	public String getCodPrestazione() {
		return codPrestazione;
	}



	public void setCodPrestazione(String codPrestazione) {
		this.codPrestazione = codPrestazione;
	}



	public String getDescPrestazione() {
		return descPrestazione;
	}



	public void setDescPrestazione(String descPrestazione) {
		this.descPrestazione = descPrestazione;
	}

	public String getTipoPrestazione() {
		return tipoPrestazione;
	}


	public void setTipoPrestazione(String tipoPrestazione) {
		this.tipoPrestazione = tipoPrestazione;
	}


	public List<ModelB1Macroaggregati> getMacroaggregati() {
		return macroaggregati;
	}


	public void setMacroaggregati(List<ModelB1Macroaggregati> macroaggregati) {
		this.macroaggregati = macroaggregati;
	}


	public List<ModelB1UtenzaPrestReg1> getUtenze() {
		return utenze;
	}


	public void setUtenze(List<ModelB1UtenzaPrestReg1> utenze) {
		this.utenze = utenze;
	}


	public List<ModelB1VociPrestReg2> getPrestazioniRegionali2() {
		return prestazioniRegionali2;
	}


	public void setPrestazioniRegionali2(List<ModelB1VociPrestReg2> prestazioniRegionali2) {
		this.prestazioniRegionali2 = prestazioniRegionali2;
	}


	public List<ModelB1UtenzaPrestReg1> getUtenzeCostoTotale() {
		return utenzeCostoTotale;
	}


	public void setUtenzeCostoTotale(List<ModelB1UtenzaPrestReg1> utenzeCostoTotale) {
		this.utenzeCostoTotale = utenzeCostoTotale;
	}


	public List<ModelB1UtenzaPrestReg1> getUtenzeQuotaSocioAssistenziale() {
		return utenzeQuotaSocioAssistenziale;
	}


	public void setUtenzeQuotaSocioAssistenziale(List<ModelB1UtenzaPrestReg1> utenzeQuotaSocioAssistenziale) {
		this.utenzeQuotaSocioAssistenziale = utenzeQuotaSocioAssistenziale;
	}


	public String getMsgInformativo() {
		return msgInformativo;
	}


	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}


	public String getMsgInformativoQSA() {
		return msgInformativoQSA;
	}


	public void setMsgInformativoQSA(String msgInformativoQSA) {
		this.msgInformativoQSA = msgInformativoQSA;
	}

	
	
}




