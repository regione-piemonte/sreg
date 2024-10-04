/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;


public class ModelStatoCruscotto {
	
	private String codStato; 
	private String desStato; 
	private Integer nEnti;
	
	public ModelStatoCruscotto () {}
	
	public ModelStatoCruscotto(Object[] obj) { 
		this.codStato = String.valueOf(obj[0]);
		this.desStato = String.valueOf(obj[1]);
		this.nEnti = (Integer) obj[2];
	}

	public String getCodStato() {
		return codStato;
	}

	public void setCodStato(String codStato) {
		this.codStato = codStato;
	}

	public String getDesStato() {
		return desStato;
	}

	public void setDesStato(String desStato) {
		this.desStato = desStato;
	}

	public Integer getnEnti() {
		return nEnti;
	}

	public void setnEnti(Integer nEnti) {
		this.nEnti = nEnti;
	}

}
