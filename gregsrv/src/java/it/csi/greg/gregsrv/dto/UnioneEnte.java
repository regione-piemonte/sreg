/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class UnioneEnte {
	
	private String codiceRegionaleEnteDest;
	private String codiceRegionaleEnteDaUnire;
	private String denominazioneEnteDest;
	private String denominazioneEnteDaUnire;
	/*private boolean unioneDenominazione;
	private boolean unionePartitaIva;
	private boolean unioneComune;
	private boolean unioneCodIstat;
	private boolean unioneAsl;
	private boolean unioneEMail;
	private boolean unioneTelefono;
	private boolean unionePec;
	private boolean unioneComuni;*/
	private List<Integer> lista;
	private Date dataMerge;
	private Map<String, Boolean>[] listacheckeds;
	
	public String getCodiceRegionaleEnteDest() {
		return codiceRegionaleEnteDest;
	}
	public void setCodiceRegionaleEnteDest(String codiceRegionaleEnteDest) {
		this.codiceRegionaleEnteDest = codiceRegionaleEnteDest;
	}
	public String getCodiceRegionaleEnteDaUnire() {
		return codiceRegionaleEnteDaUnire;
	}
	public void setCodiceRegionaleEnteDaUnire(String codiceRegionaleEnteDaUnire) {
		this.codiceRegionaleEnteDaUnire = codiceRegionaleEnteDaUnire;
	}
	/*public boolean isUnioneDenominazione() {
		return unioneDenominazione;
	}
	public void setUnioneDenominazione(boolean unioneDenominazione) {
		this.unioneDenominazione = unioneDenominazione;
	}
	public boolean isUnionePartitaIva() {
		return unionePartitaIva;
	}
	public void setUnionePartitaIva(boolean unionePartitaIva) {
		this.unionePartitaIva = unionePartitaIva;
	}
	public boolean isUnioneComune() {
		return unioneComune;
	}
	public void setUnioneComune(boolean unioneComune) {
		this.unioneComune = unioneComune;
	}
	public boolean isUnioneCodIstat() {
		return unioneCodIstat;
	}
	public void setUnioneCodIstat(boolean unioneCodIstat) {
		this.unioneCodIstat = unioneCodIstat;
	}
	public boolean isUnioneAsl() {
		return unioneAsl;
	}
	public void setUnioneAsl(boolean unioneAsl) {
		this.unioneAsl = unioneAsl;
	}
	public boolean isUnioneEMail() {
		return unioneEMail;
	}
	public void setUnioneEMail(boolean unioneEMail) {
		this.unioneEMail = unioneEMail;
	}
	public boolean isUnioneTelefono() {
		return unioneTelefono;
	}
	public void setUnioneTelefono(boolean unioneTelefono) {
		this.unioneTelefono = unioneTelefono;
	}
	public boolean isUnionePec() {
		return unionePec;
	}
	public void setUnionePec(boolean unionePec) {
		this.unionePec = unionePec;
	}
	public boolean isUnioneComuni() {
		return unioneComuni;
	}
	public void setUnioneComuni(boolean unioneComuni) {
		this.unioneComuni = unioneComuni;
	}*/
	
	public List<Integer> getLista() {
		return lista;
	}
	public void setLista(List<Integer> lista) {
		this.lista = lista;
	}
	public Date getDataMerge() {
		return dataMerge;
	}
	public void setDataMerge(Date dataMerge) {
		this.dataMerge = dataMerge;
	}
	public String getDenominazioneEnteDest() {
		return denominazioneEnteDest;
	}
	public void setDenominazioneEnteDest(String denominazioneEnteDest) {
		this.denominazioneEnteDest = denominazioneEnteDest;
	}
	public String getDenominazioneEnteDaUnire() {
		return denominazioneEnteDaUnire;
	}
	public void setDenominazioneEnteDaUnire(String denominazioneEnteDaUnire) {
		this.denominazioneEnteDaUnire = denominazioneEnteDaUnire;
	}
	public Map<String, Boolean>[] getListacheckeds() {
		return listacheckeds;
	}
	public void setListacheckeds(Map<String, Boolean>[] listacheckeds) {
		this.listacheckeds = listacheckeds;
	}
	
}
