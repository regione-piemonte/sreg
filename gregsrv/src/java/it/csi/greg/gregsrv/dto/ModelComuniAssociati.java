/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.time.format.DateTimeFormatter;
import java.util.Date;

import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregRSchedeEntiGestoriComuni;
import it.csi.greg.gregsrv.util.Converter;


public class ModelComuniAssociati {
	
	private Integer pkIdSchedaComuneAssociato;
	private String codiceIstat;
	private String desComune;
	private Integer idComune;
	private String dal;
	private String al;
	
	public ModelComuniAssociati() { }
	
	public ModelComuniAssociati(GregRSchedeEntiGestoriComuni comAssociato,Integer annoGestione) {
		this.pkIdSchedaComuneAssociato = comAssociato.getIdSchedaEnteGestoreComune();
		this.codiceIstat = comAssociato.getGregDComuni().getCodIstatComune();
		this.desComune = comAssociato.getGregDComuni().getDesComune();
		this.idComune = comAssociato.getGregDComuni().getIdComune();
		this.dal = Converter.getStringFromTimestamp(comAssociato.getDataInizioValidita());
		if (comAssociato.getDataFineValidita()!=null) {
        if (Converter.getAnno(comAssociato.getDataFineValidita()).equalsIgnoreCase(annoGestione.toString()))
		this.al = Converter.getStringFromTimestamp(comAssociato.getDataFineValidita());
		}
	}
	
	public ModelComuniAssociati(GregDComuni comAssociato) {
		this.pkIdSchedaComuneAssociato = null;
		this.codiceIstat = comAssociato.getCodIstatComune();
		this.desComune = comAssociato.getDesComune();
		this.idComune = comAssociato.getIdComune();
	}

	public ModelComuniAssociati(Object[] obj) {
		this.pkIdSchedaComuneAssociato = (Integer) obj[0];
		this.codiceIstat = String.valueOf(obj[1]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[1]);
		this.desComune = String.valueOf(obj[2]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[2]);
		this.dal = String.valueOf(obj[3]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[3]);
		this.idComune = (Integer) obj[4];
	}

	public Integer getPkIdSchedaComuneAssociato() {
		return pkIdSchedaComuneAssociato;
	}

	public void setPkIdSchedaComuneAssociato(Integer pkIdSchedaComuneAssociato) {
		this.pkIdSchedaComuneAssociato = pkIdSchedaComuneAssociato;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	public String getDesComune() {
		return desComune;
	}

	public void setDesComune(String desComune) {
		this.desComune = desComune;
	}

	public Integer getIdComune() {
		return idComune;
	}

	public void setIdComune(Integer idComune) {
		this.idComune = idComune;
	}

	public String getDal() {
		return dal;
	}

	public void setDal(String dal) {
		this.dal = dal;
	}

	public String getAl() {
		return al;
	}

	public void setAl(String al) {
		this.al = al;
	}	
	
}