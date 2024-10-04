/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.exception;

public class ParametriObbligatoriException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6566744962579058185L;

	private String codMsg;
	private String descrizioneMsg;
	
	public ParametriObbligatoriException(String valoreMsg) {
		super(valoreMsg);
	}
	
	public ParametriObbligatoriException(String codMsg, String valoreMsg) {
		this.descrizioneMsg = valoreMsg;
		this.codMsg = codMsg;
	}
	
	public String getCodMsg() {
		return codMsg;
	}

	public void setCodMsg(String codMsg) {
		this.codMsg = codMsg;
	}

	public String getDescrizioneMsg() {
		return descrizioneMsg;
	}

	public void setDescrizioneMsg(String descrizioneMsg) {
		this.descrizioneMsg = descrizioneMsg;
	}
}
