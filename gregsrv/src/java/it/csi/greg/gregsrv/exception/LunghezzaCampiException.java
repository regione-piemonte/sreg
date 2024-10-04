/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.exception;

public class LunghezzaCampiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6566744962579058185L;

	private String codMsg;
	
	public LunghezzaCampiException(String valoreMsg) {
		super(valoreMsg);
	}
	
	public LunghezzaCampiException(String codMsg, String valoreMsg) {
		super(valoreMsg);
		this.codMsg = codMsg;
	}

	public String getCodMsg() {
		return codMsg;
	}

	public void setCodMsg(String codMsg) {
		this.codMsg = codMsg;
	}
}
