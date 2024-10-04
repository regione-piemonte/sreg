/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.exception;

public class IntegritaException extends Exception {
	private String codMsg;
	private String descrizioneMsg;
	private String file;
	/**
	 * 
	 */
	private static final long serialVersionUID = -6566744962579058185L;

	public IntegritaException(String codMsg, String valoreMsg) {
		this.descrizioneMsg = valoreMsg;
		this.codMsg = codMsg;
	}
	
	public IntegritaException(String codMsg, String valoreMsg, String file) {
		this.descrizioneMsg = valoreMsg;
		this.codMsg = codMsg;
		this.file = file;
	}
	
	public IntegritaException(String valoreMsg) {
		super(valoreMsg);
	}
	

	public IntegritaException() {
		super();
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

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
