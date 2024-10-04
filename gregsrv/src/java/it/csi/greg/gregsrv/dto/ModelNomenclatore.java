/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;

public class ModelNomenclatore {
	private Integer idNomenclatore;
	private String codNomenclatore;
	private String descMacroArea;
	private String descSottoArea;
	private String descSottoAreaDet;
	private String descPresidio;
	private String descClassificazionePresidio;
	private String descFunzionePresidio;
	private String descVoce;
	private String descSottoVoce;
	private String descTipoResidenza;
	private String codMacroArea;
	private String codSottoArea;
	private String codSottoAreaDet;
	private String codPresidio;
	private String codClassificazionePresidio;
	private String codFunzionePresidio;
	private String codVoce;
	private String codSottoVoce;
	private String codTipoResidenza;
	private Timestamp dal;
	private Timestamp al;
	private Timestamp dataMin;
	private Timestamp dataCancellazione;
	
	public ModelNomenclatore () {}

	public Integer getIdNomenclatore() {
		return idNomenclatore;
	}

	public void setIdNomenclatore(Integer idNomenclatore) {
		this.idNomenclatore = idNomenclatore;
	}

	public Timestamp getDataMin() {
		return dataMin;
	}

	public void setDataMin(Timestamp dataMin) {
		this.dataMin = dataMin;
	}

	public String getDescMacroArea() {
		return descMacroArea;
	}

	public void setDescMacroArea(String descMacroArea) {
		this.descMacroArea = descMacroArea;
	}

	public String getDescSottoArea() {
		return descSottoArea;
	}

	public void setDescSottoArea(String descSottoArea) {
		this.descSottoArea = descSottoArea;
	}

	public String getDescSottoAreaDet() {
		return descSottoAreaDet;
	}

	public void setDescSottoAreaDet(String descSottoAreaDet) {
		this.descSottoAreaDet = descSottoAreaDet;
	}

	public String getDescPresidio() {
		return descPresidio;
	}

	public void setDescPresidio(String descPresidio) {
		this.descPresidio = descPresidio;
	}

	public String getDescClassificazionePresidio() {
		return descClassificazionePresidio;
	}

	public void setDescClassificazionePresidio(String descClassificazionePresidio) {
		this.descClassificazionePresidio = descClassificazionePresidio;
	}

	public String getDescFunzionePresidio() {
		return descFunzionePresidio;
	}

	public void setDescFunzionePresidio(String descFunzionePresidio) {
		this.descFunzionePresidio = descFunzionePresidio;
	}

	public String getDescVoce() {
		return descVoce;
	}

	public void setDescVoce(String descVoce) {
		this.descVoce = descVoce;
	}

	public String getDescSottoVoce() {
		return descSottoVoce;
	}

	public void setDescSottoVoce(String descSottoVoce) {
		this.descSottoVoce = descSottoVoce;
	}

	public String getDescTipoResidenza() {
		return descTipoResidenza;
	}

	public void setDescTipoResidenza(String descTipoResidenza) {
		this.descTipoResidenza = descTipoResidenza;
	}

	public String getCodMacroArea() {
		return codMacroArea;
	}

	public void setCodMacroArea(String codMacroArea) {
		this.codMacroArea = codMacroArea;
	}

	public String getCodSottoArea() {
		return codSottoArea;
	}

	public void setCodSottoArea(String codSottoArea) {
		this.codSottoArea = codSottoArea;
	}

	public String getCodSottoAreaDet() {
		return codSottoAreaDet;
	}

	public void setCodSottoAreaDet(String codSottoAreaDet) {
		this.codSottoAreaDet = codSottoAreaDet;
	}

	public String getCodPresidio() {
		return codPresidio;
	}

	public void setCodPresidio(String codPresidio) {
		this.codPresidio = codPresidio;
	}

	public String getCodClassificazionePresidio() {
		return codClassificazionePresidio;
	}

	public void setCodClassificazionePresidio(String codClassificazionePresidio) {
		this.codClassificazionePresidio = codClassificazionePresidio;
	}

	public String getCodFunzionePresidio() {
		return codFunzionePresidio;
	}

	public void setCodFunzionePresidio(String codFunzionePresidio) {
		this.codFunzionePresidio = codFunzionePresidio;
	}

	public String getCodVoce() {
		return codVoce;
	}

	public void setCodVoce(String codVoce) {
		this.codVoce = codVoce;
	}

	public String getCodSottoVoce() {
		return codSottoVoce;
	}

	public void setCodSottoVoce(String codSottoVoce) {
		this.codSottoVoce = codSottoVoce;
	}

	public String getCodTipoResidenza() {
		return codTipoResidenza;
	}

	public void setCodTipoResidenza(String codTipoResidenza) {
		this.codTipoResidenza = codTipoResidenza;
	}

	public Timestamp getDal() {
		return dal;
	}

	public void setDal(Timestamp dal) {
		this.dal = dal;
	}

	public Timestamp getAl() {
		return al;
	}

	public void setAl(Timestamp al) {
		this.al = al;
	}

	public String getCodNomenclatore() {
		return codNomenclatore;
	}

	public void setCodNomenclatore(String codNomenclatore) {
		this.codNomenclatore = codNomenclatore;
	}

	public Timestamp getDataCancellazione() {
		return dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	

}
