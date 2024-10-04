/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import it.csi.greg.gregsrv.business.entity.GregCParametri;

public class ModelParametro {
	
	private Integer idParametro;
	private String codParametro;
	private String desParametro;
	private String informativa;
	private BigDecimal valNum;
	private Integer valInt;
	private String valtext;
	private Boolean valBool;
	private Timestamp valDataDa;
	private Timestamp valDataA;
	private String utenteOperazione;
	private Timestamp dataCreazione;
	private Timestamp dataModifica;
	private Timestamp dataCancellazione;
	
	public ModelParametro() {}
	
	public ModelParametro(GregCParametri parametro) {
		this.idParametro = parametro.getIdParametro();
		this.codParametro = parametro.getCodParametro();
		this.desParametro = parametro.getDesParametro();
		this.informativa = parametro.getInformativa();
		this.valNum = parametro.getValNum();
		this.valInt = parametro.getValInt();
		this.valtext = parametro.getValText();
		this.valBool = parametro.getValBool();
		this.valDataDa = parametro.getValDataDa();
		this.valDataA = parametro.getValDataA();
		this.utenteOperazione = parametro.getUtenteOperazione();
		this.dataCreazione = parametro.getDataCreazione();
		this.dataModifica = parametro.getDataModifica();
		this.dataCancellazione = parametro.getDataCancellazione();
	}

	public Integer getIdParametro() {
		return idParametro;
	}

	public void setIdParametro(Integer idParametro) {
		this.idParametro = idParametro;
	}

	public String getCodParametro() {
		return codParametro;
	}

	public void setCodParametro(String codParametro) {
		this.codParametro = codParametro;
	}

	public String getDesParametro() {
		return desParametro;
	}

	public void setDesParametro(String desParametro) {
		this.desParametro = desParametro;
	}

	public String getInformativa() {
		return informativa;
	}

	public void setInformativa(String informativa) {
		this.informativa = informativa;
	}

	public BigDecimal getValNum() {
		return valNum;
	}

	public void setValNum(BigDecimal valNum) {
		this.valNum = valNum;
	}

	public Integer getValInt() {
		return valInt;
	}

	public void setValInt(Integer valInt) {
		this.valInt = valInt;
	}

	public String getValtext() {
		return valtext;
	}

	public void setValtext(String valtext) {
		this.valtext = valtext;
	}

	public Boolean getValBool() {
		return valBool;
	}

	public void setValBool(Boolean valBool) {
		this.valBool = valBool;
	}

	public Timestamp getValDataDa() {
		return valDataDa;
	}

	public void setValDataDa(Timestamp valDataDa) {
		this.valDataDa = valDataDa;
	}

	public Timestamp getValDataA() {
		return valDataA;
	}

	public void setValDataA(Timestamp valDataA) {
		this.valDataA = valDataA;
	}

	public String getUtenteOperazione() {
		return utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Timestamp getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(Timestamp dataModifica) {
		this.dataModifica = dataModifica;
	}

	public Timestamp getDataCancellazione() {
		return dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}
}