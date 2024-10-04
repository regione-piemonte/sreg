/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.sql.Timestamp;
import java.util.List;

public class ModelDettaglioPrestazConfiguratore {
		
	private Integer idPrestazione;
	private String codPrestRegionale;
	private String desPrestRegionale;
	private String codTipologia;
	private String tipoStruttura;
	private String tipoQuota;
	private Integer idTipologia;
	private Integer idTipoStruttura;
	private Integer idTipoQuota;
	private Integer ordinamento;
	private Timestamp dal;
	private Timestamp al;
	private boolean modificabile;
	private List<ModelMacroaggregatiConf> macroaggregati;
	private List<ModelPrestUtenza> targetUtenzaPrestReg1;
	private String notaPrestazione;
	private List<ModelPrest1Prest2> prest1Prest2;
	private List<ModelPrest1PrestMin> prest1PrestMin;
	private List<ModelPrest1PrestCollegate> prestazioniCollegate;
	private Timestamp dataMin;
	private Timestamp dataCreazione;
	
	public ModelDettaglioPrestazConfiguratore () {}


	public Integer getIdTipologia() {
		return idTipologia;
	}


	public void setIdTipologia(Integer idTipologia) {
		this.idTipologia = idTipologia;
	}


	public Integer getIdTipoStruttura() {
		return idTipoStruttura;
	}


	public void setIdTipoStruttura(Integer idTipoStruttura) {
		this.idTipoStruttura = idTipoStruttura;
	}


	public Integer getIdTipoQuota() {
		return idTipoQuota;
	}


	public void setIdTipoQuota(Integer idTipoQuota) {
		this.idTipoQuota = idTipoQuota;
	}


	public Integer getIdPrestazione() {
		return idPrestazione;
	}


	public void setIdPrestazione(Integer idPrestazione) {
		this.idPrestazione = idPrestazione;
	}


	public String getCodTipologia() {
		return codTipologia;
	}


	public void setCodTipologia(String codTipologia) {
		this.codTipologia = codTipologia;
	}

	public String getTipoStruttura() {
		return tipoStruttura;
	}


	public void setTipoStruttura(String tipoStruttura) {
		this.tipoStruttura = tipoStruttura;
	}


	public String getTipoQuota() {
		return tipoQuota;
	}


	public void setTipoQuota(String tipoQuota) {
		this.tipoQuota = tipoQuota;
	}


	public Integer getOrdinamento() {
		return ordinamento;
	}


	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}


	public String getCodPrestRegionale() {
		return codPrestRegionale;
	}


	public void setCodPrestRegionale(String codPrestRegionale) {
		this.codPrestRegionale = codPrestRegionale;
	}


	public String getDesPrestRegionale() {
		return desPrestRegionale;
	}


	public void setDesPrestRegionale(String desPrestRegionale) {
		this.desPrestRegionale = desPrestRegionale;
	}


	public List<ModelMacroaggregatiConf> getMacroaggregati() {
		return macroaggregati;
	}


	public void setMacroaggregati(List<ModelMacroaggregatiConf> macroaggregati) {
		this.macroaggregati = macroaggregati;
	}


	public List<ModelPrestUtenza> getTargetUtenzaPrestReg1() {
		return targetUtenzaPrestReg1;
	}


	public void setTargetUtenzaPrestReg1(List<ModelPrestUtenza> targetUtenzaPrestReg1) {
		this.targetUtenzaPrestReg1 = targetUtenzaPrestReg1;
	}

	public String getNotaPrestazione() {
		return notaPrestazione;
	}

	public void setNotaPrestazione(String notaPrestazione) {
		this.notaPrestazione = notaPrestazione;
	}

	public List<ModelPrest1Prest2> getPrest1Prest2() {
		return prest1Prest2;
	}

	public void setPrest1Prest2(List<ModelPrest1Prest2> prest1Prest2) {
		this.prest1Prest2 = prest1Prest2;
	}
	
	public List<ModelPrest1PrestMin> getPrest1PrestMin() {
		return prest1PrestMin;
	}


	public void setPrest1PrestMin(List<ModelPrest1PrestMin> prest1PrestMin) {
		this.prest1PrestMin = prest1PrestMin;
	}


	public List<ModelPrest1PrestCollegate> getPrestazioniCollegate() {
		return prestazioniCollegate;
	}

	public void setPrestazioniCollegate(List<ModelPrest1PrestCollegate> prestazioniCollegate) {
		this.prestazioniCollegate = prestazioniCollegate;
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


	public boolean isModificabile() {
		return modificabile;
	}


	public void setModificabile(boolean modificabile) {
		this.modificabile = modificabile;
	}


	public Timestamp getDataMin() {
		return dataMin;
	}


	public void setDataMin(Timestamp dataMin) {
		this.dataMin = dataMin;
	}


	public Timestamp getDataCreazione() {
		return dataCreazione;
	}


	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
}