/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class ModelB1ElencoLbl {
	private List<ModelB1Lbl> macroaggregati;
	private List<ModelB1Lbl> missione_programma;
	private List<ModelB1Lbl> utenze;
	private List<ModelB1Lbl> msgInformativi;
	
	
	public ModelB1ElencoLbl() {}


	public List<ModelB1Lbl> getMacroaggregati() {
		return macroaggregati;
	}


	public void setMacroaggregati(List<ModelB1Lbl> macroaggregati) {
		this.macroaggregati = macroaggregati;
	}


	public List<ModelB1Lbl> getMissione_programma() {
		return missione_programma;
	}


	public void setMissione_programma(List<ModelB1Lbl> missione_programma) {
		this.missione_programma = missione_programma;
	}


	public List<ModelB1Lbl> getUtenze() {
		return utenze;
	}


	public void setUtenze(List<ModelB1Lbl> utenze) {
		this.utenze = utenze;
	}


	public List<ModelB1Lbl> getMsgInformativi() {
		return msgInformativi;
	}


	public void setMsgInformativi(List<ModelB1Lbl> msgInformativi) {
		this.msgInformativi = msgInformativi;
	}
	
	
	
}
