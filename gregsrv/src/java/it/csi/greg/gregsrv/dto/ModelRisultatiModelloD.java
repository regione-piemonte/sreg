/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;

public class ModelRisultatiModelloD {
	
	private Integer idTipoVoceModD;
	private String descrizioneVoceModello;
	private String codiceVoceModello;
	private String msgInformativo;
	private String operatoreMatematico;
	private String sezioneModello;
	private String descrizioneTipoVoce;
	private String codiceTipoVoce;
	private Boolean dataEntry;	
	private BigDecimal valore;	
	private Integer idRendicontazioneEnte;
	private Integer idSchedaEnteGestore;
	private Integer annoGestione;
	
	public ModelRisultatiModelloD () {}
	
	public ModelRisultatiModelloD (Object[] obj) {
		this.idTipoVoceModD = (Integer) obj[0];
		this.descrizioneVoceModello = String.valueOf(obj[1]);
		this.codiceVoceModello = String.valueOf(obj[2]);
		this.msgInformativo = String.valueOf(obj[3]);
		this.operatoreMatematico = String.valueOf(obj[4]);
		this.sezioneModello = String.valueOf(obj[5]);
		this.descrizioneTipoVoce = String.valueOf(obj[6]);
		this.codiceTipoVoce = String.valueOf(obj[7]);
		this.dataEntry = (Boolean) obj[8];
		this.valore = (BigDecimal) obj[9];
		this.idRendicontazioneEnte = (Integer) obj[10];
		this.idSchedaEnteGestore = (Integer) obj[11];
		this.annoGestione = (Integer) obj[12];
	}

	public Integer getIdTipoVoceModD() {
		return idTipoVoceModD;
	}

	public void setIdTipoVoceModD(Integer idTipoVoceModD) {
		this.idTipoVoceModD = idTipoVoceModD;
	}

	public String getDescrizioneVoceModello() {
		return descrizioneVoceModello;
	}

	public void setDescrizioneVoceModello(String descrizioneVoceModello) {
		this.descrizioneVoceModello = descrizioneVoceModello;
	}

	public String getCodiceVoceModello() {
		return codiceVoceModello;
	}

	public void setCodiceVoceModello(String codiceVoceModello) {
		this.codiceVoceModello = codiceVoceModello;
	}

	public String getMsgInformativo() {
		return msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public String getOperatoreMatematico() {
		return operatoreMatematico;
	}

	public void setOperatoreMatematico(String operatoreMatematico) {
		this.operatoreMatematico = operatoreMatematico;
	}

	public String getSezioneModello() {
		return sezioneModello;
	}

	public void setSezioneModello(String sezioneModello) {
		this.sezioneModello = sezioneModello;
	}

	public String getDescrizioneTipoVoce() {
		return descrizioneTipoVoce;
	}

	public void setDescrizioneTipoVoce(String descrizioneTipoVoce) {
		this.descrizioneTipoVoce = descrizioneTipoVoce;
	}

	public String getCodiceTipoVoce() {
		return codiceTipoVoce;
	}

	public void setCodiceTipoVoce(String codiceTipoVoce) {
		this.codiceTipoVoce = codiceTipoVoce;
	}

	public Boolean getDataEntry() {
		return dataEntry;
	}

	public void setDataEntry(Boolean dataEntry) {
		this.dataEntry = dataEntry;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}

	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}

	public Integer getIdSchedaEnteGestore() {
		return idSchedaEnteGestore;
	}

	public void setIdSchedaEnteGestore(Integer idSchedaEnteGestore) {
		this.idSchedaEnteGestore = idSchedaEnteGestore;
	}

	public Integer getAnnoGestione() {
		return annoGestione;
	}

	public void setAnnoGestione(Integer annoGestione) {
		this.annoGestione = annoGestione;
	}	
}