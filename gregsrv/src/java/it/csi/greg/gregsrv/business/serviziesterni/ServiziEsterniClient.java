/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.serviziesterni;

import java.util.logging.Logger;

public class ServiziEsterniClient {		
	private final static Logger log = Logger.getLogger(ServiziEsterniClient.class.getName());
	
	private static ServiziEsterniClient instance;

	private ServiziEsterniClient() {
	}

	public static ServiziEsterniClient getInstance() {
		if (instance == null) {
			instance = new ServiziEsterniClient();
			try {				
								
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}
