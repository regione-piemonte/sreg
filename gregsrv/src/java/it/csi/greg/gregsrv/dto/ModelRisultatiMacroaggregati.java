/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class ModelRisultatiMacroaggregati {
	
	private Integer idSpesaMacro;
	private String codSpesaMissione;
	private String codMacroaggregato;
	private String descrizioneMissioneCartacea;
	private String descrizioneProgrammaCartaceo;
	private String msgInformativo;
	private String descrizioneMacroaggregato;	
	private BigDecimal valore;	
	private Integer idRendicontazioneEnte;
	private Integer idSchedaEnteGestore;
	private Integer annoGestione;
	
	public ModelRisultatiMacroaggregati () {}

	public Integer getIdSpesaMacro() {
		return idSpesaMacro;
	}



	public void setIdSpesaMacro(Integer idSpesaMacro) {
		this.idSpesaMacro = idSpesaMacro;
	}



	public String getDescrizioneMissioneCartacea() {
		return descrizioneMissioneCartacea;
	}



	public void setDescrizioneMissioneCartacea(String descrizioneMissioneCartacea) {
		this.descrizioneMissioneCartacea = descrizioneMissioneCartacea;
	}



	public String getDescrizioneProgrammaCartaceo() {
		return descrizioneProgrammaCartaceo;
	}



	public void setDescrizioneProgrammaCartaceo(String descrizioneProgrammaCartaceo) {
		this.descrizioneProgrammaCartaceo = descrizioneProgrammaCartaceo;
	}



	public String getDescrizioneMacroaggregato() {
		return descrizioneMacroaggregato;
	}



	public void setDescrizioneMacroaggregato(String descrizioneMacroaggregato) {
		this.descrizioneMacroaggregato = descrizioneMacroaggregato;
	}



	public String getMsgInformativo() {
		return msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}

	public Integer getIdSchedaEnteGestore() {
		return idSchedaEnteGestore;
	}

	public void setIdSchedaEnteGestore(Integer idSchedaEnteGestore) {
		this.idSchedaEnteGestore = idSchedaEnteGestore;
	}

	public Integer getAnnoGestione() {
		return annoGestione;
	}

	public void setAnnoGestione(Integer annoGestione) {
		this.annoGestione = annoGestione;
	}
	
	public String getCodSpesaMissione() {
		return codSpesaMissione;
	}

	public void setCodSpesaMissione(String codSpesaMissione) {
		this.codSpesaMissione = codSpesaMissione;
	}

	public String getCodMacroaggregato() {
		return codMacroaggregato;
	}

	public void setCodMacroaggregato(String codMacroaggregato) {
		this.codMacroaggregato = codMacroaggregato;
	}
	
}