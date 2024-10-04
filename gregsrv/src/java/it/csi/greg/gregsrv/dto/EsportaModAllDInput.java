/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.List;

public class EsportaModAllDInput {

	private Integer idEnte;
	private String annoGestione;
	private ModelRendicontazioneModAllD datiFnps;
	private List<ModelPrestazioneAllD> prestazioniFnps;
	private List<ModelUtenzaAllD> utenzeFnps;
	private List<BigDecimal> totaleUtenze;
	private List<Aree> areeUtenze; 
	private String fnps;
	private String denominazioneEnte;
	private String totaloneUtenze;
	private String residuo;
	private String giustificazione;
	private String residuoAzioni;
	
	public EsportaModAllDInput() { }

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public String getAnnoGestione() {
		return annoGestione;
	}

	public void setAnnoGestione(String annoGestione) {
		this.annoGestione = annoGestione;
	}

	public ModelRendicontazioneModAllD getDatiFnps() {
		return datiFnps;
	}

	public void setDatiFnps(ModelRendicontazioneModAllD datiFnps) {
		this.datiFnps = datiFnps;
	}

	public List<ModelPrestazioneAllD> getPrestazioniFnps() {
		return prestazioniFnps;
	}

	public void setPrestazioniFnps(List<ModelPrestazioneAllD> prestazioniFnps) {
		this.prestazioniFnps = prestazioniFnps;
	}

	public List<ModelUtenzaAllD> getUtenzeFnps() {
		return utenzeFnps;
	}

	public void setUtenzeFnps(List<ModelUtenzaAllD> utenzeFnps) {
		this.utenzeFnps = utenzeFnps;
	}

	public List<BigDecimal> getTotaleUtenze() {
		return totaleUtenze;
	}

	public void setTotaleUtenze(List<BigDecimal> totaleUtenze) {
		this.totaleUtenze = totaleUtenze;
	}

	public List<Aree> getAreeUtenze() {
		return areeUtenze;
	}

	public void setAreeUtenze(List<Aree> areeUtenze) {
		this.areeUtenze = areeUtenze;
	}

	public String getFnps() {
		return fnps;
	}

	public void setFnps(String fnps) {
		this.fnps = fnps;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

	public String getTotaloneUtenze() {
		return totaloneUtenze;
	}

	public void setTotaloneUtenze(String totaloneUtenze) {
		this.totaloneUtenze = totaloneUtenze;
	}

	public String getResiduo() {
		return residuo;
	}

	public void setResiduo(String residuo) {
		this.residuo = residuo;
	}

	public String getGiustificazione() {
		return giustificazione;
	}

	public void setGiustificazione(String giustificazione) {
		this.giustificazione = giustificazione;
	}

	public String getResiduoAzioni() {
		return residuoAzioni;
	}

	public void setResiduoAzioni(String residuoAzioni) {
		this.residuoAzioni = residuoAzioni;
	}

}

