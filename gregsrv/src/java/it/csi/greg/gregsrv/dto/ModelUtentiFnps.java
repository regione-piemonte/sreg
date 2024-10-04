/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.csi.greg.gregsrv.business.entity.GregRUserProfilo;
import it.csi.greg.gregsrv.business.entity.GregTUser;

public class ModelUtentiFnps {
	
	private Integer idUtenza;
	private String codUtenza;
	private String descUtenza;
	private Integer idUtenzaFnps;
	private Double valorePercentuale;
	private boolean utilizzatoPerCalcolo;
	private Integer annoInizioValidita;
	private Integer annoFineValidita;
	private boolean modificabile;
	
	public ModelUtentiFnps () {}

	public ModelUtentiFnps(Object[] obj) {
		super();
		this.idUtenza = (Integer) obj[0];
		this.codUtenza = (String) obj[1];
		this.descUtenza = (String) obj[2];
		this.idUtenzaFnps = (Integer) obj[3];
		this.valorePercentuale = (Double) obj[4];
		this.utilizzatoPerCalcolo = (Boolean) obj[5];
		this.annoInizioValidita = (Integer) obj[6];
		this.annoFineValidita = (Integer) obj[7];
	}

	public Integer getIdUtenza() {
		return idUtenza;
	}

	public void setIdUtenza(Integer idUtenza) {
		this.idUtenza = idUtenza;
	}

	public String getCodUtenza() {
		return codUtenza;
	}

	public void setCodUtenza(String codUtenza) {
		this.codUtenza = codUtenza;
	}

	public String getDescUtenza() {
		return descUtenza;
	}

	public void setDescUtenza(String descUtenza) {
		this.descUtenza = descUtenza;
	}

	public Integer getIdUtenzaFnps() {
		return idUtenzaFnps;
	}

	public void setIdUtenzaFnps(Integer idUtenzaFnps) {
		this.idUtenzaFnps = idUtenzaFnps;
	}

	public Double getValorePercentuale() {
		return valorePercentuale;
	}

	public void setValorePercentuale(Double valorePercentuale) {
		this.valorePercentuale = valorePercentuale;
	}

	public boolean isUtilizzatoPerCalcolo() {
		return utilizzatoPerCalcolo;
	}

	public void setUtilizzatoPerCalcolo(boolean utilizzatoPerCalcolo) {
		this.utilizzatoPerCalcolo = utilizzatoPerCalcolo;
	}

	public Integer getAnnoInizioValidita() {
		return annoInizioValidita;
	}

	public void setAnnoInizioValidita(Integer annoInizioValidita) {
		this.annoInizioValidita = annoInizioValidita;
	}

	public Integer getAnnoFineValidita() {
		return annoFineValidita;
	}

	public void setAnnoFineValidita(Integer annoFineValidita) {
		this.annoFineValidita = annoFineValidita;
	}

	public boolean isModificabile() {
		return modificabile;
	}

	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}
	
	
}