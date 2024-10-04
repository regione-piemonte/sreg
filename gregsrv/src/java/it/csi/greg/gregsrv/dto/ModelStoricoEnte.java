/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.Date;

import it.csi.greg.gregsrv.business.entity.GregTCronologia;

public class ModelStoricoEnte {
	

	private String operatore;
	private Date dal;
	private Date al;

	
	public ModelStoricoEnte() { }


	public ModelStoricoEnte(Object[] o) {
		this.operatore = (String) o[2];
		this.dal = (Date) o[0];
		this.al = (Date) o[1];
	}




	public String getOperatore() {
		return operatore;
	}


	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}


	public Date getDal() {
		return dal;
	}


	public void setDal(Date dal) {
		this.dal = dal;
	}


	public Date getAl() {
		return al;
	}


	public void setAl(Date al) {
		this.al = al;
	}
	

	
}