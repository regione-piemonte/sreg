/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import it.csi.greg.gregsrv.business.entity.GregDAsl;
import it.csi.greg.gregsrv.business.entity.GregDProvince;
import it.csi.greg.gregsrv.business.entity.GregRSchedeEntiGestoriComuni;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.util.Checker;

public class ModelDatiAnagrafici {

	private Integer idSchedaEntegestore;
	private String codiceRegionale;
	private String codiceFiscale;
	private String denominazione;
	private String partitaIva;
	private ModelTipoEnte tipoEnte;
	private ModelComune comune;
	private String codiceIstat;
	private ModelDatiAsl asl;
	private String email;
	private String telefono;
	private String pec;
	private ModelResponsabileEnte responsabileEnte;

	public ModelDatiAnagrafici() {
	}

	public ModelDatiAnagrafici(Object[] obj) {
		this.codiceRegionale = String.valueOf(obj[0]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[0]);
		this.codiceFiscale = String.valueOf(obj[1]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[1]);
		this.denominazione = String.valueOf(obj[2]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[2]);
		this.partitaIva = String.valueOf(obj[3]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[3]);
		this.email = String.valueOf(obj[13]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[13]);
		this.telefono = String.valueOf(obj[14]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[14]);
		this.pec = String.valueOf(obj[15]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[15]);
		this.codiceIstat = String.valueOf(obj[8]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[8]);
		this.responsabileEnte = new ModelResponsabileEnte(
				String.valueOf(obj[17]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[17]),
				String.valueOf(obj[16]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[16]),
				String.valueOf(obj[18]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[18]),
				String.valueOf(obj[19]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[19]),
				String.valueOf(obj[20]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[20]),
				String.valueOf(obj[21]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[21]));
		this.comune = new ModelComune(String.valueOf(obj[6]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[6]),
				String.valueOf(obj[7]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[7]),
				String.valueOf(obj[9]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[9]),
				String.valueOf(obj[10]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[10]));
		this.tipoEnte = new ModelTipoEnte(String.valueOf(obj[4]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[4]),
				String.valueOf(obj[5]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[5]));
		this.asl = new ModelDatiAsl(String.valueOf(obj[11]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[11]),
				String.valueOf(obj[12]).equalsIgnoreCase("null") ? "" : String.valueOf(obj[12]));

	}

	public Integer getIdSchedaEntegestore() {
		return idSchedaEntegestore;
	}

	public void setIdSchedaEntegestore(Integer idSchedaEntegestore) {
		this.idSchedaEntegestore = idSchedaEntegestore;
	}

	public String getCodiceRegionale() {
		return codiceRegionale;
	}

	public void setCodiceRegionale(String codiceRegionale) {
		this.codiceRegionale = codiceRegionale;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public ModelTipoEnte getTipoEnte() {
		return tipoEnte;
	}

	public void setTipoEnte(ModelTipoEnte tipoEnte) {
		this.tipoEnte = tipoEnte;
	}

	public ModelComune getComune() {
		return comune;
	}

	public void setComune(ModelComune comune) {
		this.comune = comune;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	public ModelDatiAsl getAsl() {
		return asl;
	}

	public void setAsl(ModelDatiAsl asl) {
		this.asl = asl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPec() {
		return pec;
	}

	public void setPec(String pec) {
		this.pec = pec;
	}

	public ModelResponsabileEnte getResponsabileEnte() {
		return responsabileEnte;
	}

	public void setResponsabileEnte(ModelResponsabileEnte responsabileEnte) {
		this.responsabileEnte = responsabileEnte;
	}

}