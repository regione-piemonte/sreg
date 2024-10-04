/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

public class Aree {
	private String area;
	private Integer numeroUtenze;

	//altri modelli
	public Aree () {}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getNumeroUtenze() {
		return numeroUtenze;
	}

	public void setNumeroUtenze(Integer numeroUtenze) {
		this.numeroUtenze = numeroUtenze;
	}
	
	
	
}
