/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigInteger;
import java.util.List;

public class ResultCreaAnno {
	private Integer idScheda;
	private Integer anno;
	private String cf;
	private String esito;
	private String errore;
	
	//altri modelli
	public ResultCreaAnno () {}

	
	public ResultCreaAnno(Object[] o) {
		this.idScheda = ((BigInteger) o[1]).intValue() ;
		this.anno = ((BigInteger) o[0]).intValue();
		this.cf = (String) o[2];
		this.esito = (String) o[3];
		this.errore = o.length==5 ? (String) o[4] : null;
	}


	public Integer getIdScheda() {
		return idScheda;
	}

	public void setIdScheda(Integer idScheda) {
		this.idScheda = idScheda;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getErrore() {
		return errore;
	}

	public void setErrore(String errore) {
		this.errore = errore;
	}

	
	
}
