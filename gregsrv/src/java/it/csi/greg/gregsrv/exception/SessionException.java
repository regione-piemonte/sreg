/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.exception;

public class SessionException extends Exception {
	
	private String descrizioneMsg;
	private String codMsg;
	/**
	 * 
	 */
	private static final long serialVersionUID = -6566744962579058185L;

	public SessionException(String descrizioneMsg, String codMsg) {
		this.descrizioneMsg = descrizioneMsg;
		this.codMsg = codMsg;
	}

	public SessionException() {
		super();
	}
	
	public SessionException(String valoreMsg) {
		super(valoreMsg);
	}
	
	
	public String getDescrizioneMsg() {
		return descrizioneMsg;
	}

	public void setDescrizioneMsg(String descrizioneMsg) {
		this.descrizioneMsg = descrizioneMsg;
	}

	public String getCodMsg() {
		return codMsg;
	}

	public void setCodMsg(String codMsg) {
		this.codMsg = codMsg;
	}
	
	

}
