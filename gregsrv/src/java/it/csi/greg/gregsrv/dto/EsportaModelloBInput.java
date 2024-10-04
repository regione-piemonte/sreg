/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.List;

public class EsportaModelloBInput {

	private Integer idEnte;
	private BigDecimal totaleSpesaTutteMissioni;
	private BigDecimal totaleSpesaCorrenteMis1204;
	private BigDecimal totaleSpesaCorrenteMis01041215;
	private BigDecimal totaleSpesaCorrenteTutteMissioni;
	private ModelRendicontazioneModB datiB;
	private List<ModelBMissioni> missioniB;
	private List<String> rowTotaleSpeseContoCapitale;
	private List<String> rowTotaleSpeseIncrementoAttFinanz;
	private List<String> rowTotaleMissione;
	private List<String> rowTotaleSpeseCorrenti;
	private String denominazioneEnte;
	
	public EsportaModelloBInput() { }

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public BigDecimal getTotaleSpesaTutteMissioni() {
		return totaleSpesaTutteMissioni;
	}

	public void setTotaleSpesaTutteMissioni(BigDecimal totaleSpesaTutteMissioni) {
		this.totaleSpesaTutteMissioni = totaleSpesaTutteMissioni;
	}

	public BigDecimal getTotaleSpesaCorrenteMis1204() {
		return totaleSpesaCorrenteMis1204;
	}

	public void setTotaleSpesaCorrenteMis1204(BigDecimal totaleSpesaCorrenteMis1204) {
		this.totaleSpesaCorrenteMis1204 = totaleSpesaCorrenteMis1204;
	}

	public BigDecimal getTotaleSpesaCorrenteMis01041215() {
		return totaleSpesaCorrenteMis01041215;
	}

	public void setTotaleSpesaCorrenteMis01041215(BigDecimal totaleSpesaCorrenteMis01041215) {
		this.totaleSpesaCorrenteMis01041215 = totaleSpesaCorrenteMis01041215;
	}

	public BigDecimal getTotaleSpesaCorrenteTutteMissioni() {
		return totaleSpesaCorrenteTutteMissioni;
	}

	public void setTotaleSpesaCorrenteTutteMissioni(BigDecimal totaleSpesaCorrenteTutteMissioni) {
		this.totaleSpesaCorrenteTutteMissioni = totaleSpesaCorrenteTutteMissioni;
	}

	public ModelRendicontazioneModB getDatiB() {
		return datiB;
	}

	public void setDatiB(ModelRendicontazioneModB datiB) {
		this.datiB = datiB;
	}

	public List<ModelBMissioni> getMissioniB() {
		return missioniB;
	}

	public void setMissioniB(List<ModelBMissioni> missioniB) {
		this.missioniB = missioniB;
	}

	public List<String> getRowTotaleSpeseContoCapitale() {
		return rowTotaleSpeseContoCapitale;
	}

	public void setRowTotaleSpeseContoCapitale(List<String> rowTotaleSpeseContoCapitale) {
		this.rowTotaleSpeseContoCapitale = rowTotaleSpeseContoCapitale;
	}

	public List<String> getRowTotaleSpeseIncrementoAttFinanz() {
		return rowTotaleSpeseIncrementoAttFinanz;
	}

	public void setRowTotaleSpeseIncrementoAttFinanz(List<String> rowTotaleSpeseIncrementoAttFinanz) {
		this.rowTotaleSpeseIncrementoAttFinanz = rowTotaleSpeseIncrementoAttFinanz;
	}

	public List<String> getRowTotaleMissione() {
		return rowTotaleMissione;
	}

	public void setRowTotaleMissione(List<String> rowTotaleMissione) {
		this.rowTotaleMissione = rowTotaleMissione;
	}

	public List<String> getRowTotaleSpeseCorrenti() {
		return rowTotaleSpeseCorrenti;
	}

	public void setRowTotaleSpeseCorrenti(List<String> rowTotaleSpeseCorrenti) {
		this.rowTotaleSpeseCorrenti = rowTotaleSpeseCorrenti;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

}

