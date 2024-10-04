/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class EsportaModelloFInput {

	private Integer idEnte;
	private ModelRendicontazioneModF rendModF;
	private List<String> totaleCP;
	private List<String> totaleRP;
	private String totaleTotCP;
	private List<String> totaleCOpP;
	private String totaleTotCOpP;
	private List<String> totaleRO;
	private List<String> totaleCO;
	private String totaleTotCO;
	private List<String> totaleCOpO;
	private String totaleTotCOpO;
	private List<String> totaleCOpAttO;
	private String totaleTotCOpAttO;
	private String denominazioneEnte;
	private List<String> monteOreSettInterno;
	private String totaleMonteOreSettInterno;
	private List<String> monteOreSettEsterno;
	private String totaleMonteOreSettEsterno;
	
	public EsportaModelloFInput() { }

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public ModelRendicontazioneModF getRendModF() {
		return rendModF;
	}

	public void setRendModF(ModelRendicontazioneModF rendModF) {
		this.rendModF = rendModF;
	}

	public List<String> getTotaleCP() {
		return totaleCP;
	}

	public void setTotaleCP(List<String> totaleCP) {
		this.totaleCP = totaleCP;
	}

	public List<String> getTotaleRP() {
		return totaleRP;
	}

	public void setTotaleRP(List<String> totaleRP) {
		this.totaleRP = totaleRP;
	}

	public String getTotaleTotCP() {
		return totaleTotCP;
	}

	public void setTotaleTotCP(String totaleTotCP) {
		this.totaleTotCP = totaleTotCP;
	}

	public List<String> getTotaleCOpP() {
		return totaleCOpP;
	}

	public void setTotaleCOpP(List<String> totaleCOpP) {
		this.totaleCOpP = totaleCOpP;
	}

	public String getTotaleTotCOpP() {
		return totaleTotCOpP;
	}

	public void setTotaleTotCOpP(String totaleTotCOpP) {
		this.totaleTotCOpP = totaleTotCOpP;
	}

	public List<String> getTotaleRO() {
		return totaleRO;
	}

	public void setTotaleRO(List<String> totaleRO) {
		this.totaleRO = totaleRO;
	}

	public List<String> getTotaleCO() {
		return totaleCO;
	}

	public void setTotaleCO(List<String> totaleCO) {
		this.totaleCO = totaleCO;
	}

	public String getTotaleTotCO() {
		return totaleTotCO;
	}

	public void setTotaleTotCO(String totaleTotCO) {
		this.totaleTotCO = totaleTotCO;
	}

	public List<String> getTotaleCOpO() {
		return totaleCOpO;
	}

	public void setTotaleCOpO(List<String> totaleCOpO) {
		this.totaleCOpO = totaleCOpO;
	}

	public String getTotaleTotCOpO() {
		return totaleTotCOpO;
	}

	public void setTotaleTotCOpO(String totaleTotCOpO) {
		this.totaleTotCOpO = totaleTotCOpO;
	}

	public List<String> getTotaleCOpAttO() {
		return totaleCOpAttO;
	}

	public void setTotaleCOpAttO(List<String> totaleCOpAttO) {
		this.totaleCOpAttO = totaleCOpAttO;
	}

	public String getTotaleTotCOpAttO() {
		return totaleTotCOpAttO;
	}

	public void setTotaleTotCOpAttO(String totaleTotCOpAttO) {
		this.totaleTotCOpAttO = totaleTotCOpAttO;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

	public List<String> getMonteOreSettInterno() {
		return monteOreSettInterno;
	}

	public void setMonteOreSettInterno(List<String> monteOreSettInterno) {
		this.monteOreSettInterno = monteOreSettInterno;
	}

	public String getTotaleMonteOreSettInterno() {
		return totaleMonteOreSettInterno;
	}

	public void setTotaleMonteOreSettInterno(String totaleMonteOreSettInterno) {
		this.totaleMonteOreSettInterno = totaleMonteOreSettInterno;
	}

	public List<String> getMonteOreSettEsterno() {
		return monteOreSettEsterno;
	}

	public void setMonteOreSettEsterno(List<String> monteOreSettEsterno) {
		this.monteOreSettEsterno = monteOreSettEsterno;
	}

	public String getTotaleMonteOreSettEsterno() {
		return totaleMonteOreSettEsterno;
	}

	public void setTotaleMonteOreSettEsterno(String totaleMonteOreSettEsterno) {
		this.totaleMonteOreSettEsterno = totaleMonteOreSettEsterno;
	}

}

