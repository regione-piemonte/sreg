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

public class ModelFondi {
	
	private Integer idFondo;
	private String codFondo;
	private String descFondo;
	private Integer idRegola;
	private String codRegola;
	private String descRegola;
	private BigDecimal valore;
	private String codTipologiaFondo;
	private String funzioneRegola;
	private boolean notModificabile;
	private Integer idFondoRendicontazione;
	private Integer idEnte;
	private Integer idSpesaFnps;
	private BigDecimal valoreSpesaFnps;
	private boolean leps;
	private String msgInformativo;
	
	public ModelFondi () {}

	public ModelFondi(Object[] obj) {
		this.idFondo = (Integer) obj[0];
		this.codFondo = (String) obj[1];
		this.descFondo = (String) obj[2];
		this.idRegola = obj[3]!=null ? (Integer) obj[3] : null;
		this.codRegola = (String) obj[4];
		this.descRegola = (String) obj[5];
		this.valore = (BigDecimal) obj[6];
		this.codTipologiaFondo = (String) obj[7];
		this.funzioneRegola = obj[8]!=null ? (String) obj[8] : null;
		this.idFondoRendicontazione = (Integer) obj[9];
		this.idEnte = (Integer) obj[10];
		this.leps = obj[11]!=null ? (Boolean) obj[11] : false;
		this.msgInformativo = (String) obj[12];
	}

	public Integer getIdFondo() {
		return idFondo;
	}

	public void setIdFondo(Integer idFondo) {
		this.idFondo = idFondo;
	}

	public String getCodFondo() {
		return codFondo;
	}

	public void setCodFondo(String codFondo) {
		this.codFondo = codFondo;
	}

	public String getDescFondo() {
		return descFondo;
	}

	public void setDescFondo(String descFondo) {
		this.descFondo = descFondo;
	}

	public Integer getIdRegola() {
		return idRegola;
	}

	public void setIdRegola(Integer idRegola) {
		this.idRegola = idRegola;
	}

	public String getCodRegola() {
		return codRegola;
	}

	public void setCodRegola(String codRegola) {
		this.codRegola = codRegola;
	}

	public String getDescRegola() {
		return descRegola;
	}

	public void setDescRegola(String descRegola) {
		this.descRegola = descRegola;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public String getCodTipologiaFondo() {
		return codTipologiaFondo;
	}

	public void setCodTipologiaFondo(String codTipologiaFondo) {
		this.codTipologiaFondo = codTipologiaFondo;
	}

	public String getFunzioneRegola() {
		return funzioneRegola;
	}

	public void setFunzioneRegola(String funzioneRegola) {
		this.funzioneRegola = funzioneRegola;
	}

	public boolean isNotModificabile() {
		return notModificabile;
	}

	public void setNotModificabile(boolean notModificabile) {
		this.notModificabile = notModificabile;
	}

	public Integer getIdFondoRendicontazione() {
		return idFondoRendicontazione;
	}

	public void setIdFondoRendicontazione(Integer idFondoRendicontazione) {
		this.idFondoRendicontazione = idFondoRendicontazione;
	}

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public Integer getIdSpesaFnps() {
		return idSpesaFnps;
	}

	public void setIdSpesaFnps(Integer idSpesaFnps) {
		this.idSpesaFnps = idSpesaFnps;
	}

	public BigDecimal getValoreSpesaFnps() {
		return valoreSpesaFnps;
	}

	public void setValoreSpesaFnps(BigDecimal valoreSpesaFnps) {
		this.valoreSpesaFnps = valoreSpesaFnps;
	}

	public boolean isLeps() {
		return leps;
	}

	public void setLeps(boolean leps) {
		this.leps = leps;
	}

	public String getMsgInformativo() {
		return msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

}