/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import it.csi.greg.gregsrv.business.entity.GregTMacroaggregatiBilancio;

public class ModelMacroaggregati {
	private Integer idMacroaggregati;
	private String codMacroaggregati;
	private String descMacroaggregati;
	private String codificaMacroaggregati;
	private String altraDescMacroaggregati;
	private String msgInformativo;
	private int ordinamento;
	
	public ModelMacroaggregati () {}
	
	public ModelMacroaggregati (GregTMacroaggregatiBilancio Macroaggregati) {
		this.idMacroaggregati = Macroaggregati.getIdMacroaggregatoBilancio();
		this.codMacroaggregati = Macroaggregati.getCodMacroaggregatoBilancio();
		this.descMacroaggregati = Macroaggregati.getDesMacroaggregatoBilancio();
		this.codificaMacroaggregati=Macroaggregati.getCodificaMacroaggregatoBilancio();
		this.altraDescMacroaggregati=Macroaggregati.getAltraDescMacroaggregatoBilancio();
		this.msgInformativo=Macroaggregati.getMsgInformativo();
		this.ordinamento = Macroaggregati.getOrdinamento();
	}

	public Integer getIdMacroaggregati() {
		return idMacroaggregati;
	}

	public void setIdMacroaggregato(Integer idMacroaggregati) {
		this.idMacroaggregati = idMacroaggregati;
	}

	public String getCodMacroaggregati() {
		return codMacroaggregati;
	}

	public void setCodMacroaggregato(String codMacroaggregati) {
		this.codMacroaggregati = codMacroaggregati;
	}

	public String getDescMacroaggregati() {
		return descMacroaggregati;
	}

	public void setDescMacroaggregato(String descMacroaggregati) {
		this.descMacroaggregati = descMacroaggregati;
	}

	public String getCodificaMacroaggregati() {
		return codificaMacroaggregati;
	}

	public void setCodificaMacroaggregato(String codificaMacroaggregati) {
		this.codificaMacroaggregati = codificaMacroaggregati;
	}

	public String getAltraDescMacroaggregati() {
		return altraDescMacroaggregati;
	}

	public void setAltraDescMacroaggregato(String altraDescMacroaggregati) {
		this.altraDescMacroaggregati = altraDescMacroaggregati;
	}

	public String getMsgInformativo() {
		return msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public int getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}
	
}
